package com.objectorientedoleg.ui.components.preview

import androidx.compose.ui.tooling.preview.PreviewParameterProvider

class TabsPreviewParameterProvider : PreviewParameterProvider<List<String>> {
    override val values: Sequence<List<String>> = sequenceOf(
        listOf(
            "Recommended",
            "Action",
            "Comedy",
            "Horror"
        )
    )
}