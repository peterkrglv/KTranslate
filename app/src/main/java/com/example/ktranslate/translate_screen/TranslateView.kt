package com.example.ktranslate.translate_screen

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.example.domain.models.WordTranslation
import com.example.ktranslate.KDeleteButton
import com.example.ktranslate.KFavButton
import com.example.ktranslate.R
import com.example.ktranslate.icons.CopyIcon
import org.koin.androidx.compose.koinViewModel
import java.sql.Timestamp

@Composable
fun TranslateView(
    viewModel: TranslateViewModel = koinViewModel()
) {
    val viewState = viewModel.viewState.collectAsState()
    val viewAction = viewModel.viewAction.collectAsState()
    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == androidx.lifecycle.Lifecycle.Event.ON_RESUME) {
                viewModel.obtainEvent(TranslateEvent.RenewHistory)
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    LaunchedEffect(viewAction.value) {
        when (val action = viewAction.value) {
            is TranslateAction.ConnectionLost -> {
                Toast.makeText(
                    context,
                    context.getString(R.string.no_internet_connection), Toast.LENGTH_LONG
                ).show()
                viewModel.clearAction()
            }

            is TranslateAction.ConnectionAvailable -> {
                Toast.makeText(
                    context,
                    context.getString(R.string.internet_connection_restored), Toast.LENGTH_LONG
                )
                    .show()
                viewModel.clearAction()
            }

            null -> {}
        }
    }

    TranslateScreen(
        state = viewState.value,
        onQueryChanged = { viewModel.obtainEvent(TranslateEvent.QueryChanged(it)) },
        onTranslateClicked = { viewModel.obtainEvent(TranslateEvent.TranslateClicked) },
        onFavouriteClicked = { viewModel.obtainEvent(TranslateEvent.FavouriteClicked) },
        onFavouriteItemClicked = { item ->
            viewModel.obtainEvent(
                TranslateEvent.FavouriteHistoryItemClicked(
                    item
                )
            )
        },
        onDeleteHistoryItemClicked = { item ->
            viewModel.obtainEvent(
                TranslateEvent.DeleteHistoryItemClicked(
                    item
                )
            )
        }
    )
}

@Composable
fun TranslateScreen(
    state: TranslateState,
    onQueryChanged: (String) -> Unit,
    onTranslateClicked: () -> Unit,
    onFavouriteClicked: () -> Unit,
    onFavouriteItemClicked: (item: WordTranslation) -> Unit,
    onDeleteHistoryItemClicked: (WordTranslation) -> Unit
) {
    val currentTranslation = state.currentTranslation
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(dimensionResource(R.dimen.padding_small)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TranslationCard(
            query = state.query,
            currentTranslation = currentTranslation,
            onQueryChanged = onQueryChanged,
            onTranslateClicked = onTranslateClicked,
            onFavouriteClicked = onFavouriteClicked,
            translationNotFound = state.translationNotFound
        )
        History(
            history = state.history,
            onFavouriteItemClicked = onFavouriteItemClicked,
            onDeleteHistoryItemClicked = onDeleteHistoryItemClicked,
            isHistoryLoading = state.isHistoryLoading
        )
    }
}

