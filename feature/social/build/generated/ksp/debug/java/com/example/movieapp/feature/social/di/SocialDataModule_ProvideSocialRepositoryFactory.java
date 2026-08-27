package com.example.movieapp.feature.social.di;

import com.example.movieapp.domain.repository.AuthRepository;
import com.example.movieapp.domain.repository.SocialRepository;
import com.google.firebase.firestore.FirebaseFirestore;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.Provider;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;

@ScopeMetadata("javax.inject.Singleton")
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
public final class SocialDataModule_ProvideSocialRepositoryFactory implements Factory<SocialRepository> {
  private final Provider<FirebaseFirestore> firestoreProvider;

  private final Provider<AuthRepository> authRepositoryProvider;

  private SocialDataModule_ProvideSocialRepositoryFactory(
      Provider<FirebaseFirestore> firestoreProvider,
      Provider<AuthRepository> authRepositoryProvider) {
    this.firestoreProvider = firestoreProvider;
    this.authRepositoryProvider = authRepositoryProvider;
  }

  @Override
  public SocialRepository get() {
    return provideSocialRepository(firestoreProvider.get(), authRepositoryProvider.get());
  }

  public static SocialDataModule_ProvideSocialRepositoryFactory create(
      Provider<FirebaseFirestore> firestoreProvider,
      Provider<AuthRepository> authRepositoryProvider) {
    return new SocialDataModule_ProvideSocialRepositoryFactory(firestoreProvider, authRepositoryProvider);
  }

  public static SocialRepository provideSocialRepository(FirebaseFirestore firestore,
      AuthRepository authRepository) {
    return Preconditions.checkNotNullFromProvides(SocialDataModule.INSTANCE.provideSocialRepository(firestore, authRepository));
  }
}
