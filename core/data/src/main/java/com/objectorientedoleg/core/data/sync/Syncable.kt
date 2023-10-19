package com.objectorientedoleg.core.data.sync

interface Syncable {

    suspend fun sync(synchronizer: Synchronizer): Boolean
}