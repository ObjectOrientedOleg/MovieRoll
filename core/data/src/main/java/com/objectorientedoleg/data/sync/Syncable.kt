package com.objectorientedoleg.data.sync

interface Syncable {

    suspend fun sync(synchronizer: Synchronizer): Boolean
}