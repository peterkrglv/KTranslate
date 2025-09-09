package com.example.ktranslate.translate_screen

import android.util.Log
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
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LifecycleEventObserver
import com.example.domain.models.WordTranslation
import com.example.ktranslate.Loading
import com.example.ktranslate.icons.CopyIcon
import com.example.ktranslate.icons.DeleteIcon
import com.example.ktranslate.icons.StarFilledIcon
import com.example.ktranslate.icons.StarIcon
import org.koin.androidx.compose.koinViewModel
import java.sql.Timestamp

@Composable
fun TranslateView(
    viewModel: TranslateViewModel = koinViewModel()
) {
    val viewState = viewModel.viewState.collectAsState()
    val lifecycleOwner = LocalLifecycleOwner.current

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
            .padding(8.dp),
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
        if (state.isHistoryLoading) Loading()
        else History(
            history = state.history,
            onFavouriteItemClicked = onFavouriteItemClicked,
            onDeleteHistoryItemClicked = onDeleteHistoryItemClicked
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
        OutlinedTextField(
            value = query,
            onValueChange = onQueryChanged,
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
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
                value = currentTranslation?.translated ?: "Nothing found",
                onValueChange = {},
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
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
                    }
                ) {
                    Icon(
                        modifier = Modifier.padding(horizontal = 8.dp),
                        imageVector = CopyIcon,
                        contentDescription = "Copy Icon"
                    )
                }
                IconButton(
                    onClick = onFavouriteClicked
                ) {
                    Icon(
                        modifier = Modifier.padding(horizontal = 8.dp),
                        imageVector = if (currentTranslation.isFavourite) StarFilledIcon else StarIcon,
                        contentDescription = if (currentTranslation.isFavourite) "Unfavourite Icon" else "Favourite Icon"
                    )
                }
            }
        }
    }
}

@Composable
fun History(
    history: List<WordTranslation>,
    onFavouriteItemClicked: (item: WordTranslation) -> Unit,
    onDeleteHistoryItemClicked: (item: WordTranslation) -> Unit
) {
    Text(
        modifier = Modifier.padding(top = 16.dp),
        text = "History"
    )
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 16.dp)
    ) {
        items(
            items = history,
            key = { it.id }
        ) { item ->
            Log.d("History", "Rendering item: $item")
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
            .padding(8.dp)
            .height(48.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .weight(1f)
        ) {
            Text(text = item.original)
            Text(text = item.translated)
        }
        IconButton(
            onClick = { onDeleteHistoryItemClicked(item) }
        ) {
            Icon(
                modifier = Modifier.padding(horizontal = 8.dp),
                imageVector = DeleteIcon,
                contentDescription = "Delete Icon"
            )
        }
        IconButton(
            onClick = { onFavouriteItemClicked(item) }
        ) {
            Icon(
                modifier = Modifier.padding(horizontal = 8.dp),
                imageVector = if (item.isFavourite) StarFilledIcon else StarIcon,
                contentDescription = if (item.isFavourite) "Unfavourite Icon" else "Favourite Icon"
            )
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