package com.objectorientedoleg.ui.preview

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.objectorientedoleg.ui.components.Tab

class TabsProvider : PreviewParameterProvider<List<Tab>> {
    override val values: Sequence<List<Tab>> = sequenceOf(fakeTabs())
}