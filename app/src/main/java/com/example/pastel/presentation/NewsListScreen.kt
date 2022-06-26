package com.example.pastel.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.pastel.R
import com.example.pastel.data.remote.model.Article
import com.example.pastel.presentation.destinations.NewsDetailScreenDestination
import com.example.pastel.ui.theme.Cream
import com.example.pastel.ui.theme.Dark
import com.example.pastel.ui.theme.Grey
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Composable
@Destination(start = true)
fun NewsListScreen(
    navigator: DestinationsNavigator,
    mainActivityViewModel: MainActivityViewModel = hiltViewModel(),
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        AppBar(title = "NewsFeed")
        Text(
            text = stringResource(R.string.top_news),
            style = TextStyle(
                color = Cream,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold
            ),
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .background(Grey)
                .padding(vertical = 4.dp)
        )
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            items(mainActivityViewModel.uiState.articles) { article ->
                NewsItem(
                    article = article,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            navigator.navigate(
                                NewsDetailScreenDestination(article.url)
                            )
                        }
                )
                Divider(
                    modifier = Modifier.padding(
                        horizontal = 16.dp
                    )
                )
            }
        }
    }
}

@Composable
fun NewsItem(
    article: Article,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(250.dp)
    ) {
        AsyncImage(
            model = article.urlToImage,
            contentDescription = article.description,
            modifier = modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            Color.Black
                        ),
                        startY = 300f
                    )
                )
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            contentAlignment = Alignment.BottomStart
        ) {
            Column(
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = article.title ?: "Title",
                    style = TextStyle(
                        color = Color.White,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                )
                Spacer(modifier = Modifier.height(3.dp))
                Text(
                    text = article.author ?: "Author",
                    style = TextStyle(
                        color = Cream,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Normal
                    )
                )
            }
        }

    }
}


@Composable
fun AppBar(
    title: String
) {
    Row(
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .height(52.dp)
            .background(Dark)

    ) {
        Text(
            text = title,
            fontWeight = FontWeight.SemiBold,
            fontSize = 16.sp,
            color = Color.White,
            modifier = Modifier.padding(start = 12.dp)
        )
    }
}