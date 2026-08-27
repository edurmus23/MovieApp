package com.example.movieapp.feature.profile.presentation;

import com.example.movieapp.domain.repository.AuthRepository;
import com.example.movieapp.feature.profile.domain.usecase.UpdatePasswordUseCase;
import com.example.movieapp.feature.profile.domain.usecase.UpdateProfileUseCase;
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
public final class SettingsViewModel_Factory implements Factory<SettingsViewModel> {
  private final Provider<UpdateProfileUseCase> updateProfileUseCaseProvider;

  private final Provider<UpdatePasswordUseCase> updatePasswordUseCaseProvider;

  private final Provider<AuthRepository> authRepositoryProvider;

  private SettingsViewModel_Factory(Provider<UpdateProfileUseCase> updateProfileUseCaseProvider,
      Provider<UpdatePasswordUseCase> updatePasswordUseCaseProvider,
      Provider<AuthRepository> authRepositoryProvider) {
    this.updateProfileUseCaseProvider = updateProfileUseCaseProvider;
    this.updatePasswordUseCaseProvider = updatePasswordUseCaseProvider;
    this.authRepositoryProvider = authRepositoryProvider;
  }

  @Override
  public SettingsViewModel get() {
    return newInstance(updateProfileUseCaseProvider.get(), updatePasswordUseCaseProvider.get(), authRepositoryProvider.get());
  }

  public static SettingsViewModel_Factory create(
      Provider<UpdateProfileUseCase> updateProfileUseCaseProvider,
      Provider<UpdatePasswordUseCase> updatePasswordUseCaseProvider,
      Provider<AuthRepository> authRepositoryProvider) {
    return new SettingsViewModel_Factory(updateProfileUseCaseProvider, updatePasswordUseCaseProvider, authRepositoryProvider);
  }

  public static SettingsViewModel newInstance(UpdateProfileUseCase updateProfileUseCase,
      UpdatePasswordUseCase updatePasswordUseCase, AuthRepository authRepository) {
    return new SettingsViewModel(updateProfileUseCase, updatePasswordUseCase, authRepository);
  }
}
