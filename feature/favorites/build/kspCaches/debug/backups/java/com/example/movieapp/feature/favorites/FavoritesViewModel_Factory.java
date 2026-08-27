package com.example.movieapp.feature.favorites;

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
public final class FavoritesViewModel_Factory implements Factory<FavoritesViewModel> {
  private final Provider<FavouriteRepository> favouriteRepositoryProvider;

  private FavoritesViewModel_Factory(Provider<FavouriteRepository> favouriteRepositoryProvider) {
    this.favouriteRepositoryProvider = favouriteRepositoryProvider;
  }

  @Override
  public FavoritesViewModel get() {
    return newInstance(favouriteRepositoryProvider.get());
  }

  public static FavoritesViewModel_Factory create(
      Provider<FavouriteRepository> favouriteRepositoryProvider) {
    return new FavoritesViewModel_Factory(favouriteRepositoryProvider);
  }

  public static FavoritesViewModel newInstance(FavouriteRepository favouriteRepository) {
    return new FavoritesViewModel(favouriteRepository);
  }
}
