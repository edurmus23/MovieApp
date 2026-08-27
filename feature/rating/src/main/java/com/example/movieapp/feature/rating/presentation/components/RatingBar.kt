package com.example.movieapp.feature.rating.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.movieapp.core.ui.theme.ImdbYellow
import com.example.movieapp.feature.rating.R

import androidx.compose.ui.tooling.preview.Preview

@Composable
fun InteractiveRatingBar(
    currentRating: Int,
    onRatingSelected: (Int) -> Unit,
    modifier: Modifier = Modifier,
    maxStars: Int = 5
) {
    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            for (i in 1..maxStars) {
                Icon(
                    imageVector = if (i <= currentRating) Icons.Default.Star else Icons.Default.StarBorder,
                    contentDescription = null,
                    tint = if (i <= currentRating) ImdbYellow else Color.Gray.copy(alpha = 0.5f),
                    modifier = Modifier
                        .size(32.dp)
                        .clickable { onRatingSelected(i) }
                )
            }
        }
        
        if (currentRating > 0) {
            Text(
                text = "$currentRating / $maxStars",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.ExtraBold,
                color = ImdbYellow,
                modifier = Modifier.padding(top = 8.dp)
            )
        } else {
            Text(
                text = stringResource(R.string.rating_tap_to_rate),
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
fun InteractiveRatingBarPreview() {
    MaterialTheme {
        Box(modifier = Modifier.padding(16.dp)) {
            InteractiveRatingBar(
                currentRating = 3,
                onRatingSelected = {}
            )
        }
    }
}
