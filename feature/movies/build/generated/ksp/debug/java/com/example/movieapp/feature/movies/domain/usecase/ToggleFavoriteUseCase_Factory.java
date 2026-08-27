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
public final class ToggleFavoriteUseCase_Factory implements Factory<ToggleFavoriteUseCase> {
  private final Provider<FavouriteRepository> favouriteRepositoryProvider;

  private ToggleFavoriteUseCase_Factory(Provider<FavouriteRepository> favouriteRepositoryProvider) {
    this.favouriteRepositoryProvider = favouriteRepositoryProvider;
  }

  @Override
  public ToggleFavoriteUseCase get() {
    return newInstance(favouriteRepositoryProvider.get());
  }

  public static ToggleFavoriteUseCase_Factory create(
      Provider<FavouriteRepository> favouriteRepositoryProvider) {
    return new ToggleFavoriteUseCase_Factory(favouriteRepositoryProvider);
  }

  public static ToggleFavoriteUseCase newInstance(FavouriteRepository favouriteRepository) {
    return new ToggleFavoriteUseCase(favouriteRepository);
  }
}
