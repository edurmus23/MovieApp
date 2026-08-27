package com.example.movieapp.feature.watched.data.repository;

import com.example.movieapp.data.local.dao.WatchedMovieDao;
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
public final class WatchedRepositoryImpl_Factory implements Factory<WatchedRepositoryImpl> {
  private final Provider<WatchedMovieDao> daoProvider;

  private final Provider<AuthRepository> authRepositoryProvider;

  private final Provider<FirebaseFirestore> firestoreProvider;

  private WatchedRepositoryImpl_Factory(Provider<WatchedMovieDao> daoProvider,
      Provider<AuthRepository> authRepositoryProvider,
      Provider<FirebaseFirestore> firestoreProvider) {
    this.daoProvider = daoProvider;
    this.authRepositoryProvider = authRepositoryProvider;
    this.firestoreProvider = firestoreProvider;
  }

  @Override
  public WatchedRepositoryImpl get() {
    return newInstance(daoProvider.get(), authRepositoryProvider.get(), firestoreProvider.get());
  }

  public static WatchedRepositoryImpl_Factory create(Provider<WatchedMovieDao> daoProvider,
      Provider<AuthRepository> authRepositoryProvider,
      Provider<FirebaseFirestore> firestoreProvider) {
    return new WatchedRepositoryImpl_Factory(daoProvider, authRepositoryProvider, firestoreProvider);
  }

  public static WatchedRepositoryImpl newInstance(WatchedMovieDao dao,
      AuthRepository authRepository, FirebaseFirestore firestore) {
    return new WatchedRepositoryImpl(dao, authRepository, firestore);
  }
}
