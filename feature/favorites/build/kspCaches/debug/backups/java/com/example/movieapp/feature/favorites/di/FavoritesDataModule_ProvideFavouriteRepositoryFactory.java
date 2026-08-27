package com.example.movieapp.feature.favorites.di;

import com.example.movieapp.data.local.dao.FavoriteMovieDao;
import com.example.movieapp.data.local.dao.UserListDao;
import com.example.movieapp.domain.repository.AuthRepository;
import com.example.movieapp.domain.repository.FavouriteRepository;
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
public final class FavoritesDataModule_ProvideFavouriteRepositoryFactory implements Factory<FavouriteRepository> {
  private final Provider<FavoriteMovieDao> daoProvider;

  private final Provider<UserListDao> userListDaoProvider;

  private final Provider<AuthRepository> authRepositoryProvider;

  private final Provider<FirebaseFirestore> firestoreProvider;

  private FavoritesDataModule_ProvideFavouriteRepositoryFactory(
      Provider<FavoriteMovieDao> daoProvider, Provider<UserListDao> userListDaoProvider,
      Provider<AuthRepository> authRepositoryProvider,
      Provider<FirebaseFirestore> firestoreProvider) {
    this.daoProvider = daoProvider;
    this.userListDaoProvider = userListDaoProvider;
    this.authRepositoryProvider = authRepositoryProvider;
    this.firestoreProvider = firestoreProvider;
  }

  @Override
  public FavouriteRepository get() {
    return provideFavouriteRepository(daoProvider.get(), userListDaoProvider.get(), authRepositoryProvider.get(), firestoreProvider.get());
  }

  public static FavoritesDataModule_ProvideFavouriteRepositoryFactory create(
      Provider<FavoriteMovieDao> daoProvider, Provider<UserListDao> userListDaoProvider,
      Provider<AuthRepository> authRepositoryProvider,
      Provider<FirebaseFirestore> firestoreProvider) {
    return new FavoritesDataModule_ProvideFavouriteRepositoryFactory(daoProvider, userListDaoProvider, authRepositoryProvider, firestoreProvider);
  }

  public static FavouriteRepository provideFavouriteRepository(FavoriteMovieDao dao,
      UserListDao userListDao, AuthRepository authRepository, FirebaseFirestore firestore) {
    return Preconditions.checkNotNullFromProvides(FavoritesDataModule.INSTANCE.provideFavouriteRepository(dao, userListDao, authRepository, firestore));
  }
}
