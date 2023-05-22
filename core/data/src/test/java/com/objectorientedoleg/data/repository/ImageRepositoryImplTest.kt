package com.objectorientedoleg.data.repository

import com.objectorientedoleg.data.type.ImageType
import com.objectorientedoleg.database.dao.ImageConfigurationDao
import com.objectorientedoleg.database.model.ImageConfigurationEntity
import com.objectorientedoleg.network.MovieRollNetworkDataSource
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Test

private const val BaseUrl = "https://image.tmdb.org/t/p/"

private const val BackdropPath = "/qTkJ6kbTeSjqfHCFCmWnfWZJOtm.jpg"
private val BackdropSizes =
    listOf(
        "w300",
        "w780",
        "w1280",
        "original"
    )

@OptIn(ExperimentalCoroutinesApi::class)
class ImageRepositoryImplTest {

    private val testDispatcher = StandardTestDispatcher()

    private val imageConfigurationDaoMock = mockk<ImageConfigurationDao>()
    private val networkDataSourceMock = mockk<MovieRollNetworkDataSource>()

    @Test
    fun urlFromParams_configUnavailable_nullUrlReturned() = runTest(testDispatcher) {
        mockImageConfigurationUnavailable()
        val subject = createTestSubject()

        val image = subject.getImage(ImageParams(BackdropPath, ImageType.Backdrop))
        assertNull(image)
    }

    @Test
    fun urlFromParams_configAvailable_urlReturned() = runTest(testDispatcher) {
        mockImageConfigurationAvailable()
        val subject = createTestSubject()
        testScheduler.advanceUntilIdle()

        val image = subject.getImage(ImageParams(BackdropPath, ImageType.Backdrop))
            ?: throw AssertionError()
        assertTrue(image.url(300) == "https://image.tmdb.org/t/p/w300/qTkJ6kbTeSjqfHCFCmWnfWZJOtm.jpg")
    }

    private fun createTestSubject() =
        ImageRepositoryImpl(
            imageConfigurationDaoMock,
            networkDataSourceMock,
            TestScope(testDispatcher)
        )

    private fun mockImageConfigurationUnavailable() {
        every {
            imageConfigurationDaoMock.getImageConfiguration()
        } returns flow {
            emit(null)
        }
    }

    private fun mockImageConfigurationAvailable() {
        every {
            imageConfigurationDaoMock.getImageConfiguration()
        } returns flow {
            emit(
                ImageConfigurationEntity(
                    BaseUrl,
                    BackdropSizes,
                    emptyList(),
                    emptyList(),
                    emptyList(),
                    emptyList()
                )
            )
        }
    }
}