package com.example.movieapp.feature.profile.presentation;

import android.app.Application;
import com.example.movieapp.domain.repository.AuthRepository;
import com.example.movieapp.domain.repository.SocialRepository;
import com.example.movieapp.feature.profile.domain.usecase.GetProfileUseCase;
import com.example.movieapp.feature.profile.domain.usecase.LogoutUseCase;
import com.example.movieapp.feature.profile.domain.usecase.UpdateProfileUseCase;
import com.example.movieapp.feature.profile.domain.usecase.UploadProfilePictureUseCase;
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
public final class ProfileViewModel_Factory implements Factory<ProfileViewModel> {
  private final Provider<GetProfileUseCase> getProfileUseCaseProvider;

  private final Provider<LogoutUseCase> logoutUseCaseProvider;

  private final Provider<UploadProfilePictureUseCase> uploadProfilePictureUseCaseProvider;

  private final Provider<UpdateProfileUseCase> updateProfileUseCaseProvider;

  private final Provider<AuthRepository> authRepositoryProvider;

  private final Provider<SocialRepository> socialRepositoryProvider;

  private final Provider<Application> applicationProvider;

  private ProfileViewModel_Factory(Provider<GetProfileUseCase> getProfileUseCaseProvider,
      Provider<LogoutUseCase> logoutUseCaseProvider,
      Provider<UploadProfilePictureUseCase> uploadProfilePictureUseCaseProvider,
      Provider<UpdateProfileUseCase> updateProfileUseCaseProvider,
      Provider<AuthRepository> authRepositoryProvider,
      Provider<SocialRepository> socialRepositoryProvider,
      Provider<Application> applicationProvider) {
    this.getProfileUseCaseProvider = getProfileUseCaseProvider;
    this.logoutUseCaseProvider = logoutUseCaseProvider;
    this.uploadProfilePictureUseCaseProvider = uploadProfilePictureUseCaseProvider;
    this.updateProfileUseCaseProvider = updateProfileUseCaseProvider;
    this.authRepositoryProvider = authRepositoryProvider;
    this.socialRepositoryProvider = socialRepositoryProvider;
    this.applicationProvider = applicationProvider;
  }

  @Override
  public ProfileViewModel get() {
    return newInstance(getProfileUseCaseProvider.get(), logoutUseCaseProvider.get(), uploadProfilePictureUseCaseProvider.get(), updateProfileUseCaseProvider.get(), authRepositoryProvider.get(), socialRepositoryProvider.get(), applicationProvider.get());
  }

  public static ProfileViewModel_Factory create(
      Provider<GetProfileUseCase> getProfileUseCaseProvider,
      Provider<LogoutUseCase> logoutUseCaseProvider,
      Provider<UploadProfilePictureUseCase> uploadProfilePictureUseCaseProvider,
      Provider<UpdateProfileUseCase> updateProfileUseCaseProvider,
      Provider<AuthRepository> authRepositoryProvider,
      Provider<SocialRepository> socialRepositoryProvider,
      Provider<Application> applicationProvider) {
    return new ProfileViewModel_Factory(getProfileUseCaseProvider, logoutUseCaseProvider, uploadProfilePictureUseCaseProvider, updateProfileUseCaseProvider, authRepositoryProvider, socialRepositoryProvider, applicationProvider);
  }

  public static ProfileViewModel newInstance(GetProfileUseCase getProfileUseCase,
      LogoutUseCase logoutUseCase, UploadProfilePictureUseCase uploadProfilePictureUseCase,
      UpdateProfileUseCase updateProfileUseCase, AuthRepository authRepository,
      SocialRepository socialRepository, Application application) {
    return new ProfileViewModel(getProfileUseCase, logoutUseCase, uploadProfilePictureUseCase, updateProfileUseCase, authRepository, socialRepository, application);
  }
}
