package com.objectorientedoleg.core.data.repository

import androidx.paging.*
import com.objectorientedoleg.core.data.model.Movie
import com.objectorientedoleg.core.data.model.asModel
import com.objectorientedoleg.core.data.paging.LocalMoviesDataSource
import com.objectorientedoleg.core.data.paging.MoviesPageSize
import com.objectorientedoleg.core.data.paging.MoviesRemoteMediator
import com.objectorientedoleg.core.data.paging.RemoteMoviesDataSource
import com.objectorientedoleg.core.data.sync.Synchronizer
import com.objectorientedoleg.core.data.type.MovieType
import com.objectorientedoleg.core.database.model.MovieEntity
import com.objectorientedoleg.core.database.model.MoviePageEntity
import com.objectorientedoleg.core.database.proxy.MovieRollDatabaseProxy
import com.objectorientedoleg.core.network.MovieRollNetworkDataSource
import com.objectorientedoleg.core.network.model.NetworkMovie
import com.objectorientedoleg.core.network.model.NetworkMovies
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.datetime.Instant
import java.util.UUID
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
internal class MoviesRepositoryImpl @Inject constructor(
    private val database: MovieRollDatabaseProxy,
    private val networkDataSource: MovieRollNetworkDataSource,
    private val synchronizer: Synchronizer
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

                is MovieQuery.ByGenreId -> networkDataSource.getMoviesByGenre(
                    genreId = movieQuery.id.toInt(),
                    page = page
                )
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
        movieId = id.toString(),
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