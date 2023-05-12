package com.objectorientedoleg.data.model

import com.objectorientedoleg.database.model.GenreEntity

data class Genre(val id: Int, val name: String)

fun GenreEntity.asModel(): Genre = Genre(id = id, name = name)


