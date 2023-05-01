package com.objectorientedoleg.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant

@Entity(tableName = "image_configuration_table")
data class ImageConfigurationEntity(
    @ColumnInfo(name = "base_url")
    val baseUrl: String,
    @ColumnInfo(name = "backdrop_sizes")
    val backdropSizes: List<String>,
    @ColumnInfo(name = "logo_sizes")
    val logoSizes: List<String>,
    @ColumnInfo(name = "poster_sizes")
    val posterSizes: List<String>,
    @ColumnInfo(name = "profile_sizes")
    val profileSizes: List<String>,
    @ColumnInfo(name = "still_sizes")
    val stillSizes: List<String>,
    @ColumnInfo(name = "creation_date")
    val creationDate: Instant = Clock.System.now(),
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0
)