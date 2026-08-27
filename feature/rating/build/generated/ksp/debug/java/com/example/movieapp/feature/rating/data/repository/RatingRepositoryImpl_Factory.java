package com.example.movieapp.feature.rating.data.repository;

import com.example.movieapp.domain.repository.AuthRepository;
import com.google.firebase.firestore.FirebaseFirestore;
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
public final class RatingRepositoryImpl_Factory implements Factory<RatingRepositoryImpl> {
  private final Provider<FirebaseFirestore> firestoreProvider;

  private final Provider<AuthRepository> authRepositoryProvider;

  private RatingRepositoryImpl_Factory(Provider<FirebaseFirestore> firestoreProvider,
      Provider<AuthRepository> authRepositoryProvider) {
    this.firestoreProvider = firestoreProvider;
    this.authRepositoryProvider = authRepositoryProvider;
  }

  @Override
  public RatingRepositoryImpl get() {
    return newInstance(firestoreProvider.get(), authRepositoryProvider.get());
  }

  public static RatingRepositoryImpl_Factory create(Provider<FirebaseFirestore> firestoreProvider,
      Provider<AuthRepository> authRepositoryProvider) {
    return new RatingRepositoryImpl_Factory(firestoreProvider, authRepositoryProvider);
  }

  public static RatingRepositoryImpl newInstance(FirebaseFirestore firestore,
      AuthRepository authRepository) {
    return new RatingRepositoryImpl(firestore, authRepository);
  }
}
