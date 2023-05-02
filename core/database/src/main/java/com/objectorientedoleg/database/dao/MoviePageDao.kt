package com.objectorientedoleg.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.objectorientedoleg.database.model.MoviePageEntity
import kotlinx.datetime.Instant

@Dao
interface MoviePageDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(moviePageEntity: MoviePageEntity)

    @Query("SELECT creation_date FROM movie_page_table WHERE movie_type = :movieType ORDER BY creation_date ASC")
    suspend fun lastUpdated(movieType: String): Instant?

    @Query("SELECT * FROM movie_page_table WHERE id = :pageId")
    suspend fun getPage(pageId: String): MoviePageEntity?

    @Query("DELETE FROM movie_page_table WHERE movie_type = :movieType")
    suspend fun deleteAll(movieType: String)
}