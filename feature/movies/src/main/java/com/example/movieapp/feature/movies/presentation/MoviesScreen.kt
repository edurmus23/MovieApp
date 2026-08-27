package com.example.movieapp.feature.movies.presentation

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SmartToy
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import com.example.movieapp.domain.model.MovieDto
import com.example.movieapp.feature.movies.presentation.components.MovieItem
import com.example.movieapp.core.ui.components.shimmerEffect
import com.example.movieapp.domain.util.Constants
import androidx.compose.ui.res.stringResource
import com.example.movieapp.feature.movies.R
import androidx.compose.ui.tooling.preview.Preview
import androidx.paging.PagingData
import kotlinx.coroutines.flow.flowOf

@Composable
fun MoviesScreen(
    viewModel: MovieViewModel = hiltViewModel(),
    onMovieClick: (Int) -> Unit,
    onAiChatClick: () -> Unit
) {
    val movies = viewModel.moviePagingData.collectAsLazyPagingItems()
    val topRatedMovies = viewModel.topRatedMovies.collectAsLazyPagingItems()
    val upcomingMovies = viewModel.upcomingMovies.collectAsLazyPagingItems()
    val bannerMovies by viewModel.bannerMovies.collectAsState()
    val favouriteMovies by viewModel.favouriteMovies.collectAsState()
    val favouriteIds by viewModel.favouriteIds.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(viewModel.authError) {
        viewModel.authError.collect { message ->
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }
    //İçindekiler
    MoviesContent(
        movies = movies,
        topRatedMovies = topRatedMovies,
        upcomingMovies = upcomingMovies,
        bannerMovies = bannerMovies,
        favouriteMovies = favouriteMovies,
        favouriteIds = favouriteIds,
        onToggleFavourite = viewModel::onToggleFavourite,
        onMovieClick = onMovieClick,
        onAiChatClick = onAiChatClick
    )
}

@Composable
fun HeroBannerCarousel(
    movies: List<MovieDto>,
    onMovieClick: (Int) -> Unit
) {
    if (movies.isEmpty()) return

    val pagerState = rememberPagerState { movies.size }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .height(350.dp)
        ) { page ->
            BannerItem(movie = movies[page], onClick = onMovieClick)
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Indicator
        Row(
            Modifier
                .height(10.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {//Banner oldukça devam et
            repeat(movies.size) { iteration ->
                val color = if (pagerState.currentPage == iteration) 
                    MaterialTheme.colorScheme.primary 
                else //daha şeffaf olucak
                    MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f)
                
                Box(
                    modifier = Modifier
                        .padding(horizontal = 4.dp)
                        .clip(CircleShape)
                        .background(color)
                        .size(8.dp)
                )
            }
        }
    }
}

@Composable
fun BannerItem(movie: MovieDto, onClick: (Int) -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 8.dp)
            .clip(RoundedCornerShape(16.dp))
            .clickable { onClick(movie.id) }
    ) {
        AsyncImage(
            model = Constants.IMAGE_BASE_URL + (movie.backdropPath ?: movie.posterPath),
            contentDescription = movie.title,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        
        // Şık bir karartma gradyanı
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.8f)),
                        startY = 400f
                    )
                )
        )

        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(16.dp)
        ) {
            Surface(
                color = MaterialTheme.colorScheme.primary,
                shape = RoundedCornerShape(4.dp)
            ) {
                Text(
                    text = stringResource(R.string.movie_banner_title),
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = movie.title,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }
    }
}

@Composable
fun SectionHeader(
    title: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleLarge,
        fontWeight = FontWeight.Bold,
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp)
    )
}

