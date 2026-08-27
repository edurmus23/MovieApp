package com.example.movieapp.feature.profile.domain.usecase;

import android.content.Context;
import com.example.movieapp.data.local.dao.FavoriteMovieDao;
import com.example.movieapp.data.local.dao.UserListDao;
import com.example.movieapp.domain.repository.AuthRepository;
import com.example.movieapp.domain.repository.SearchRepository;
import com.example.movieapp.domain.repository.SocialRepository;
import com.example.movieapp.domain.repository.WatchedRepository;
import com.example.movieapp.feature.profile.domain.repository.ProfileRepository;
import com.example.movieapp.feature.rating.domain.repository.RatingRepository;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Provider;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;

@ScopeMetadata
@QualifierMetadata("dagger.hilt.android.qualifiers.ApplicationContext")
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
public final class GetProfileUseCase_Factory implements Factory<GetProfileUseCase> {
  private final Provider<AuthRepository> authRepositoryProvider;

  private final Provider<FavoriteMovieDao> favoriteMovieDaoProvider;

  private final Provider<UserListDao> userListDaoProvider;

  private final Provider<ProfileRepository> profileRepositoryProvider;

  private final Provider<SearchRepository> searchRepositoryProvider;

  private final Provider<WatchedRepository> watchedRepositoryProvider;

  private final Provider<SocialRepository> socialRepositoryProvider;

  private final Provider<RatingRepository> ratingRepositoryProvider;

  private final Provider<Context> contextProvider;

  private GetProfileUseCase_Factory(Provider<AuthRepository> authRepositoryProvider,
      Provider<FavoriteMovieDao> favoriteMovieDaoProvider,
      Provider<UserListDao> userListDaoProvider,
      Provider<ProfileRepository> profileRepositoryProvider,
      Provider<SearchRepository> searchRepositoryProvider,
      Provider<WatchedRepository> watchedRepositoryProvider,
      Provider<SocialRepository> socialRepositoryProvider,
      Provider<RatingRepository> ratingRepositoryProvider, Provider<Context> contextProvider) {
    this.authRepositoryProvider = authRepositoryProvider;
    this.favoriteMovieDaoProvider = favoriteMovieDaoProvider;
    this.userListDaoProvider = userListDaoProvider;
    this.profileRepositoryProvider = profileRepositoryProvider;
    this.searchRepositoryProvider = searchRepositoryProvider;
    this.watchedRepositoryProvider = watchedRepositoryProvider;
    this.socialRepositoryProvider = socialRepositoryProvider;
    this.ratingRepositoryProvider = ratingRepositoryProvider;
    this.contextProvider = contextProvider;
  }

  @Override
  public GetProfileUseCase get() {
    return newInstance(authRepositoryProvider.get(), favoriteMovieDaoProvider.get(), userListDaoProvider.get(), profileRepositoryProvider.get(), searchRepositoryProvider.get(), watchedRepositoryProvider.get(), socialRepositoryProvider.get(), ratingRepositoryProvider.get(), contextProvider.get());
  }

  public static GetProfileUseCase_Factory create(Provider<AuthRepository> authRepositoryProvider,
      Provider<FavoriteMovieDao> favoriteMovieDaoProvider,
      Provider<UserListDao> userListDaoProvider,
      Provider<ProfileRepository> profileRepositoryProvider,
      Provider<SearchRepository> searchRepositoryProvider,
      Provider<WatchedRepository> watchedRepositoryProvider,
      Provider<SocialRepository> socialRepositoryProvider,
      Provider<RatingRepository> ratingRepositoryProvider, Provider<Context> contextProvider) {
    return new GetProfileUseCase_Factory(authRepositoryProvider, favoriteMovieDaoProvider, userListDaoProvider, profileRepositoryProvider, searchRepositoryProvider, watchedRepositoryProvider, socialRepositoryProvider, ratingRepositoryProvider, contextProvider);
  }

  public static GetProfileUseCase newInstance(AuthRepository authRepository,
      FavoriteMovieDao favoriteMovieDao, UserListDao userListDao,
      ProfileRepository profileRepository, SearchRepository searchRepository,
      WatchedRepository watchedRepository, SocialRepository socialRepository,
      RatingRepository ratingRepository, Context context) {
    return new GetProfileUseCase(authRepository, favoriteMovieDao, userListDao, profileRepository, searchRepository, watchedRepository, socialRepository, ratingRepository, context);
  }
}
