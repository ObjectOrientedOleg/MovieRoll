package com.objectorientedoleg.data.repository

import com.objectorientedoleg.data.model.Genre
import com.objectorientedoleg.data.sync.Syncable
import kotlinx.coroutines.flow.Flow

interface GenreRepository : Syncable {

    fun getGenres(): Flow<List<Genre>>
}