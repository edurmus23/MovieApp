package com.example.movieapp.feature.movies.di;

import com.example.movieapp.domain.repository.MoviesRepository;
import com.example.movieapp.feature.movies.data.remote.MoviesApiService;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.Provider;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;

@ScopeMetadata("javax.inject.Singleton")
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
public final class MoviesDataModule_ProvideMoviesRepositoryFactory implements Factory<MoviesRepository> {
  private final Provider<MoviesApiService> apiServiceProvider;

  private MoviesDataModule_ProvideMoviesRepositoryFactory(
      Provider<MoviesApiService> apiServiceProvider) {
    this.apiServiceProvider = apiServiceProvider;
  }

  @Override
  public MoviesRepository get() {
    return provideMoviesRepository(apiServiceProvider.get());
  }

  public static MoviesDataModule_ProvideMoviesRepositoryFactory create(
      Provider<MoviesApiService> apiServiceProvider) {
    return new MoviesDataModule_ProvideMoviesRepositoryFactory(apiServiceProvider);
  }

  public static MoviesRepository provideMoviesRepository(MoviesApiService apiService) {
    return Preconditions.checkNotNullFromProvides(MoviesDataModule.INSTANCE.provideMoviesRepository(apiService));
  }
}
