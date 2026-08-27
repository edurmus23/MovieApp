package com.example.movieapp.feature.movies.presentation;

import android.app.Application;
import com.example.movieapp.domain.repository.AuthRepository;
import com.example.movieapp.domain.repository.FavouriteRepository;
import com.example.movieapp.feature.movies.domain.usecase.GetBannerMoviesUseCase;
import com.example.movieapp.feature.movies.domain.usecase.GetPopularMoviesUseCase;
import com.example.movieapp.feature.movies.domain.usecase.GetTopRatedMoviesUseCase;
import com.example.movieapp.feature.movies.domain.usecase.GetUpcomingMoviesUseCase;
import com.example.movieapp.feature.movies.domain.usecase.ToggleFavoriteUseCase;
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
public final class MovieViewModel_Factory implements Factory<MovieViewModel> {
  private final Provider<GetPopularMoviesUseCase> getPopularMoviesUseCaseProvider;

  private final Provider<GetTopRatedMoviesUseCase> getTopRatedMoviesUseCaseProvider;

  private final Provider<GetUpcomingMoviesUseCase> getUpcomingMoviesUseCaseProvider;

  private final Provider<GetBannerMoviesUseCase> getBannerMoviesUseCaseProvider;

  private final Provider<ToggleFavoriteUseCase> toggleFavoriteUseCaseProvider;

  private final Provider<FavouriteRepository> favouriteRepositoryProvider;

  private final Provider<AuthRepository> authRepositoryProvider;

  private final Provider<Application> applicationProvider;

  private MovieViewModel_Factory(Provider<GetPopularMoviesUseCase> getPopularMoviesUseCaseProvider,
      Provider<GetTopRatedMoviesUseCase> getTopRatedMoviesUseCaseProvider,
      Provider<GetUpcomingMoviesUseCase> getUpcomingMoviesUseCaseProvider,
      Provider<GetBannerMoviesUseCase> getBannerMoviesUseCaseProvider,
      Provider<ToggleFavoriteUseCase> toggleFavoriteUseCaseProvider,
      Provider<FavouriteRepository> favouriteRepositoryProvider,
      Provider<AuthRepository> authRepositoryProvider, Provider<Application> applicationProvider) {
    this.getPopularMoviesUseCaseProvider = getPopularMoviesUseCaseProvider;
    this.getTopRatedMoviesUseCaseProvider = getTopRatedMoviesUseCaseProvider;
    this.getUpcomingMoviesUseCaseProvider = getUpcomingMoviesUseCaseProvider;
    this.getBannerMoviesUseCaseProvider = getBannerMoviesUseCaseProvider;
    this.toggleFavoriteUseCaseProvider = toggleFavoriteUseCaseProvider;
    this.favouriteRepositoryProvider = favouriteRepositoryProvider;
    this.authRepositoryProvider = authRepositoryProvider;
    this.applicationProvider = applicationProvider;
  }

  @Override
  public MovieViewModel get() {
    return newInstance(getPopularMoviesUseCaseProvider.get(), getTopRatedMoviesUseCaseProvider.get(), getUpcomingMoviesUseCaseProvider.get(), getBannerMoviesUseCaseProvider.get(), toggleFavoriteUseCaseProvider.get(), favouriteRepositoryProvider.get(), authRepositoryProvider.get(), applicationProvider.get());
  }

  public static MovieViewModel_Factory create(
      Provider<GetPopularMoviesUseCase> getPopularMoviesUseCaseProvider,
      Provider<GetTopRatedMoviesUseCase> getTopRatedMoviesUseCaseProvider,
      Provider<GetUpcomingMoviesUseCase> getUpcomingMoviesUseCaseProvider,
      Provider<GetBannerMoviesUseCase> getBannerMoviesUseCaseProvider,
      Provider<ToggleFavoriteUseCase> toggleFavoriteUseCaseProvider,
      Provider<FavouriteRepository> favouriteRepositoryProvider,
      Provider<AuthRepository> authRepositoryProvider, Provider<Application> applicationProvider) {
    return new MovieViewModel_Factory(getPopularMoviesUseCaseProvider, getTopRatedMoviesUseCaseProvider, getUpcomingMoviesUseCaseProvider, getBannerMoviesUseCaseProvider, toggleFavoriteUseCaseProvider, favouriteRepositoryProvider, authRepositoryProvider, applicationProvider);
  }

  public static MovieViewModel newInstance(GetPopularMoviesUseCase getPopularMoviesUseCase,
      GetTopRatedMoviesUseCase getTopRatedMoviesUseCase,
      GetUpcomingMoviesUseCase getUpcomingMoviesUseCase,
      GetBannerMoviesUseCase getBannerMoviesUseCase, ToggleFavoriteUseCase toggleFavoriteUseCase,
      FavouriteRepository favouriteRepository, AuthRepository authRepository,
      Application application) {
    return new MovieViewModel(getPopularMoviesUseCase, getTopRatedMoviesUseCase, getUpcomingMoviesUseCase, getBannerMoviesUseCase, toggleFavoriteUseCase, favouriteRepository, authRepository, application);
  }
}
