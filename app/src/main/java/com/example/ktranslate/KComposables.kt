package com.example.ktranslate

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import com.example.ktranslate.icons.DeleteIcon
import com.example.ktranslate.icons.StarFilledIcon
import com.example.ktranslate.icons.StarIcon

@Composable
fun Loading() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }
}

@Composable
fun KFavButton(isFavourite: Boolean, onClick: () -> Unit) {
    IconButton(
        onClick = onClick
    ) {
        Icon(
            modifier = Modifier.padding(dimensionResource(R.dimen.padding_small)),
            imageVector = if (isFavourite) StarFilledIcon else StarIcon,
            contentDescription = if (isFavourite) stringResource(R.string.unfavourite_icon)
            else stringResource(R.string.favourite_icon),
            tint = MaterialTheme.colorScheme.primary
        )
    }
}

@Composable
fun KDeleteButton(onClick: () -> Unit) {
    IconButton(
        onClick = onClick
    ) {
        Icon(
            modifier = Modifier.padding(dimensionResource(R.dimen.padding_small)),
            imageVector = DeleteIcon,
            contentDescription = stringResource(R.string.delete_icon)
        )
    }
}