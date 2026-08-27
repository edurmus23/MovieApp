package com.example.movieapp.feature.social.presentation.social;

import com.example.movieapp.domain.repository.SocialRepository;
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
public final class SocialViewModel_Factory implements Factory<SocialViewModel> {
  private final Provider<SocialRepository> socialRepositoryProvider;

  private SocialViewModel_Factory(Provider<SocialRepository> socialRepositoryProvider) {
    this.socialRepositoryProvider = socialRepositoryProvider;
  }

  @Override
  public SocialViewModel get() {
    return newInstance(socialRepositoryProvider.get());
  }

  public static SocialViewModel_Factory create(
      Provider<SocialRepository> socialRepositoryProvider) {
    return new SocialViewModel_Factory(socialRepositoryProvider);
  }

  public static SocialViewModel newInstance(SocialRepository socialRepository) {
    return new SocialViewModel(socialRepository);
  }
}
