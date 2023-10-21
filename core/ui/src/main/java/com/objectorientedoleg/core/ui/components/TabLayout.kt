package com.objectorientedoleg.core.ui.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.objectorientedoleg.core.ui.components.preview.TabsPreviewParameterProvider
import com.objectorientedoleg.core.ui.theme.ThemeDefaults
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TabLayout(
    tabCount: Int,
    tabTitle: (index: Int) -> String,
    modifier: Modifier = Modifier,
    pagerState: PagerState = rememberPagerState { tabCount },
    edgePadding: Dp = ThemeDefaults.screenEdgePadding,
    tabKey: ((index: Int) -> Any)? = null,
    tabContent: @Composable (index: Int) -> Unit
) {
    Column(modifier = modifier) {
        val scope = rememberCoroutineScope()

        ScrollableTabRow(
            modifier = Modifier.fillMaxWidth(),
            selectedTabIndex = pagerState.currentPage,
            containerColor = Color.Transparent,
            edgePadding = edgePadding,
            indicator = { tabPositions: List<TabPosition> ->
                val tabPosition = tabPositions[pagerState.currentPage]
                TabIndicator(Modifier.tabIndicatorOffset(tabPosition))
            },
            divider = {}
        ) {
            repeat(tabCount) { index ->
                TextButton(
                    onClick = {
                        scope.launch {
                            pagerState.animateScrollToPage(index)
                        }
                    }
                ) {
                    val title = remember(index) { tabTitle(index) }
                    Text(title)
                }
            }
        }
        HorizontalPager(
            modifier = Modifier.fillMaxSize(),
            state = pagerState,
            key = tabKey,
            userScrollEnabled = false
        ) { index ->
            tabContent(index)
        }
    }
}

@Composable
private fun TabIndicator(modifier: Modifier = Modifier) {
    Box(
        modifier
            .fillMaxWidth()
            .height(3.dp)
            .background(
                color = MaterialTheme.colorScheme.primary,
                shape = MaterialTheme.shapes.extraSmall.copy(
                    bottomEnd = CornerSize(0.dp),
                    bottomStart = CornerSize(0.dp)
                )
            )
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Preview(showBackground = true)
@Composable
fun PreviewTabLayout(@PreviewParameter(TabsPreviewParameterProvider::class) tabs: List<String>) {
    TabLayout(
        tabCount = tabs.size,
        tabTitle = { index -> tabs[index] },
        tabContent = {}
    )
}