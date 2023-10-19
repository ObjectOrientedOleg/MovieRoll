package com.objectorientedoleg.core.data.repository

import com.objectorientedoleg.core.data.model.Genre
import com.objectorientedoleg.core.data.sync.Syncable
import kotlinx.coroutines.flow.Flow

interface GenreRepository : Syncable {

    fun getGenres(): Flow<List<Genre>>
}