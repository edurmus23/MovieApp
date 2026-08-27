package com.example.movieapp.feature.movies.domain.usecase;

import com.example.movieapp.domain.repository.MoviesRepository;
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
public final class GetMovieDetailsUseCase_Factory implements Factory<GetMovieDetailsUseCase> {
  private final Provider<MoviesRepository> repositoryProvider;

  private GetMovieDetailsUseCase_Factory(Provider<MoviesRepository> repositoryProvider) {
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public GetMovieDetailsUseCase get() {
    return newInstance(repositoryProvider.get());
  }

  public static GetMovieDetailsUseCase_Factory create(
      Provider<MoviesRepository> repositoryProvider) {
    return new GetMovieDetailsUseCase_Factory(repositoryProvider);
  }

  public static GetMovieDetailsUseCase newInstance(MoviesRepository repository) {
    return new GetMovieDetailsUseCase(repository);
  }
}
