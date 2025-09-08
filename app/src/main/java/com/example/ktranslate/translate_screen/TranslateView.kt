package com.example.ktranslate.translate_screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.domain.models.WordTranslation
import com.example.ktranslate.icons.CopyIcon
import com.example.ktranslate.icons.StarFilledIcon
import com.example.ktranslate.icons.StarIcon
import org.koin.androidx.compose.koinViewModel
import java.sql.Timestamp

@Composable
fun TranslateView(
    viewModel: TranslateViewModel = koinViewModel()
) {
    val viewState = viewModel.viewState.collectAsState()

    TranslateScreen(
        state = viewState.value,
        onQueryChanged = { viewModel.obtainEvent(TranslateEvent.QueryChanged(it)) },
        onTranslateClicked = { viewModel.obtainEvent(TranslateEvent.TranslateClicked) },
        onFavouriteClicked = { viewModel.obtainEvent(TranslateEvent.FavouriteClicked) },
        onFavouriteItemClicked = { item ->
            viewModel.obtainEvent(
                TranslateEvent.FavouriteItemClicked(
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
    onFavouriteItemClicked: (item: WordTranslation) -> Unit
) {
    val currentTranslation = state.currentTranslation
    val focusManager = LocalFocusManager.current
    val clipboardManager = LocalClipboardManager.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        ElevatedCard(
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                value = state.query,
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
                    }
                )
            )
            if (currentTranslation != null) {
                OutlinedTextField(
                    value = currentTranslation.translated,
                    onValueChange = {},
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    readOnly = true,
                )
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

}


@Composable
@Preview(showSystemUi = true)
fun TranslateViewPreview() {
    TranslateScreen(
        state = TranslateState(
            query = "Hello",
            currentTranslation = WordTranslation(
                id = 1,
                original = "Hello",
                translated = "Hola",
                isFavourite = false,
                timestamp = Timestamp(System.currentTimeMillis())
            ),
            isTranslating = false,
            errorMessage = null,
            history = listOf(
                WordTranslation(
                    id = 1,
                    original = "Hello",
                    translated = "Hola",
                    isFavourite = false,
                    timestamp = Timestamp(System.currentTimeMillis())
                ),
                WordTranslation(
                    id = 2,
                    original = "World",
                    translated = "Mundo",
                    isFavourite = true,
                    timestamp = Timestamp(System.currentTimeMillis())
                ),
                WordTranslation(
                    id = 3,
                    original = "Goodbye",
                    translated = "Adiós",
                    isFavourite = false,
                    timestamp = Timestamp(System.currentTimeMillis())
                )
            ),
            isFavourite = false
        ),
        onQueryChanged = {},
        onTranslateClicked = {},
        onFavouriteClicked = {},
        onFavouriteItemClicked = {}
    )
}