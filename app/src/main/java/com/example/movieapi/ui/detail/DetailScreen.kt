@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.movieapi.ui.detail

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
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
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.movieapi.BuildConfig
import com.example.movieapi.ui.OnBottomReached
import com.example.movieapi.ui.main.MainState

@Composable
fun DetailScreen (mainUiState: MainState, detailViewModel: DetailViewModel = viewModel()) {
    val uriHandler = LocalUriHandler.current
    val detailUiState by detailViewModel.uiDetailState.collectAsState()

    detailViewModel.newReview(mainUiState.movies!!.id.toString())
    val lazyList = rememberLazyListState()
    val loading = remember { mutableStateOf(false) }

    LazyColumn(
        state = lazyList,
        modifier = Modifier
        .padding(12.dp)
//        .fillMaxSize()
//        .verticalScroll(rememberScrollState())
    ) {
        item{
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                contentAlignment = Alignment.Center,
            ) {
                AsyncImage(
                    model = BuildConfig.BASE_IMAGE + mainUiState.movies!!.posterPath,
                    contentDescription = null,
                    modifier = Modifier.height(245.dp)
                )
            }
            Text(
                text = "${mainUiState.movies!!.originalTitle}",
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "${mainUiState.movies!!.overview}",
            )
//        Text(
//            text = "${mainUiState.movies!!.overview}",
//        )
//        Text(
//            text = "${mainUiState.movies!!.overview}",
//        )
            Text(text = "Trailer")
            Text(text = "")
            Text(text = "User Review")




        }
        loading.value = detailUiState.loadingMoreReview

        items(detailUiState.listReview){
                item ->
            Card(modifier = Modifier.padding(bottom = 12.dp)) {
                Column(Modifier.padding(12.dp)) {
                    Text(
                        text = "${item.author}", fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "${item.createdAt}",
                    )
                    Text(
                        text = "${item.content}",
                    )
                }
            }

        }
//        if(detailUiState.loadingReview){
//            item{
//                Box(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .height(125.dp)
//                        .padding(bottom = 12.dp),
//                    contentAlignment = Alignment.Center,
//                ) {
//                    CircularProgressIndicator()
//                }
//            }
//        }else{
//            loading.value = detailUiState.loadingMoreReview
//
//            items(detailUiState.listReview){
//                    item ->
//                Card(modifier = Modifier.padding(bottom = 12.dp)) {
//                    Column(Modifier.padding(12.dp)) {
//                        Text(
//                            text = "${item.author}", fontWeight = FontWeight.Bold
//                        )
//                        Text(
//                            text = "${item.createdAt}",
//                        )
//                        Text(
//                            text = "${item.content}",
//                        )
//                    }
//                }
//
//            }
//
//
//        }




    }
    lazyList.OnBottomReached {
        // do on load more
        loading.value = true
        detailViewModel.nextReview()
    }
}