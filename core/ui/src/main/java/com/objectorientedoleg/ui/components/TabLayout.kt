package com.objectorientedoleg.ui.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.Dp
import com.objectorientedoleg.ui.components.preview.TabsPreviewParameterProvider
import com.objectorientedoleg.ui.theme.ThemeDefaults
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TabLayout(
    tabCount: Int,
    tabTitle: (index: Int) -> String,
    modifier: Modifier = Modifier,
    edgePadding: Dp = ThemeDefaults.screenEdgePadding,
    tabKey: ((index: Int) -> Any)? = null,
    tabContent: @Composable (index: Int) -> Unit
) {
    Column(modifier = modifier) {
        val pagerState = rememberPagerState { tabCount }
        val scope = rememberCoroutineScope()

        ScrollableTabRow(
            modifier = Modifier.fillMaxWidth(),
            selectedTabIndex = pagerState.currentPage,
            edgePadding = edgePadding,
            indicator = {},
            divider = {}
        ) {
            repeat(tabCount) { index ->
                TabItem(
                    title = tabTitle(index),
                    selected = index == pagerState.currentPage,
                    onClick = { scope.launch { pagerState.animateScrollToPage(index) } }
                )
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
private fun TabItem(
    title: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (selected) {
        FilledTonalButton(
            modifier = modifier,
            onClick = onClick
        ) {
            Text(text = title)
        }
    } else {
        TextButton(
            modifier = modifier,
            onClick = onClick
        ) {
            Text(text = title)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewTabLayout(@PreviewParameter(TabsPreviewParameterProvider::class) tabs: List<String>) {
    TabLayout(
        tabCount = tabs.size,
        tabTitle = { index -> tabs[index] },
        tabContent = {}
    )
}