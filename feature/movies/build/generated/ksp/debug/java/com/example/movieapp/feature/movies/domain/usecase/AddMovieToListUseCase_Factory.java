package com.example.movieapp.feature.movies.domain.usecase;

import com.example.movieapp.domain.repository.FavouriteRepository;
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
public final class AddMovieToListUseCase_Factory implements Factory<AddMovieToListUseCase> {
  private final Provider<FavouriteRepository> favouriteRepositoryProvider;

  private AddMovieToListUseCase_Factory(Provider<FavouriteRepository> favouriteRepositoryProvider) {
    this.favouriteRepositoryProvider = favouriteRepositoryProvider;
  }

  @Override
  public AddMovieToListUseCase get() {
    return newInstance(favouriteRepositoryProvider.get());
  }

  public static AddMovieToListUseCase_Factory create(
      Provider<FavouriteRepository> favouriteRepositoryProvider) {
    return new AddMovieToListUseCase_Factory(favouriteRepositoryProvider);
  }

  public static AddMovieToListUseCase newInstance(FavouriteRepository favouriteRepository) {
    return new AddMovieToListUseCase(favouriteRepository);
  }
}
