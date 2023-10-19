package com.objectorientedoleg.core.data.model

import com.objectorientedoleg.core.database.model.GenreEntity

data class Genre(val id: String, val name: String)

fun GenreEntity.asModel(): Genre = Genre(id = id, name = name)


