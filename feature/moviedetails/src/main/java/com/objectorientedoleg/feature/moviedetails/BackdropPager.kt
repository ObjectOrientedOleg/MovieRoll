package com.objectorientedoleg.feature.moviedetails

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.objectorientedoleg.core.domain.model.ImageUrl
import com.objectorientedoleg.core.ui.components.Backdrop
import kotlinx.collections.immutable.ImmutableList

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun BackdropPager(
    backdropUrls: ImmutableList<ImageUrl>,
    modifier: Modifier = Modifier
) {
    ElevatedCard(
        modifier = modifier,
        shape = MaterialTheme.shapes.extraLarge
    ) {
        Box {
            val pagerState = rememberPagerState(pageCount = backdropUrls::size)
            val pageText = "${pagerState.currentPage + 1}/${pagerState.pageCount}"

            HorizontalPager(state = pagerState) { page ->
                Backdrop(
                    backdropUrl = backdropUrls[page],
                    contentDescription = null
                )
            }
            Box(
                modifier = Modifier
                    .padding(16.dp)
                    .background(
                        color = MaterialTheme.colorScheme.background.copy(alpha = 0.5f),
                        shape = MaterialTheme.shapes.extraSmall
                    )
                    .padding(horizontal = 4.dp)
                    .align(Alignment.BottomEnd)
            ) {
                Text(
                    text = pageText,
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.labelSmall
                )
            }
        }
    }
}