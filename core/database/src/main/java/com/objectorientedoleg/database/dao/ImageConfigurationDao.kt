package com.objectorientedoleg.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.objectorientedoleg.database.model.ImageConfigurationEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.Instant

@Dao
interface ImageConfigurationDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: ImageConfigurationEntity)

    @Query("SELECT creation_date FROM image_configuration_table ORDER BY creation_date DESC")
    suspend fun lastUpdated(): Instant?

    @Query("SELECT * FROM image_configuration_table ORDER BY creation_date DESC")
    fun getImageConfiguration(): Flow<ImageConfigurationEntity?>

    @Query("DELETE FROM image_configuration_table")
    suspend fun deleteAll()

    @Transaction
    suspend fun deleteAllAndInsert(entity: ImageConfigurationEntity) {
        deleteAll()
        insert(entity)
    }
}