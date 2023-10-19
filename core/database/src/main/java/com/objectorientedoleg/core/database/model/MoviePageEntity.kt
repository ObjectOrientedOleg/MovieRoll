package com.objectorientedoleg.core.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant

@Entity(tableName = "movie_page_table")
data class MoviePageEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: String,
    @ColumnInfo(name = "movie_type")
    val movieType: String,
    @ColumnInfo(name = "page")
    val page: Int,
    @ColumnInfo(name = "total_results")
    val totalResults: Int,
    @ColumnInfo(name = "total_pages")
    val totalPages: Int,
    @ColumnInfo(name = "creation_date")
    val creationDate: Instant = Clock.System.now()
)