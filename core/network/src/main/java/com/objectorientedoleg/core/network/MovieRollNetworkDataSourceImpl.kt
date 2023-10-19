package com.objectorientedoleg.core.network

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.objectorientedoleg.core.network.intercepter.OAuthInterceptor
import com.objectorientedoleg.core.network.model.*
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.create
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException

internal class MovieRollNetworkDataSourceImpl @Inject constructor(
    okHttpClient: OkHttpClient,
    json: Json
) : MovieRollNetworkDataSource {

    private val service by lazy<TheMovieDatabaseService> {
        val updatedClient = okHttpClient.newBuilder()
            .addInterceptor(OAuthInterceptor(BuildConfig.TMDB_ACCESS_TOKEN))
            .build()
        Retrofit.Builder()
            .client(updatedClient)
            .baseUrl("https://api.themoviedb.org/3/")
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()
            .create()
    }

    override suspend fun getImageConfiguration(): Result<NetworkImageConfiguration> =
        suspendRunCatching { service.getConfiguration().imageConfiguration }

    override suspend fun getGenres(): Result<NetworkGenres> =
        suspendRunCatching { service.getGenres() }

    override suspend fun getMoviesByGenre(genreId: Int, page: Int): Result<NetworkMovies> =
        suspendRunCatching { service.getMoviesByGenre(genreId, page) }

    override suspend fun getNowPlayingMovies(page: Int): Result<NetworkMovies> =
        suspendRunCatching { service.getNowPlayingMovies(page) }

    override suspend fun getPopularMovies(page: Int): Result<NetworkMovies> =
        suspendRunCatching { service.getPopularMovies(page) }

    override suspend fun getTopRatedMovies(page: Int): Result<NetworkMovies> =
        suspendRunCatching { service.getTopRatedMovies(page) }

    override suspend fun getUpcomingMovies(page: Int): Result<NetworkMovies> =
        suspendRunCatching { service.getUpcomingMovies(page) }

    override suspend fun getMovieDetails(movieId: Int): Result<NetworkMovieDetails> =
        suspendRunCatching { service.getMovieDetails(movieId) }
}

private interface TheMovieDatabaseService {

    @GET("configuration")
    suspend fun getConfiguration(): NetworkConfiguration

    @GET("genre/movie/list")
    suspend fun getGenres(@Query("language") language: String = "en-US"): NetworkGenres

    @GET("discover/movie")
    suspend fun getMoviesByGenre(
        @Query("with_genres") genreId: Int,
        @Query("page") page: Int,
        @Query("include_adult") includeAdult: Boolean = false,
        @Query("include_video") includeVideo: Boolean = false,
        @Query("language") language: String = "en-US",
        @Query("region") region: String = "US"
    ): NetworkMovies

    @GET("movie/now_playing")
    suspend fun getNowPlayingMovies(
        @Query("page") page: Int,
        @Query("language") language: String = "en-US",
        @Query("region") region: String = "US"
    ): NetworkMovies

    @GET("movie/popular")
    suspend fun getPopularMovies(
        @Query("page") page: Int,
        @Query("language") language: String = "en-US",
        @Query("region") region: String = "US"
    ): NetworkMovies

    @GET("movie/top_rated")
    suspend fun getTopRatedMovies(
        @Query("page") page: Int,
        @Query("language") language: String = "en-US",
        @Query("region") region: String = "US"
    ): NetworkMovies

    @GET("movie/upcoming")
    suspend fun getUpcomingMovies(
        @Query("page") page: Int,
        @Query("language") language: String = "en-US",
        @Query("region") region: String = "US"
    ): NetworkMovies

    @GET("movie/{movie_id}")
    suspend fun getMovieDetails(
        @Path("movie_id") movieId: Int,
        @Query("language") language: String = "en-US",
        @Query("append_to_response") appendToResponse: String = "credits,images,release_dates",
        @Query("include_image_language") includeImageLanguage: String = "null"
    ): NetworkMovieDetails
}

private suspend fun <T> suspendRunCatching(block: suspend () -> T): Result<T> {
    return try {
        Result.success(block())
    } catch (cancellationException: CancellationException) {
        throw cancellationException
    } catch (exception: Exception) {
        Result.failure(exception)
    }
}