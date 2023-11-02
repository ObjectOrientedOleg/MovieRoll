package com.objectorientedoleg.feature.settings

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.objectorientedoleg.core.model.DarkThemeConfig
import com.objectorientedoleg.core.model.UserTheme

@Composable
fun SettingsDialog(onDismiss: () -> Unit) {
    SettingsDialog(
        onDismiss = onDismiss,
        viewModel = hiltViewModel()
    )
}

@Composable
private fun SettingsDialog(
    onDismiss: () -> Unit,
    viewModel: SettingsViewModel
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    SettingsDialog(
        uiState = uiState,
        onDismiss = onDismiss,
        onDarkThemePreferenceClick = viewModel::updateDarkThemePreference,
        onDynamicColorPreferenceClick = viewModel::updateDynamicColorPreference
    )
}

@Composable
private fun SettingsDialog(
    uiState: SettingsUiState,
    onDismiss: () -> Unit,
    onDarkThemePreferenceClick: (DarkThemeConfig) -> Unit,
    onDynamicColorPreferenceClick: (Boolean) -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.ok))
            }
        },
        title = {
            Text(
                text = stringResource(R.string.title),
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleLarge
            )
        },
        text = {
            Column {
                when (uiState) {
                    is SettingsUiState.Loading -> {
                        CircularProgressIndicator(Modifier.align(Alignment.CenterHorizontally))
                    }

                    is SettingsUiState.Loaded -> {
                        SettingsLoadedContent(
                            userTheme = uiState.userTheme,
                            showDynamicColorPreference = uiState.showDynamicColorPreference,
                            onDarkThemePreferenceClick = onDarkThemePreferenceClick,
                            onDynamicColorPreferenceClick = onDynamicColorPreferenceClick
                        )
                    }
                }
            }
        }
    )
}

@Suppress("UnusedReceiverParameter")
@Composable
private fun ColumnScope.SettingsLoadedContent(
    userTheme: UserTheme,
    showDynamicColorPreference: Boolean,
    onDarkThemePreferenceClick: (DarkThemeConfig) -> Unit,
    onDynamicColorPreferenceClick: (Boolean) -> Unit
) {
    RadioButtonGroup(title = stringResource(R.string.dark_theme)) {
        DarkThemeConfig.values().forEach { config ->
            RadioButtonRow(
                text = stringResource(config.stringRes),
                selected = config == userTheme.darkThemeConfig,
                onClick = { onDarkThemePreferenceClick(config) }
            )
        }
    }
    if (showDynamicColorPreference) {
        Spacer(Modifier.height(24.dp))
        RadioButtonGroup(title = stringResource(R.string.use_dynamic_color)) {
            RadioButtonRow(
                text = stringResource(R.string.yes),
                selected = userTheme.useDynamicColor,
                onClick = { onDynamicColorPreferenceClick(true) }
            )
            RadioButtonRow(
                text = stringResource(R.string.no),
                selected = !userTheme.useDynamicColor,
                onClick = { onDynamicColorPreferenceClick(false) }
            )
        }
    }
}

@Composable
private fun RadioButtonGroup(
    title: String,
    modifier: Modifier = Modifier,
    radioButtons: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.primaryContainer,
                shape = MaterialTheme.shapes.large
            )
            .padding(12.dp)
    ) {
        Text(
            text = title,
            color = MaterialTheme.colorScheme.onPrimaryContainer,
            style = MaterialTheme.typography.titleMedium
        )
        Spacer(Modifier.height(8.dp))
        Column(
            modifier = Modifier.selectableGroup(),
            content = radioButtons
        )
    }
}

@Composable
private fun RadioButtonRow(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .selectable(
                selected = selected,
                role = Role.RadioButton,
                onClick = onClick,
            )
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        RadioButton(
            selected = selected,
            onClick = null,
            colors = RadioButtonDefaults.colors(
                selectedColor = MaterialTheme.colorScheme.onPrimaryContainer
            )
        )
        Spacer(Modifier.width(8.dp))
        Text(
            text = text,
            color = MaterialTheme.colorScheme.onPrimaryContainer,
        )
    }
}

@get:StringRes
private val DarkThemeConfig.stringRes: Int
    get() = when (this) {
        DarkThemeConfig.FollowSystem -> R.string.follow_system
        DarkThemeConfig.Enabled -> R.string.on
        DarkThemeConfig.Disabled -> R.string.off
    }