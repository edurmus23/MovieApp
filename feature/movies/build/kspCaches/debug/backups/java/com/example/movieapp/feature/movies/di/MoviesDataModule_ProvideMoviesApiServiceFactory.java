package com.example.movieapp.feature.movies.di;

import com.example.movieapp.feature.movies.data.remote.MoviesApiService;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.Provider;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import retrofit2.Retrofit;

@ScopeMetadata("javax.inject.Singleton")
@QualifierMetadata("com.example.movieapp.network.di.TmdbRetrofit")
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
public final class MoviesDataModule_ProvideMoviesApiServiceFactory implements Factory<MoviesApiService> {
  private final Provider<Retrofit> retrofitProvider;

  private MoviesDataModule_ProvideMoviesApiServiceFactory(Provider<Retrofit> retrofitProvider) {
    this.retrofitProvider = retrofitProvider;
  }

  @Override
  public MoviesApiService get() {
    return provideMoviesApiService(retrofitProvider.get());
  }

  public static MoviesDataModule_ProvideMoviesApiServiceFactory create(
      Provider<Retrofit> retrofitProvider) {
    return new MoviesDataModule_ProvideMoviesApiServiceFactory(retrofitProvider);
  }

  public static MoviesApiService provideMoviesApiService(Retrofit retrofit) {
    return Preconditions.checkNotNullFromProvides(MoviesDataModule.INSTANCE.provideMoviesApiService(retrofit));
  }
}
