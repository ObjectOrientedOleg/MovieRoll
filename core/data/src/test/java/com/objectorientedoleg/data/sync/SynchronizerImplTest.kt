package com.objectorientedoleg.data.sync

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.Clock
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SynchronizerImplTest {

    private lateinit var subject: SynchronizerImpl

    @Before
    fun setUp() {
        subject = SynchronizerImpl()
    }

    @Test
    fun sync_noLastSyncDate_syncIsNeeded_trueReturned() = runTest {
        val syncedSuccessfully = subject.sync(
            syncDate = { null },
            onFetchUpdate = { Result.success("update") },
            onRunUpdate = { value ->
                assertEquals("update", value)
                true
            }
        )
        assertTrue(syncedSuccessfully)
    }

    @Test
    fun sync_syncedRecently_syncNotNeeded_trueReturned() = runTest {
        val syncedSuccessfully = subject.sync<Unit>(
            syncDate = { Clock.System.now() },
            onFetchUpdate = { throw IllegalStateException("Sync not needed.") },
            onRunUpdate = { throw IllegalStateException("Sync not needed.") }
        )
        assertTrue(syncedSuccessfully)
    }

    @Test
    fun sync_syncIsNeeded_updateNotFetched_falseReturned() = runTest {
        val syncedSuccessfully = subject.sync<Unit>(
            syncDate = { null },
            onFetchUpdate = { Result.failure(Exception()) },
            onRunUpdate = { throw IllegalStateException("Update not fetched.") }
        )
        assertFalse(syncedSuccessfully)
    }
}