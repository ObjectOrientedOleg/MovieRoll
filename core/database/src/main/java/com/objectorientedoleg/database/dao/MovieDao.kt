package com.objectorientedoleg.database.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.objectorientedoleg.database.model.MovieEntity

@Dao
interface MovieDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(movieEntities: List<MovieEntity>)

    @Query("SELECT * FROM movie_table WHERE movie_type = :movieType ORDER BY page ASC, popularity DESC")
    fun getMoviesByType(movieType: String): PagingSource<Int, MovieEntity>
}