@Composable
fun MovieShimmerItem() {
    Column(modifier = Modifier.padding(8.dp)) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp)
                .clip(RoundedCornerShape(12.dp))
                .shimmerEffect()
        )
        Spacer(modifier = Modifier.height(8.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth(0.7f)
                .height(20.dp)
                .shimmerEffect()
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class) //Material 3 kullandığımızı gösterir.
@Composable
fun MoviesContent(
    movies: LazyPagingItems<MovieDto>, //pagingden gelen filmler.
    topRatedMovies: LazyPagingItems<MovieDto>,
    upcomingMovies: LazyPagingItems<MovieDto>,
    bannerMovies: List<MovieDto>, //4 tane gelen movies
    favouriteMovies: List<MovieDto>,
    favouriteIds: Set<Int>,         //hangileri favoride
    onToggleFavourite: (MovieDto) -> Unit,  //favori için basınca ne olur
    onMovieClick: (Int) -> Unit,         //movie üstüne basınca ne olur
    onAiChatClick: () -> Unit
) {
    Scaffold(
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAiChatClick,
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.padding(bottom = 16.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.SmartToy,
                    contentDescription = stringResource(R.string.movie_ai_chat_desc)
                )
            }
        }
    ) { paddingValues -> //scaffold diyor ki yukarıya top bar koydum altından devam et.
        Box(  //Elemanların birbiri üstünde durmasını sağlar (lazyvertical grid + error).
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .padding(paddingValues)
        ) {
            LazyVerticalGrid( //Ana lsite yapısı 2 kolonlu bir yapıya sahip. 2 kolonu da kaplasın der.
                columns = GridCells.Adaptive(minSize = 170.dp),
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(8.dp)
            ) {
                // Hero Banner Carousel
                if (bannerMovies.isNotEmpty()) { //Baner filmi varsa corouseli göster.
                    item(span = { GridItemSpan(maxCurrentLineSpan) }) {
                        HeroBannerCarousel(movies = bannerMovies, onMovieClick = onMovieClick)
                    }
                } else if (movies.loadState.refresh is LoadState.Loading) {    // Popüler Filmler Yatay Liste

                    // Initial Shimmer for banner if still loading
                    item(span = { GridItemSpan(maxCurrentLineSpan) }) {
                        MovieShimmerItem() //oluşturulana kadar biraz karanlık loading olur.
                    }
                }

                // Popüler Filmler Yatay Liste
                item(span = { GridItemSpan(maxCurrentLineSpan) }) {
                    SectionHeader(title = stringResource(R.string.movie_section_popular))
                }
                item(span = { GridItemSpan(maxCurrentLineSpan) }) {
                    Box(modifier = Modifier.fillMaxWidth()) {
                        //Paging için bu gereklidir , ilk yüklen,rken refresh , filmeler geldi refresh = NotLoading
                        if (movies.loadState.refresh is LoadState.Loading) {
                            LazyRow(
                                contentPadding = PaddingValues(horizontal = 4.dp),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                items(5) {
                                    Box(modifier = Modifier.width(160.dp)) {
                                        MovieShimmerItem()
                                    }
                                }
                            }
                        } else if (movies.itemCount > 0) {
                            LazyRow(//eğer yüklendiyse bu sefer lazy row ekliyoruz.
                                contentPadding = PaddingValues(horizontal = 4.dp),
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                items(movies.itemCount) { index ->
                                    movies[index]?.let { movie ->
                                        MovieItem(
                                            movie = movie,
                                            isFavourite = favouriteIds.contains(movie.id),
                                            onToggleFavourite = onToggleFavourite,
                                            onItemClick = { onMovieClick(movie.id) },
                                            modifier = Modifier.width(170.dp)
                                        )
                                    }
                                }
                                
                                
                                //Listenin sonuna gelince append edilir. 2. sayfayı açar.
                                if (movies.loadState.append is LoadState.Loading) {
                                    item {
                                        Box(modifier = Modifier.width(160.dp)) {
                                            MovieShimmerItem()
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                // En Çok Oy Alan Filmler Yatay Liste
                item(span = { GridItemSpan(maxCurrentLineSpan) }) {
                    SectionHeader(title = stringResource(R.string.movie_section_top_rated))
                }
                item(span = { GridItemSpan(maxCurrentLineSpan) }) {
                    Box(modifier = Modifier.fillMaxWidth()) {
                        if (topRatedMovies.loadState.refresh is LoadState.Loading) {
                            LazyRow(
                                contentPadding = PaddingValues(horizontal = 4.dp),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                items(5) {
                                    Box(modifier = Modifier.width(160.dp)) {
                                        MovieShimmerItem()
                                    }
                                }
                            }
                        } else if (topRatedMovies.itemCount > 0) {
                            LazyRow(
                                contentPadding = PaddingValues(horizontal = 4.dp),
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                items(topRatedMovies.itemCount) { index ->
                                    topRatedMovies[index]?.let { movie ->
                                        MovieItem(
                                            movie = movie,
                                            isFavourite = favouriteIds.contains(movie.id),
                                            onToggleFavourite = onToggleFavourite,
                                            onItemClick = { onMovieClick(movie.id) },
                                            modifier = Modifier.width(170.dp)
                                        )
                                    }
                                }
                                
                                if (topRatedMovies.loadState.append is LoadState.Loading) {
                                    item {
                                        Box(modifier = Modifier.width(160.dp)) {
                                            MovieShimmerItem()
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                // Favorilerim Yatay Liste (Yeni Bölüm)
                if (favouriteMovies.isNotEmpty()) {
                    item(span = { GridItemSpan(maxCurrentLineSpan) }) {
                        SectionHeader(title = stringResource(R.string.movie_section_favorites))
                    }
                    item(span = { GridItemSpan(maxCurrentLineSpan) }) {
                        LazyRow(
                            contentPadding = PaddingValues(horizontal = 4.dp),
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            items(favouriteMovies) { movie ->
                                MovieItem(
                                    movie = movie,
                                    isFavourite = true,
                                    onToggleFavourite = onToggleFavourite,
                                    onItemClick = { onMovieClick(movie.id) },
                                    modifier = Modifier.width(170.dp)
                                )
                            }
                        }
                    }
                }

                // Yaklaşan Filmler Yatay Liste
                item(span = { GridItemSpan(maxCurrentLineSpan) }) {
                    SectionHeader(title = stringResource(R.string.movie_section_upcoming))
                }
                item(span = { GridItemSpan(maxCurrentLineSpan) }) {
                    Box(modifier = Modifier.fillMaxWidth()) {
                        if (upcomingMovies.loadState.refresh is LoadState.Loading) {
                            LazyRow(
                                contentPadding = PaddingValues(horizontal = 4.dp),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                items(5) {
                                    Box(modifier = Modifier.width(160.dp)) {
                                        MovieShimmerItem()
                                    }
                                }
                            }
                        } else if (upcomingMovies.itemCount > 0) {
                            LazyRow(
                                contentPadding = PaddingValues(horizontal = 4.dp),
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                items(upcomingMovies.itemCount) { index ->
                                    upcomingMovies[index]?.let { movie ->
                                        MovieItem(
                                            movie = movie,
                                            isFavourite = favouriteIds.contains(movie.id),
                                            onToggleFavourite = onToggleFavourite,
                                            onItemClick = { onMovieClick(movie.id) },
                                            modifier = Modifier.width(170.dp)
                                        )
                                    }
                                }

                                if (upcomingMovies.loadState.append is LoadState.Loading) {
                                    item {
                                        Box(modifier = Modifier.width(160.dp)) {
                                            MovieShimmerItem()
                                        }
                                    }
                                }
                            }
                        }
                    }
                }


            }

            if (movies.loadState.refresh is LoadState.Error) {
                val error = (movies.loadState.refresh as LoadState.Error).error
                Column(
                    modifier = Modifier.align(Alignment.Center).padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = stringResource(R.string.movie_error_prefix) + error.localizedMessage, color = MaterialTheme.colorScheme.error)
                    Spacer(Modifier.height(8.dp))
                    Button(onClick = { movies.retry() }) {
                        Text(stringResource(R.string.movie_retry_button))
                    }
                }
            }
        }
    }
}

@Preview(showBackground = false)
@Composable
fun MoviesScreenPreview() {
    val mockMovies = listOf(
        MovieDto(id = 1, title = "Film 1", posterPath = null, overview = "Özet 1", releaseDate = "2024", voteAverage = 8.0),
        MovieDto(id = 2, title = "Film 2", posterPath = null, overview = "Özet 2", releaseDate = "2024", voteAverage = 7.5),
        MovieDto(id = 3, title = "Film 3", posterPath = null, overview = "Özet 3", releaseDate = "2024", voteAverage = 9.0)
    )
    val pagingData = PagingData.from(mockMovies)
    val moviesFlow = flowOf(pagingData)
    val moviesItems = moviesFlow.collectAsLazyPagingItems()

    MoviesContent(
        movies = moviesItems,
        topRatedMovies = moviesItems,
        upcomingMovies = moviesItems,
        bannerMovies = mockMovies,
        favouriteMovies = mockMovies.take(2),
        favouriteIds = setOf(1, 2),
        onToggleFavourite = {},
        onMovieClick = {},
        onAiChatClick = {}
    )
}
@Preview(showBackground = false)
@Composable
fun HeroBannerCarouselPreview() {
    val mockMovies = listOf(
        MovieDto(id = 1, title = "Spider-Man", posterPath = null, overview = "Özet 1", releaseDate = "2024", voteAverage = 8.0),
        MovieDto(id = 2, title = "The Odyssey", posterPath = null, overview = "Özet 2", releaseDate = "2024", voteAverage = 7.5),
        MovieDto(id = 3, title = "Inception", posterPath = null, overview = "Özet 3", releaseDate = "2024", voteAverage = 9.0),
        MovieDto(id = 1, title = "Spider-Man", posterPath = null, overview = "Özet 1", releaseDate = "2024", voteAverage = 8.0)
    )
    HeroBannerCarousel(movies = mockMovies, onMovieClick = {})
}
