package com.objectorientedoleg.ui.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import com.objectorientedoleg.ui.preview.TabsProvider
import com.objectorientedoleg.ui.theme.ScreenEdgePadding
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TabLayout(tabs: List<Tab>, modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        val pagerState = rememberPagerState()
        val coroutineScope = rememberCoroutineScope()

        ScrollableTabRow(
            modifier = Modifier.fillMaxWidth(),
            selectedTabIndex = pagerState.currentPage,
            edgePadding = ScreenEdgePadding,
            indicator = {},
            divider = {}
        ) {
            tabs.forEachIndexed { index, tab ->
                TabItem(
                    title = tab.title,
                    selected = index == pagerState.currentPage,
                    onClick = { coroutineScope.launch { pagerState.animateScrollToPage(index) } }
                )
            }
        }
        Spacer(modifier = Modifier.height(ScreenEdgePadding))
        HorizontalPager(
            modifier = Modifier.fillMaxSize(),
            pageCount = tabs.size,
            state = pagerState,
            userScrollEnabled = false
        ) { index ->
            val tab = tabs[index]
            tab.content()
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
        ElevatedButton(
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

data class Tab(val title: String, val content: @Composable () -> Unit)

@Preview(showBackground = true)
@Composable
fun PreviewTabLayout(@PreviewParameter(TabsProvider::class) tabs: List<Tab>) {
    TabLayout(tabs)
}