@Composable
fun TranslationCard(
    query: String,
    currentTranslation: WordTranslation?,
    onQueryChanged: (String) -> Unit,
    onTranslateClicked: () -> Unit,
    onFavouriteClicked: () -> Unit,
    translationNotFound: Boolean
) {
    val focusManager = LocalFocusManager.current
    val clipboardManager = LocalClipboardManager.current
    ElevatedCard(
        modifier = Modifier.fillMaxWidth()
    ) {
        val context = LocalContext.current
        OutlinedTextField(
            value = query,
            onValueChange = onQueryChanged,
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimensionResource(R.dimen.padding_small)),
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    onTranslateClicked()
                    focusManager.clearFocus()
                }
            )
        )
        if (currentTranslation != null || translationNotFound) {
            OutlinedTextField(
                value = currentTranslation?.translated ?: stringResource(R.string.nothing_found),
                onValueChange = {},
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(dimensionResource(R.dimen.padding_small)),
                readOnly = true,
                isError = translationNotFound
            )
        }
        if (currentTranslation != null) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                IconButton(
                    onClick = {
                        clipboardManager.setText(AnnotatedString(currentTranslation.translated))
                        Toast.makeText(
                            context,
                            context.getString(R.string.the_translation_has_been_copied_to_clipboard),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                ) {
                    Icon(
                        modifier = Modifier.padding(horizontal = dimensionResource(R.dimen.padding_small)),
                        imageVector = CopyIcon,
                        contentDescription = stringResource(R.string.copy_icon)
                    )
                }
                KFavButton(isFavourite = currentTranslation.isFavourite) {
                    onFavouriteClicked()
                }
            }
        }
    }
}

@Composable
fun History(
    history: List<WordTranslation>,
    onFavouriteItemClicked: (item: WordTranslation) -> Unit,
    onDeleteHistoryItemClicked: (item: WordTranslation) -> Unit,
    isHistoryLoading: Boolean
) {
    Text(
        modifier = Modifier.padding(dimensionResource(R.dimen.padding_small)),
        text = stringResource(R.string.history),
        fontSize = dimensionResource(R.dimen.font_size_header_small).value.sp
    )
    if (history.isEmpty() && !isHistoryLoading) {
        Text(
            text = stringResource(R.string.nothing_here),
            fontSize = dimensionResource(R.dimen.font_size_body_big).value.sp,
            modifier = Modifier.padding(dimensionResource(R.dimen.padding_small))
        )
    }
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding()
    ) {
        items(
            items = history,
            key = { it.id }
        ) { item ->
            HistoryItem(
                item = item,
                onFavouriteItemClicked = onFavouriteItemClicked,
                onDeleteHistoryItemClicked = onDeleteHistoryItemClicked
            )
        }
    }
}


@Composable
fun HistoryItem(
    item: WordTranslation,
    onFavouriteItemClicked: (WordTranslation) -> Unit,
    onDeleteHistoryItemClicked: (WordTranslation) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(dimensionResource(R.dimen.padding_small))
            .height(48.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .weight(1f)
        ) {
            Text(
                text = item.original,
                fontSize = dimensionResource(R.dimen.font_size_body_big).value.sp
            )
            Text(
                text = item.translated,
                fontSize = dimensionResource(R.dimen.font_size_body_medium).value.sp
            )
        }
        KDeleteButton {
            onDeleteHistoryItemClicked(item)
        }
        KFavButton(item.isFavourite) {
            onFavouriteItemClicked(item)
        }
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun TranslateViewPreview() {
    TranslateScreen(
        state = TranslateState(
            query = "Hello",
            currentTranslation = WordTranslation(
                id = 1,
                original = "Hello",
                translated = "Hola",
                isFavourite = false,
                timestamp = Timestamp(System.currentTimeMillis()),
                isInHistory = true
            ),
            isTranslating = false,
            errorMessage = null,
            history = listOf(
                WordTranslation(
                    id = 1,
                    original = "Hello",
                    translated = "Hola",
                    isFavourite = false,
                    timestamp = Timestamp(System.currentTimeMillis()),
                    isInHistory = true
                ),
                WordTranslation(
                    id = 2,
                    original = "World",
                    translated = "Mundo",
                    isFavourite = true,
                    timestamp = Timestamp(System.currentTimeMillis()),
                    isInHistory = true
                ),
                WordTranslation(
                    id = 3,
                    original = "Goodbye",
                    translated = "Adiós",
                    isFavourite = false,
                    timestamp = Timestamp(System.currentTimeMillis()),
                    isInHistory = true
                )
            ),
            isFavourite = false
        ),
        onQueryChanged = {},
        onTranslateClicked = {},
        onFavouriteClicked = {},
        onFavouriteItemClicked = {},
        onDeleteHistoryItemClicked = {}
    )
}