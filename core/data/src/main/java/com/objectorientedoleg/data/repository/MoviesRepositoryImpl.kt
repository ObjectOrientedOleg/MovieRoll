package com.objectorientedoleg.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.objectorientedoleg.common.dispatchers.Dispatcher
import com.objectorientedoleg.common.dispatchers.MovieRollDispatchers.*
import com.objectorientedoleg.data.model.Movie
import com.objectorientedoleg.data.model.asModel
import com.objectorientedoleg.data.paging.LocalMoviesDataSource
import com.objectorientedoleg.data.paging.MoviesFirstPageIndex
import com.objectorientedoleg.data.paging.MoviesPageSize
import com.objectorientedoleg.data.paging.MoviesRemoteMediator
import com.objectorientedoleg.data.paging.RemoteMoviesDataSource
import com.objectorientedoleg.data.sync.Synchronizer
import com.objectorientedoleg.data.type.MovieType
import com.objectorientedoleg.database.model.MovieEntity
import com.objectorientedoleg.database.model.MoviePageEntity
import com.objectorientedoleg.database.proxy.MovieRollDatabaseProxy
import com.objectorientedoleg.network.MovieRollNetworkDataSource
import com.objectorientedoleg.network.model.NetworkMovie
import com.objectorientedoleg.network.model.NetworkMovies
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import kotlinx.datetime.Instant
import java.util.UUID
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
internal class MoviesRepositoryImpl @Inject constructor(
    private val database: MovieRollDatabaseProxy,
    private val networkDataSource: MovieRollNetworkDataSource,
    private val synchronizer: Synchronizer,
    @Dispatcher(IO) private val ioDispatcher: CoroutineDispatcher
) : MoviesRepository {

    override fun getMovies(movieQuery: MovieQuery): Flow<PagingData<Movie>> {
        val pager = Pager(
            config = PagingConfig(MoviesPageSize),
            remoteMediator = MoviesRemoteMediator(
                localMoviesDataSource = localMoviesDataSourceFor(movieQuery),
                remoteMoviesDataSource = remoteMoviesDataSourceFor(movieQuery),
                synchronizer = synchronizer,
            ),
            pagingSourceFactory = { database.movieDao.getMoviesByType(movieQuery.name) }
        )
        return pager.flow.map(PagingData<MovieEntity>::asModels)
    }

    override suspend fun sync(synchronizer: Synchronizer): Boolean = withContext(ioDispatcher) {
        val deferredSyncs = MovieType.values().map { movieType ->
            async {
                val movieQuery = MovieQuery.ByType(movieType)
                val localSource = localMoviesDataSourceFor(movieQuery)
                if (synchronizer.isDataValid(localSource.lastUpdated())) {
                    return@async true
                }
                val remoteSource = remoteMoviesDataSourceFor(movieQuery)
                val result = remoteSource.loadMovies(MoviesFirstPageIndex)
                if (result.isFailure) {
                    return@async false
                }
                val networkMovies = result.getOrThrow()
                localSource.onLoadComplete(networkMovies, true)
                // Update was successful.
                true
            }
        }
        deferredSyncs.awaitAll()
            .all { syncedSuccessfully -> syncedSuccessfully }
    }

    private fun localMoviesDataSourceFor(movieQuery: MovieQuery) =
        object : LocalMoviesDataSource {
            override suspend fun lastUpdated(): Instant? =
                database.moviePageDao.lastUpdated(movieQuery.name)

            override suspend fun onLoadComplete(networkMovies: NetworkMovies, isRefresh: Boolean) {
                database.asTransaction {
                    if (isRefresh) {
                        // The delete action is cascaded to all associated movies.
                        database.moviePageDao.deleteAll(movieQuery.name)
                    }

                    val pageId = UUID.randomUUID().toString()
                    database.moviePageDao.insert(networkMovies.asPageEntity(pageId, movieQuery))
                    database.movieDao.insertAll(networkMovies.asMovieEntities(pageId, movieQuery))
                }
            }

            override suspend fun getPage(pageId: String): MoviePageEntity? =
                database.moviePageDao.getPage(pageId)
        }

    private fun remoteMoviesDataSourceFor(movieQuery: MovieQuery) =
        object : RemoteMoviesDataSource {
            override suspend fun loadMovies(page: Int): Result<NetworkMovies> = when (movieQuery) {
                is MovieQuery.ByType -> when (movieQuery.type) {
                    MovieType.NowPlaying -> networkDataSource.getNowPlayingMovies(page)
                    MovieType.Popular -> networkDataSource.getPopularMovies(page)
                    MovieType.TopRated -> networkDataSource.getTopRatedMovies(page)
                    MovieType.UpComing -> networkDataSource.getUpcomingMovies(page)
                }

                is MovieQuery.ByGenreId -> networkDataSource.getMoviesByGenre(movieQuery.id, page)
            }
        }
}

private fun PagingData<MovieEntity>.asModels() = map { it.asModel() }

private fun NetworkMovies.asPageEntity(id: String, movieQuery: MovieQuery) =
    MoviePageEntity(
        id = id,
        movieType = movieQuery.name,
        page = page,
        totalResults = totalResults,
        totalPages = totalPages
    )

private fun NetworkMovies.asMovieEntities(pageId: String, movieQuery: MovieQuery) =
    movies.map { it.asMovieEntity(page, pageId, movieQuery) }

private fun NetworkMovie.asMovieEntity(
    page: Int,
    pageId: String,
    movieQuery: MovieQuery
) =
    MovieEntity(
        adult = adult,
        backdropPath = backdropPath,
        genreIds = genreIds,
        id = buildEntityId(movieQuery),
        movieId = id,
        movieType = movieQuery.name,
        originalLanguage = originalLanguage,
        originalTitle = originalTitle,
        overview = overview,
        page = page,
        pageId = pageId,
        popularity = popularity,
        posterPath = posterPath,
        releaseDate = releaseDate,
        title = title,
        video = video,
        voteAverage = voteAverage,
        voteCount = voteCount
    )

private fun NetworkMovie.buildEntityId(movieQuery: MovieQuery) = "${id}_${movieQuery.name}"