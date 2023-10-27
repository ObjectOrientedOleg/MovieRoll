package com.objectorientedoleg.feature.moviedetails

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.objectorientedoleg.core.domain.model.MovieDetailsItem.Credits.Credit
import com.objectorientedoleg.core.ui.components.ImageTextLayout
import com.objectorientedoleg.core.ui.components.SmallProfile
import kotlinx.collections.immutable.ImmutableList

internal fun LazyListScope.creditList(
    creditItems: ImmutableList<Credit>,
    itemModifier: Modifier = Modifier
) {
    items(items = creditItems) { credit ->
        CreditItem(
            modifier = itemModifier,
            credit = credit
        )
    }
}

@Composable
private fun CreditItem(credit: Credit, modifier: Modifier = Modifier) {
    ElevatedCard(
        modifier = modifier,
        shape = MaterialTheme.shapes.small,
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
            contentColor = MaterialTheme.colorScheme.onSecondaryContainer
        )
    ) {
        ImageTextLayout(modifier = Modifier.fillMaxSize()) {
            SmallProfile(
                modifier = Modifier.fillMaxSize(),
                profileUrl = credit.profileUrl,
                contentDescription = credit.name
            )
            Column(
                modifier = Modifier.padding(
                    start = 8.dp,
                    end = 8.dp,
                    bottom = 8.dp
                ),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = credit.name,
                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1
                )
                Text(
                    text = credit.description,
                    style = MaterialTheme.typography.bodySmall,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1
                )
            }
        }
    }
}