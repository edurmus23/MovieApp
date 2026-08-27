package com.example.movieapp.feature.rating.presentation;

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
public final class RatedMoviesViewModel_Factory implements Factory<RatedMoviesViewModel> {
  private final Provider<RatingRepository> ratingRepositoryProvider;

  private RatedMoviesViewModel_Factory(Provider<RatingRepository> ratingRepositoryProvider) {
    this.ratingRepositoryProvider = ratingRepositoryProvider;
  }

  @Override
  public RatedMoviesViewModel get() {
    return newInstance(ratingRepositoryProvider.get());
  }

  public static RatedMoviesViewModel_Factory create(
      Provider<RatingRepository> ratingRepositoryProvider) {
    return new RatedMoviesViewModel_Factory(ratingRepositoryProvider);
  }

  public static RatedMoviesViewModel newInstance(RatingRepository ratingRepository) {
    return new RatedMoviesViewModel(ratingRepository);
  }
}
