package com.example.ktranslate.favourites_screen

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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.domain.models.WordTranslation
import com.example.ktranslate.KFavButton
import com.example.ktranslate.Loading
import com.example.ktranslate.R
import org.koin.androidx.compose.koinViewModel
import java.sql.Timestamp

@Composable
fun FavouritesView(
    viewModel: FavouritesViewModel = koinViewModel()
) {
    val viewState = viewModel.viewState.collectAsState()
    FavouritesScreen(
        state = viewState.value,
        onUnfavouriteItem = { item ->
            viewModel.obtainEvent(
                FavouritesEvent.UnfavouriteItem(
                    item
                )
            )
        }
    )
}

@Composable
fun FavouritesScreen(state: FavouritesState, onUnfavouriteItem: (WordTranslation) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (state.loadingFavourites) {
            Loading()
        } else if (state.favourites.isEmpty()) {
            FavouritesPlaceHolder()
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxWidth()
            ) {
                items(items = state.favourites, key = { it.id }) { item ->
                    FavouriteItem(
                        item = item,
                        onUnfavouriteClick = { onUnfavouriteItem(item) }
                    )
                }
            }
        }
    }
}

@Composable
fun FavouritesPlaceHolder() {
    Text(
        text = stringResource(R.string.nothing_here)
    )
    Text(
        text = stringResource(R.string.to_add_favourites_return_to_the_main_screen_and_favourite_any_translations_from_your_history)
    )
}

@Composable
fun FavouriteItem(item: WordTranslation, onUnfavouriteClick: (WordTranslation) -> Unit) {
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
        KFavButton(
            isFavourite = item.isFavourite
        ) {
            onUnfavouriteClick(item)
        }
    }
}


@Preview(showSystemUi = true, showBackground = true)
@Composable
fun FavouritesScreenPreview() {
    FavouritesScreen(
        state = FavouritesState(
            favourites = listOf(
                WordTranslation(
                    id = 1,
                    original = "Hello",
                    translated = "Hola",
                    isFavourite = true,
                    isInHistory = true,
                    timestamp = Timestamp(System.currentTimeMillis())
                ),
                WordTranslation(
                    id = 2,
                    original = "Goodbye",
                    translated = "Adiós",
                    isFavourite = true,
                    isInHistory = true,
                    timestamp = Timestamp(System.currentTimeMillis())
                )
            ),
            loadingFavourites = false
        ),
        onUnfavouriteItem = { }
    )
}