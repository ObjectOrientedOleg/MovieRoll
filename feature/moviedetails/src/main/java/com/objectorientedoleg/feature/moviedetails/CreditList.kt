package com.objectorientedoleg.feature.moviedetails

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.objectorientedoleg.core.domain.model.MovieDetailsItem.Credits.Credit
import com.objectorientedoleg.core.ui.components.CircularElevatedProfile
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
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularElevatedProfile(
            modifier = Modifier.weight(1f),
            profileUrl = credit.profileUrl,
            contentDescription = credit.name
        )
        Spacer(Modifier.height(8.dp))
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