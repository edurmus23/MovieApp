package com.example.movieapp.feature.watched.presentation;

import com.example.movieapp.domain.repository.WatchedRepository;
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
public final class WatchedViewModel_Factory implements Factory<WatchedViewModel> {
  private final Provider<WatchedRepository> watchedRepositoryProvider;

  private WatchedViewModel_Factory(Provider<WatchedRepository> watchedRepositoryProvider) {
    this.watchedRepositoryProvider = watchedRepositoryProvider;
  }

  @Override
  public WatchedViewModel get() {
    return newInstance(watchedRepositoryProvider.get());
  }

  public static WatchedViewModel_Factory create(
      Provider<WatchedRepository> watchedRepositoryProvider) {
    return new WatchedViewModel_Factory(watchedRepositoryProvider);
  }

  public static WatchedViewModel newInstance(WatchedRepository watchedRepository) {
    return new WatchedViewModel(watchedRepository);
  }
}
