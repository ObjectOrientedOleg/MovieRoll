package com.objectorientedoleg.data.util

import com.objectorientedoleg.data.model.ImageConfiguration
import com.objectorientedoleg.data.model.ImageType
import com.objectorientedoleg.data.model.ImageUrlParams
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertTrue
import org.junit.Before
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

private const val PosterPath = "/pIkRyD18kl4FhoCNQuWxWu5cBLM.jpg"
private val PosterSizes =
    listOf(
        "w92",
        "w154",
        "w185",
        "w342",
        "w500",
        "w780",
        "original"
    )

private const val ProfilePath = "/tL0bxnvJNvCWbysjQuh6lcboJGx.jpg"
private val ProfileSizes =
    listOf(
        "w45",
        "w185",
        "h632",
        "original"
    )

class ImageUrlAssemblerTest {

    private val configurationMock = mockk<ImageConfiguration>()

    private lateinit var subject: ImageUrlAssembler

    @Before
    fun setUp() {
        setUpConfigurationMock()
        subject = ImageUrlAssembler(configurationMock)
    }

    @Test
    fun urlFromParams_validSize_w500PosterUrlReturned() {
        val params = ImageUrlParams(PosterPath, 500, ImageType.Poster)
        val url = subject.urlFromParams(params)
        assertTrue(url == "https://image.tmdb.org/t/p/w500/pIkRyD18kl4FhoCNQuWxWu5cBLM.jpg")
    }

    @Test
    fun urlFromParams_validSize_w185ProfileUrlReturned() {
        val params = ImageUrlParams(ProfilePath, 185, ImageType.Profile)
        val url = subject.urlFromParams(params)
        assertTrue(url == "https://image.tmdb.org/t/p/w185/tL0bxnvJNvCWbysjQuh6lcboJGx.jpg")
    }

    @Test
    fun urlFromParams_validSize_w300BackdropUrlReturned() {
        val params = ImageUrlParams(BackdropPath, 300, ImageType.Backdrop)
        val url = subject.urlFromParams(params)
        assertTrue(url == "https://image.tmdb.org/t/p/w300/qTkJ6kbTeSjqfHCFCmWnfWZJOtm.jpg")
    }

    @Test
    fun urlFromParams_invalidSize_originalPosterUrlReturned() {
        val params = ImageUrlParams(PosterPath, -1, ImageType.Poster)
        val url = subject.urlFromParams(params)
        assertTrue(url == "https://image.tmdb.org/t/p/original/pIkRyD18kl4FhoCNQuWxWu5cBLM.jpg")
    }

    @Test
    fun urlFromParams_invalidSize_originalProfileUrlReturned() {
        val params = ImageUrlParams(ProfilePath, -1, ImageType.Profile)
        val url = subject.urlFromParams(params)
        assertTrue(url == "https://image.tmdb.org/t/p/original/tL0bxnvJNvCWbysjQuh6lcboJGx.jpg")
    }

    @Test
    fun urlFromParams_invalidSize_originalBackdropUrlReturned() {
        val params = ImageUrlParams(BackdropPath, -1, ImageType.Backdrop)
        val url = subject.urlFromParams(params)
        assertTrue(url == "https://image.tmdb.org/t/p/original/qTkJ6kbTeSjqfHCFCmWnfWZJOtm.jpg")
    }

    @Test
    fun urlFromParams_noPosterSizesAvailable_nullUrlReturned() {
        every { configurationMock.posterSizes } returns emptyList()
        subject = ImageUrlAssembler(configurationMock)

        val params = ImageUrlParams(PosterPath, 500, ImageType.Poster)
        val url = subject.urlFromParams(params)
        assertTrue(url == null)
    }

    @Test
    fun urlFromParams_noProfileSizesAvailable_nullUrlReturned() {
        every { configurationMock.profileSizes } returns emptyList()
        subject = ImageUrlAssembler(configurationMock)

        val params = ImageUrlParams(ProfilePath, 500, ImageType.Profile)
        val url = subject.urlFromParams(params)
        assertTrue(url == null)
    }

    @Test
    fun urlFromParams_noBackdropSizesAvailable_nullUrlReturned() {
        every { configurationMock.backdropSizes } returns emptyList()
        subject = ImageUrlAssembler(configurationMock)

        val params = ImageUrlParams(BackdropPath, 300, ImageType.Backdrop)
        val url = subject.urlFromParams(params)
        assertTrue(url == null)
    }

    private fun setUpConfigurationMock() {
        every { configurationMock.baseUrl } returns BaseUrl
        every { configurationMock.backdropSizes } returns BackdropSizes
        every { configurationMock.posterSizes } returns PosterSizes
        every { configurationMock.profileSizes } returns ProfileSizes
    }
}