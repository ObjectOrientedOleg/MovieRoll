package com.objectorientedoleg.core.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.objectorientedoleg.core.database.model.MovieDetailsEntity

@Dao
interface MovieDetailsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: MovieDetailsEntity)

    @Query("SELECT * FROM movie_details_table WHERE id = :movieId")
    suspend fun getMovieDetails(movieId: String): MovieDetailsEntity?

    @Delete
    suspend fun delete(entity: MovieDetailsEntity)
}