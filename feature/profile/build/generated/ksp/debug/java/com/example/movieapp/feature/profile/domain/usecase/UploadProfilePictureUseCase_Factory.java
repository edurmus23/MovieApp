package com.example.movieapp.feature.profile.domain.usecase;

import com.example.movieapp.domain.repository.AuthRepository;
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
public final class UploadProfilePictureUseCase_Factory implements Factory<UploadProfilePictureUseCase> {
  private final Provider<AuthRepository> repositoryProvider;

  private UploadProfilePictureUseCase_Factory(Provider<AuthRepository> repositoryProvider) {
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public UploadProfilePictureUseCase get() {
    return newInstance(repositoryProvider.get());
  }

  public static UploadProfilePictureUseCase_Factory create(
      Provider<AuthRepository> repositoryProvider) {
    return new UploadProfilePictureUseCase_Factory(repositoryProvider);
  }

  public static UploadProfilePictureUseCase newInstance(AuthRepository repository) {
    return new UploadProfilePictureUseCase(repository);
  }
}
