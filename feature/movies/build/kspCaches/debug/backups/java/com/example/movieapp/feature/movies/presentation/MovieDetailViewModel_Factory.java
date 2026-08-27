package com.example.movieapp.feature.movies.presentation;

import android.app.Application;
import com.example.movieapp.domain.repository.AuthRepository;
import com.example.movieapp.domain.repository.FavouriteRepository;
import com.example.movieapp.domain.repository.WatchedRepository;
import com.example.movieapp.feature.movies.domain.usecase.AddMovieToListUseCase;
import com.example.movieapp.feature.movies.domain.usecase.GetMovieDetailsUseCase;
import com.example.movieapp.feature.movies.domain.usecase.GetMovieTrailerUseCase;
import com.example.movieapp.feature.movies.domain.usecase.GetSimilarMoviesUseCase;
import com.example.movieapp.feature.movies.domain.usecase.GetWatchProvidersUseCase;
import com.example.movieapp.feature.movies.domain.usecase.ToggleFavoriteUseCase;
import com.example.movieapp.feature.profile.domain.usecase.AddRecentMovieUseCase;
import com.example.movieapp.feature.rating.domain.repository.RatingRepository;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Provider;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;

@ScopeMetadata
@QualifierMetadata
@DaggerGenerated
@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes",
    "KotlinInternal",
    "KotlinInternalInJava",
    "cast",
    "deprecation",
    "nullness:initialization.field.uninitialized"
})
public final class MovieDetailViewModel_Factory implements Factory<MovieDetailViewModel> {
  private final Provider<GetMovieDetailsUseCase> getMovieDetailsUseCaseProvider;

  private final Provider<GetMovieTrailerUseCase> getMovieTrailerUseCaseProvider;

  private final Provider<GetSimilarMoviesUseCase> getSimilarMoviesUseCaseProvider;

  private final Provider<GetWatchProvidersUseCase> getWatchProvidersUseCaseProvider;

  private final Provider<ToggleFavoriteUseCase> toggleFavoriteUseCaseProvider;

  private final Provider<AddMovieToListUseCase> addMovieToListUseCaseProvider;

  private final Provider<AddRecentMovieUseCase> addRecentMovieUseCaseProvider;

  private final Provider<FavouriteRepository> favouriteRepositoryProvider;

  private final Provider<RatingRepository> ratingRepositoryProvider;

  private final Provider<WatchedRepository> watchedRepositoryProvider;

  private final Provider<AuthRepository> authRepositoryProvider;

  private final Provider<Application> applicationProvider;

  private MovieDetailViewModel_Factory(
      Provider<GetMovieDetailsUseCase> getMovieDetailsUseCaseProvider,
      Provider<GetMovieTrailerUseCase> getMovieTrailerUseCaseProvider,
      Provider<GetSimilarMoviesUseCase> getSimilarMoviesUseCaseProvider,
      Provider<GetWatchProvidersUseCase> getWatchProvidersUseCaseProvider,
      Provider<ToggleFavoriteUseCase> toggleFavoriteUseCaseProvider,
      Provider<AddMovieToListUseCase> addMovieToListUseCaseProvider,
      Provider<AddRecentMovieUseCase> addRecentMovieUseCaseProvider,
      Provider<FavouriteRepository> favouriteRepositoryProvider,
      Provider<RatingRepository> ratingRepositoryProvider,
      Provider<WatchedRepository> watchedRepositoryProvider,
      Provider<AuthRepository> authRepositoryProvider, Provider<Application> applicationProvider) {
    this.getMovieDetailsUseCaseProvider = getMovieDetailsUseCaseProvider;
    this.getMovieTrailerUseCaseProvider = getMovieTrailerUseCaseProvider;
    this.getSimilarMoviesUseCaseProvider = getSimilarMoviesUseCaseProvider;
    this.getWatchProvidersUseCaseProvider = getWatchProvidersUseCaseProvider;
    this.toggleFavoriteUseCaseProvider = toggleFavoriteUseCaseProvider;
    this.addMovieToListUseCaseProvider = addMovieToListUseCaseProvider;
    this.addRecentMovieUseCaseProvider = addRecentMovieUseCaseProvider;
    this.favouriteRepositoryProvider = favouriteRepositoryProvider;
    this.ratingRepositoryProvider = ratingRepositoryProvider;
    this.watchedRepositoryProvider = watchedRepositoryProvider;
    this.authRepositoryProvider = authRepositoryProvider;
    this.applicationProvider = applicationProvider;
  }

  @Override
  public MovieDetailViewModel get() {
    return newInstance(getMovieDetailsUseCaseProvider.get(), getMovieTrailerUseCaseProvider.get(), getSimilarMoviesUseCaseProvider.get(), getWatchProvidersUseCaseProvider.get(), toggleFavoriteUseCaseProvider.get(), addMovieToListUseCaseProvider.get(), addRecentMovieUseCaseProvider.get(), favouriteRepositoryProvider.get(), ratingRepositoryProvider.get(), watchedRepositoryProvider.get(), authRepositoryProvider.get(), applicationProvider.get());
  }

  public static MovieDetailViewModel_Factory create(
      Provider<GetMovieDetailsUseCase> getMovieDetailsUseCaseProvider,
      Provider<GetMovieTrailerUseCase> getMovieTrailerUseCaseProvider,
      Provider<GetSimilarMoviesUseCase> getSimilarMoviesUseCaseProvider,
      Provider<GetWatchProvidersUseCase> getWatchProvidersUseCaseProvider,
      Provider<ToggleFavoriteUseCase> toggleFavoriteUseCaseProvider,
      Provider<AddMovieToListUseCase> addMovieToListUseCaseProvider,
      Provider<AddRecentMovieUseCase> addRecentMovieUseCaseProvider,
      Provider<FavouriteRepository> favouriteRepositoryProvider,
      Provider<RatingRepository> ratingRepositoryProvider,
      Provider<WatchedRepository> watchedRepositoryProvider,
      Provider<AuthRepository> authRepositoryProvider, Provider<Application> applicationProvider) {
    return new MovieDetailViewModel_Factory(getMovieDetailsUseCaseProvider, getMovieTrailerUseCaseProvider, getSimilarMoviesUseCaseProvider, getWatchProvidersUseCaseProvider, toggleFavoriteUseCaseProvider, addMovieToListUseCaseProvider, addRecentMovieUseCaseProvider, favouriteRepositoryProvider, ratingRepositoryProvider, watchedRepositoryProvider, authRepositoryProvider, applicationProvider);
  }

  public static MovieDetailViewModel newInstance(GetMovieDetailsUseCase getMovieDetailsUseCase,
      GetMovieTrailerUseCase getMovieTrailerUseCase,
      GetSimilarMoviesUseCase getSimilarMoviesUseCase,
      GetWatchProvidersUseCase getWatchProvidersUseCase,
      ToggleFavoriteUseCase toggleFavoriteUseCase, AddMovieToListUseCase addMovieToListUseCase,
      AddRecentMovieUseCase addRecentMovieUseCase, FavouriteRepository favouriteRepository,
      RatingRepository ratingRepository, WatchedRepository watchedRepository,
      AuthRepository authRepository, Application application) {
    return new MovieDetailViewModel(getMovieDetailsUseCase, getMovieTrailerUseCase, getSimilarMoviesUseCase, getWatchProvidersUseCase, toggleFavoriteUseCase, addMovieToListUseCase, addRecentMovieUseCase, favouriteRepository, ratingRepository, watchedRepository, authRepository, application);
  }
}
