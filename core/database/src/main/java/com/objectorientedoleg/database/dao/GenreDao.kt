package com.objectorientedoleg.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.objectorientedoleg.database.model.GenreEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.Instant

@Dao
interface GenreDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(genreEntities: List<GenreEntity>)

    @Query("SELECT creation_date FROM genre_table ORDER BY creation_date DESC")
    suspend fun lastUpdated(): Instant?

    @Query("SELECT * FROM genre_table")
    fun getAll(): Flow<List<GenreEntity>>

    @Query("DELETE FROM genre_table")
    suspend fun deleteAll()

    @Transaction
    suspend fun deleteAndInsertAll(genreEntities: List<GenreEntity>) {
        deleteAll()
        insertAll(genreEntities)
    }
}