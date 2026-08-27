package com.example.movieapp.feature.profile.domain.usecase;

import com.example.movieapp.domain.repository.AuthRepository;
import com.example.movieapp.feature.profile.domain.repository.ProfileRepository;
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
public final class AddRecentMovieUseCase_Factory implements Factory<AddRecentMovieUseCase> {
  private final Provider<AuthRepository> authRepositoryProvider;

  private final Provider<ProfileRepository> profileRepositoryProvider;

  private AddRecentMovieUseCase_Factory(Provider<AuthRepository> authRepositoryProvider,
      Provider<ProfileRepository> profileRepositoryProvider) {
    this.authRepositoryProvider = authRepositoryProvider;
    this.profileRepositoryProvider = profileRepositoryProvider;
  }

  @Override
  public AddRecentMovieUseCase get() {
    return newInstance(authRepositoryProvider.get(), profileRepositoryProvider.get());
  }

  public static AddRecentMovieUseCase_Factory create(
      Provider<AuthRepository> authRepositoryProvider,
      Provider<ProfileRepository> profileRepositoryProvider) {
    return new AddRecentMovieUseCase_Factory(authRepositoryProvider, profileRepositoryProvider);
  }

  public static AddRecentMovieUseCase newInstance(AuthRepository authRepository,
      ProfileRepository profileRepository) {
    return new AddRecentMovieUseCase(authRepository, profileRepository);
  }
}
