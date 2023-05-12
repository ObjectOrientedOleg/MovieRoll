package com.objectorientedoleg.ui.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
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

@Immutable
data class Tab(val title: String, val content: @Composable () -> Unit)