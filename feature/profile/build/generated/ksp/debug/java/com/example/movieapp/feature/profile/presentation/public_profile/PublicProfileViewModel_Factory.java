package com.example.movieapp.feature.profile.presentation.public_profile;

import com.example.movieapp.domain.repository.SocialRepository;
import com.example.movieapp.feature.profile.domain.usecase.GetPublicProfileUseCase;
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
public final class PublicProfileViewModel_Factory implements Factory<PublicProfileViewModel> {
  private final Provider<GetPublicProfileUseCase> getPublicProfileUseCaseProvider;

  private final Provider<SocialRepository> socialRepositoryProvider;

  private PublicProfileViewModel_Factory(
      Provider<GetPublicProfileUseCase> getPublicProfileUseCaseProvider,
      Provider<SocialRepository> socialRepositoryProvider) {
    this.getPublicProfileUseCaseProvider = getPublicProfileUseCaseProvider;
    this.socialRepositoryProvider = socialRepositoryProvider;
  }

  @Override
  public PublicProfileViewModel get() {
    return newInstance(getPublicProfileUseCaseProvider.get(), socialRepositoryProvider.get());
  }

  public static PublicProfileViewModel_Factory create(
      Provider<GetPublicProfileUseCase> getPublicProfileUseCaseProvider,
      Provider<SocialRepository> socialRepositoryProvider) {
    return new PublicProfileViewModel_Factory(getPublicProfileUseCaseProvider, socialRepositoryProvider);
  }

  public static PublicProfileViewModel newInstance(GetPublicProfileUseCase getPublicProfileUseCase,
      SocialRepository socialRepository) {
    return new PublicProfileViewModel(getPublicProfileUseCase, socialRepository);
  }
}
