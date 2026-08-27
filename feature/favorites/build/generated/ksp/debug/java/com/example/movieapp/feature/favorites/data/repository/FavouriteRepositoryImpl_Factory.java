package com.example.movieapp.feature.favorites.data.repository;

import com.example.movieapp.data.local.dao.FavoriteMovieDao;
import com.example.movieapp.data.local.dao.UserListDao;
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
public final class FavouriteRepositoryImpl_Factory implements Factory<FavouriteRepositoryImpl> {
  private final Provider<FavoriteMovieDao> daoProvider;

  private final Provider<UserListDao> userListDaoProvider;

  private final Provider<AuthRepository> authRepositoryProvider;

  private final Provider<FirebaseFirestore> firestoreProvider;

  private FavouriteRepositoryImpl_Factory(Provider<FavoriteMovieDao> daoProvider,
      Provider<UserListDao> userListDaoProvider, Provider<AuthRepository> authRepositoryProvider,
      Provider<FirebaseFirestore> firestoreProvider) {
    this.daoProvider = daoProvider;
    this.userListDaoProvider = userListDaoProvider;
    this.authRepositoryProvider = authRepositoryProvider;
    this.firestoreProvider = firestoreProvider;
  }

  @Override
  public FavouriteRepositoryImpl get() {
    return newInstance(daoProvider.get(), userListDaoProvider.get(), authRepositoryProvider.get(), firestoreProvider.get());
  }

  public static FavouriteRepositoryImpl_Factory create(Provider<FavoriteMovieDao> daoProvider,
      Provider<UserListDao> userListDaoProvider, Provider<AuthRepository> authRepositoryProvider,
      Provider<FirebaseFirestore> firestoreProvider) {
    return new FavouriteRepositoryImpl_Factory(daoProvider, userListDaoProvider, authRepositoryProvider, firestoreProvider);
  }

  public static FavouriteRepositoryImpl newInstance(FavoriteMovieDao dao, UserListDao userListDao,
      AuthRepository authRepository, FirebaseFirestore firestore) {
    return new FavouriteRepositoryImpl(dao, userListDao, authRepository, firestore);
  }
}
