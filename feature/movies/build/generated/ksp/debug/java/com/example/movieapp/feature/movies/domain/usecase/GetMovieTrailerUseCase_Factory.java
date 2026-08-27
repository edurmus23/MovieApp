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
public final class GetMovieTrailerUseCase_Factory implements Factory<GetMovieTrailerUseCase> {
  private final Provider<MoviesRepository> repositoryProvider;

  private GetMovieTrailerUseCase_Factory(Provider<MoviesRepository> repositoryProvider) {
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public GetMovieTrailerUseCase get() {
    return newInstance(repositoryProvider.get());
  }

  public static GetMovieTrailerUseCase_Factory create(
      Provider<MoviesRepository> repositoryProvider) {
    return new GetMovieTrailerUseCase_Factory(repositoryProvider);
  }

  public static GetMovieTrailerUseCase newInstance(MoviesRepository repository) {
    return new GetMovieTrailerUseCase(repository);
  }
}
