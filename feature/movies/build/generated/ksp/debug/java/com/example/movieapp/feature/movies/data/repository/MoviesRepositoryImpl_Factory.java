package com.example.movieapp.feature.movies.data.repository;

import com.example.movieapp.feature.movies.data.remote.MoviesApiService;
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
public final class MoviesRepositoryImpl_Factory implements Factory<MoviesRepositoryImpl> {
  private final Provider<MoviesApiService> apiServiceProvider;

  private MoviesRepositoryImpl_Factory(Provider<MoviesApiService> apiServiceProvider) {
    this.apiServiceProvider = apiServiceProvider;
  }

  @Override
  public MoviesRepositoryImpl get() {
    return newInstance(apiServiceProvider.get());
  }

  public static MoviesRepositoryImpl_Factory create(Provider<MoviesApiService> apiServiceProvider) {
    return new MoviesRepositoryImpl_Factory(apiServiceProvider);
  }

  public static MoviesRepositoryImpl newInstance(MoviesApiService apiService) {
    return new MoviesRepositoryImpl(apiService);
  }
}
