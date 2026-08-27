package com.example.movieapp.feature.profile.domain.usecase;

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
public final class GetPublicProfileUseCase_Factory implements Factory<GetPublicProfileUseCase> {
  private final Provider<ProfileRepository> repositoryProvider;

  private GetPublicProfileUseCase_Factory(Provider<ProfileRepository> repositoryProvider) {
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public GetPublicProfileUseCase get() {
    return newInstance(repositoryProvider.get());
  }

  public static GetPublicProfileUseCase_Factory create(
      Provider<ProfileRepository> repositoryProvider) {
    return new GetPublicProfileUseCase_Factory(repositoryProvider);
  }

  public static GetPublicProfileUseCase newInstance(ProfileRepository repository) {
    return new GetPublicProfileUseCase(repository);
  }
}
