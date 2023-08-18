package com.example.movieapi.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.movieapi.BuildConfig
import com.example.movieapi.model.Movies
import com.example.movieapi.ui.main.MainState
import com.example.movieapi.ui.main.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
//    mainUiState: MainState,
    modifier: Modifier, onDetailClick: (Movies) -> Unit,
    mainViewModel: MainViewModel = viewModel(),
) {
    val mainUiState by mainViewModel.uiState.collectAsState()

    Column(
        modifier = Modifier.padding(24.dp)
//        .verticalScroll(rememberScrollState())
    ) {
        if (mainUiState.loadingGenre) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(45.dp)
                    .padding(bottom = 12.dp),
                contentAlignment = Alignment.Center,
            ) {
                CircularProgressIndicator()
            }
        } else {
            if (mainUiState.listGenres.isNotEmpty()) {
                LazyRow(Modifier.padding(bottom = 12.dp)) {
                    items(mainUiState.listGenres) { genre ->
                        Card(
                            modifier = Modifier
                                .padding(end = 6.dp)
                                .clickable {
                                    mainViewModel.changeGenres(genre.id.toString())
                                },
                            colors = if (genre.id.toString() == mainUiState.selectedGenre) {
                                CardDefaults.cardColors(
                                    containerColor = Color.Red,
                                )
                            } else {
                                CardDefaults.cardColors(
                                    containerColor = Color.Cyan,
                                )
                            },
                        ) {
                            Text(
                                modifier = Modifier.padding(12.dp),
                                text = "${genre.name}",
                            )
                        }
                    }
                }


            }
        }
        val loading = remember { mutableStateOf(false) }

        if (mainUiState.loadingMovie) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(125.dp)
                    .padding(bottom = 12.dp),
                contentAlignment = Alignment.Center,
            ) {
                CircularProgressIndicator()
            }
        } else {
            val lazyList = rememberLazyListState()
            loading.value = mainUiState.loadingMore

            LazyColumn(state = lazyList) {
                items(mainUiState.listMovies) { item ->
                    Card(modifier = Modifier.padding(bottom = 12.dp),
                        onClick = { onDetailClick(item) }
//                            .clickable()
                    ) {
                        Column(Modifier.padding(12.dp)) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(bottom = 12.dp),
                                contentAlignment = Alignment.Center,
                            ) {
                                AsyncImage(
                                    model = BuildConfig.BASE_IMAGE + item.posterPath,
                                    contentDescription = null,
                                    modifier = Modifier.height(245.dp)
                                )
                            }
                            Text(
                                text = "${item.originalTitle}", fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "${item.overview}",
                            )
                        }
                    }
                }
                item {
                    if (loading.value) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(10.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(50.dp),
                                color = Color.Magenta,
                                strokeWidth = 2.dp
                            )
                        }

                    }
                }
            }

            lazyList.OnBottomReached {
                // do on load more
                loading.value = true
                mainViewModel.getNextPage()
            }
        }
    }
}

@Composable
fun LazyListState.OnBottomReached(
    loadMore: () -> Unit
) {
    val shouldLoadMore = remember {
        derivedStateOf {
            val lastVisibleItem = layoutInfo.visibleItemsInfo.lastOrNull()
                ?: return@derivedStateOf true

            lastVisibleItem.index == layoutInfo.totalItemsCount - 1
        }
    }

    LaunchedEffect(shouldLoadMore) {
        snapshotFlow { shouldLoadMore.value }
            .collect {
                if (it) loadMore()
            }
    }
}
