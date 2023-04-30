package com.objectorientedoleg.data.sync

internal interface Syncable {

    suspend fun sync(synchronizer: Synchronizer): Boolean
}