package com.example.movieapp.feature.profile.data.repository;

import com.example.movieapp.data.local.dao.RecentMovieDao;
import com.example.movieapp.domain.repository.SearchRepository;
import com.example.movieapp.feature.rating.domain.repository.RatingRepository;
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
public final class ProfileRepositoryImpl_Factory implements Factory<ProfileRepositoryImpl> {
  private final Provider<RecentMovieDao> recentMovieDaoProvider;

  private final Provider<FirebaseFirestore> firestoreProvider;

  private final Provider<SearchRepository> searchRepositoryProvider;

  private final Provider<RatingRepository> ratingRepositoryProvider;

  private ProfileRepositoryImpl_Factory(Provider<RecentMovieDao> recentMovieDaoProvider,
      Provider<FirebaseFirestore> firestoreProvider,
      Provider<SearchRepository> searchRepositoryProvider,
      Provider<RatingRepository> ratingRepositoryProvider) {
    this.recentMovieDaoProvider = recentMovieDaoProvider;
    this.firestoreProvider = firestoreProvider;
    this.searchRepositoryProvider = searchRepositoryProvider;
    this.ratingRepositoryProvider = ratingRepositoryProvider;
  }

  @Override
  public ProfileRepositoryImpl get() {
    return newInstance(recentMovieDaoProvider.get(), firestoreProvider.get(), searchRepositoryProvider.get(), ratingRepositoryProvider.get());
  }

  public static ProfileRepositoryImpl_Factory create(
      Provider<RecentMovieDao> recentMovieDaoProvider,
      Provider<FirebaseFirestore> firestoreProvider,
      Provider<SearchRepository> searchRepositoryProvider,
      Provider<RatingRepository> ratingRepositoryProvider) {
    return new ProfileRepositoryImpl_Factory(recentMovieDaoProvider, firestoreProvider, searchRepositoryProvider, ratingRepositoryProvider);
  }

  public static ProfileRepositoryImpl newInstance(RecentMovieDao recentMovieDao,
      FirebaseFirestore firestore, SearchRepository searchRepository,
      RatingRepository ratingRepository) {
    return new ProfileRepositoryImpl(recentMovieDao, firestore, searchRepository, ratingRepository);
  }
}
