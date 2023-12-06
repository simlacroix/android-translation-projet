package com.example.translationproject.StartPage

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.example.translationproject.R
import com.example.translationproject.UiStates.HistoryItem
import com.example.translationproject.Util.Languages
import com.example.translationproject.Util.codeToName
import com.example.translationproject.Util.getString
import com.example.translationproject.Util.observeLifecycleEvents
import org.koin.androidx.compose.koinViewModel


@Composable
fun StartPage(
    onNavigateToCamera: () -> Unit,
    onNavigateToAccount: () -> Unit,
    viewModel: StartPageViewModel = koinViewModel()
) {
    viewModel.observeLifecycleEvents(LocalLifecycleOwner.current.lifecycle)
    Box(modifier = Modifier.padding(10.dp)) {
        Column {
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                HistoryBottomSheet(
                    viewModel.translationHistory,
                    viewModel.isLoggedIn,
                    { viewModel.askClearHistory() }
                ) {
                    viewModel.toggleFavorite(it)
                }
                TextButton(onClick = { onNavigateToAccount() }) {
                    Text(text = viewModel.getString(R.string.account_log_in), color = Color.Black)
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            TranslationComponent(viewModel = viewModel, onNavigateToCamera = onNavigateToCamera)
        }
    }
}

@Composable
fun TranslationComponent(viewModel: StartPageViewModel, onNavigateToCamera: () -> Unit) {
    val textToTranslate = viewModel.textToTranslate
    val translatedText = viewModel.translatedText
    var scrollState = rememberScrollState()
    Box(
        modifier = Modifier
            .wrapContentHeight()
            .background(color = Color.LightGray, shape = RoundedCornerShape(5.dp))
            .border(width = 2.dp, color = Color.Black, shape = RoundedCornerShape(5.dp))
    ) {
        Column(modifier = Modifier
            .padding(5.dp)
            .verticalScroll(scrollState)) {
            Row(
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                LanguageDropDown(
                    currentSelection = mutableListOf(viewModel.sourceLanguage),
                    label = viewModel.getString(R.string.source_language),
                    onClick = { viewModel.expandedSource = true },
                    isOpen = viewModel.expandedSource,
                    onDismiss = { viewModel.expandedSource = false },
                    isSource = true,
                    onSelectLanguage = { x -> viewModel.changeSourceLanguage(x) })
                IconButton(onClick = { onNavigateToCamera() }) {
                    Icon(
                        painter = painterResource(id = R.drawable.camera),
                        contentDescription = "From photo",
                        tint = Color.Black
                    )
                }
                IconButton(
                    onClick = { viewModel.handleListenClick() },
                    enabled = viewModel.sourceLanguage != null
                ) {
                    Icon(
                        painter = painterResource(R.drawable.microphone),
                        contentDescription = "Listen",
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = textToTranslate,
                textStyle = TextStyle(color = Color.Black),
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                onValueChange = {
                    viewModel.onTextChange(it)
                })
            Spacer(modifier = Modifier.height(16.dp))
            AnimatedVisibility(visible = viewModel.currentlyListening) {
                Icon(
                    imageVector = Icons.Filled.CheckCircle,
                    contentDescription = "Listening",
                    tint = Color.Black
                )
            }
            Row(
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                LanguageDropDown(
                    currentSelection = viewModel.targetLanguages,
                    label = viewModel.getString(R.string.target_language),
                    onClick = { viewModel.expandedTarget = true },
                    isOpen = viewModel.expandedTarget,
                    isSource = false,
                    onDismiss = { viewModel.expandedTarget = false },
                    onSelectLanguage = { x -> viewModel.changeTargetLanguage(x) })
                IconButton(
                    onClick = { viewModel.swap() },
                    enabled = viewModel.targetLanguages.size == 1 && viewModel.sourceLanguage != null && viewModel.translatedText != "..."
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.swap),
                        contentDescription = "Swap",
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = translatedText,
                textStyle = TextStyle(color = Color.Black),
                readOnly = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                onValueChange = {})
            Button(
                onClick = { viewModel.translate() },
                enabled = viewModel.textToTranslate.isNotEmpty() && viewModel.targetLanguages.isNotEmpty()
            ) {
                Text(text = viewModel.getString(R.string.translate))
            }

            if (viewModel.showAlert) {
                AlertDialog(
                    text = { Text(text = viewModel.alertText) },
                    onDismissRequest = { viewModel.showAlert = false },
                    dismissButton = { viewModel.alertDismissButton() },
                    confirmButton = {
                        Button(
                            onClick = { viewModel.alertOnConfirmClick() },
                        ) {
                            Text(text = viewModel.getString(R.string.ok))
                        }
                    })
            }
        }
    }
}

@Composable
fun LanguageDropDown(
    currentSelection: MutableList<Languages?>,
    label: String,
    onClick: () -> Unit,
    isOpen: Boolean,
    isSource: Boolean,
    onDismiss: () -> Unit,
    onSelectLanguage: (Languages?) -> Unit,
) {
    val auto = LocalContext.current.getString(R.string.auto)
    val icon = if (isOpen) {
        Icons.Filled.KeyboardArrowUp
    } else {
        Icons.Filled.KeyboardArrowDown
    }
    DropdownMenu(expanded = isOpen, onDismissRequest = onDismiss) {
        if (isSource) {
            DropdownMenuItem(
                onClick = { onSelectLanguage(null) },
                text = {
                    Text(
                        text = "${if (currentSelection.contains(null)) ">" else ""} $auto",
                        color = if (currentSelection.contains(null)) Color.Black else Color.White
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = if (currentSelection.contains(null)) Color.White else Color.Black)
            )
        }
        Languages.values().asList().forEach { language ->
            DropdownMenuItem(
                onClick = { onSelectLanguage(language) },
                text = {
                    Text(
                        text = "${if (currentSelection.contains(language)) ">" else ""} ${language.name}",
                        color = if (currentSelection.contains(language)) Color.Black else Color.White
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = if (currentSelection.contains(language)) Color.White else Color.Black)
            )
        }
    }
    OutlinedTextField(
        value = if (isSource) currentSelection[0]?.name
            ?: auto else currentSelection.joinToString { it?.name!! },
        textStyle = TextStyle(color = Color.Black),
        onValueChange = {},
        label = { Text(text = label, color = Color.Black) },
        readOnly = true,
        modifier = Modifier.fillMaxWidth(0.6F),
        trailingIcon = {
            Icon(
                imageVector = icon,
                contentDescription = "",
                modifier = Modifier.clickable { onClick() },
                tint = Color.Black
            )
        })
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryBottomSheet(
    history: Array<HistoryItem>,
    isLoggedIn: Boolean,
    askClearHistory: () -> Unit,
    toggleFavorite: (HistoryItem) -> Unit
) {
    val sheetState = rememberModalBottomSheetState()
    var showBottomSheet by remember { mutableStateOf(false) }
    ExtendedFloatingActionButton(
        text = { Text(text = LocalContext.current.getString(R.string.show_history), color = Color.Black) },
        icon = { Icon(Icons.Filled.Info, tint = Color.Black, contentDescription = "") },
        containerColor = MaterialTheme.colorScheme.secondary,
        onClick = {
            showBottomSheet = true
        }
    )
    // Screen content
    if (showBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = {
                showBottomSheet = false
            },
            sheetState = sheetState
        ) {
            // Sheet content
            if (isLoggedIn) {
                IconButton(onClick = { askClearHistory() }) {
                    Icon(
                        painter = painterResource(id = R.drawable.delete),
                        contentDescription = "delete"
                    )
                }
                HistoryTable(history) {
                    toggleFavorite(it)
                }
            } else {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp, vertical = 30.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(text = LocalContext.current.getString(R.string.unlock_history))
                }
            }
        }
    }
}

@Composable
fun HistoryTable(history: Array<HistoryItem>, toggleFavorite: (HistoryItem) -> Unit) {
    LazyColumn(
        modifier = Modifier
            .padding(10.dp)
            .background(color = MaterialTheme.colorScheme.primary, shape = RoundedCornerShape(5.dp))
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                TableCell(text = LocalContext.current.getString(R.string.source), weight = 0.475f)
                TableCell(text = LocalContext.current.getString(R.string.translated), weight = 0.475f)
                TableCell(text = "", weight = 0.05f)
            }
        }
        items(history) { item ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(intrinsicSize = IntrinsicSize.Max),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                val icon =
                    if (item.isFavorite) painterResource(id = R.drawable.star) else painterResource(
                        id = R.drawable.star_outline
                    )
                TableCell(
                    text = "${codeToName(item.sourceLanguage)}: \n${item.sourceText}",
                    weight = 0.475f
                )
                TableCell(
                    text = "${codeToName(item.targetLanguage)}: \n${item.translatedText}",
                    weight = 0.475f
                )
                IconButton(
                    onClick = { toggleFavorite(item) },
                    modifier = Modifier
                        .weight(0.05F)
                        .border(1.dp, Color.Black)
                        .fillMaxHeight()
                ) {
                    Icon(painter = icon, contentDescription = "favorite")
                }
            }
        }
    }
}

@Composable
fun RowScope.TableCell(
    text: String,
    weight: Float
) {
    Text(
        text = text,
        Modifier
            .border(1.dp, Color.Black)
            .weight(weight)
            .padding(8.dp)
            .fillMaxHeight()
    )
}