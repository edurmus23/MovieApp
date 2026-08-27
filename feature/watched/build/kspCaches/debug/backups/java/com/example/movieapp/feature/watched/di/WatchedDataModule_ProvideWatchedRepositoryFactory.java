package com.example.movieapp.feature.watched.di;

import com.example.movieapp.data.local.dao.WatchedMovieDao;
import com.example.movieapp.domain.repository.AuthRepository;
import com.example.movieapp.domain.repository.WatchedRepository;
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
public final class WatchedDataModule_ProvideWatchedRepositoryFactory implements Factory<WatchedRepository> {
  private final Provider<WatchedMovieDao> daoProvider;

  private final Provider<AuthRepository> authRepositoryProvider;

  private final Provider<FirebaseFirestore> firestoreProvider;

  private WatchedDataModule_ProvideWatchedRepositoryFactory(Provider<WatchedMovieDao> daoProvider,
      Provider<AuthRepository> authRepositoryProvider,
      Provider<FirebaseFirestore> firestoreProvider) {
    this.daoProvider = daoProvider;
    this.authRepositoryProvider = authRepositoryProvider;
    this.firestoreProvider = firestoreProvider;
  }

  @Override
  public WatchedRepository get() {
    return provideWatchedRepository(daoProvider.get(), authRepositoryProvider.get(), firestoreProvider.get());
  }

  public static WatchedDataModule_ProvideWatchedRepositoryFactory create(
      Provider<WatchedMovieDao> daoProvider, Provider<AuthRepository> authRepositoryProvider,
      Provider<FirebaseFirestore> firestoreProvider) {
    return new WatchedDataModule_ProvideWatchedRepositoryFactory(daoProvider, authRepositoryProvider, firestoreProvider);
  }

  public static WatchedRepository provideWatchedRepository(WatchedMovieDao dao,
      AuthRepository authRepository, FirebaseFirestore firestore) {
    return Preconditions.checkNotNullFromProvides(WatchedDataModule.INSTANCE.provideWatchedRepository(dao, authRepository, firestore));
  }
}
