package com.example.movieapp.message.service;

import com.example.movieapp.domain.repository.AuthRepository;
import com.example.movieapp.message.util.NotificationHelper;
import dagger.MembersInjector;
import dagger.internal.DaggerGenerated;
import dagger.internal.InjectedFieldSignature;
import dagger.internal.Provider;
import dagger.internal.QualifierMetadata;
import javax.annotation.processing.Generated;

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
public final class MovieAppMessagingService_MembersInjector implements MembersInjector<MovieAppMessagingService> {
  private final Provider<AuthRepository> authRepositoryProvider;

  private final Provider<NotificationHelper> notificationHelperProvider;

  private MovieAppMessagingService_MembersInjector(Provider<AuthRepository> authRepositoryProvider,
      Provider<NotificationHelper> notificationHelperProvider) {
    this.authRepositoryProvider = authRepositoryProvider;
    this.notificationHelperProvider = notificationHelperProvider;
  }

  @Override
  public void injectMembers(MovieAppMessagingService instance) {
    injectAuthRepository(instance, authRepositoryProvider.get());
    injectNotificationHelper(instance, notificationHelperProvider.get());
  }

  public static MembersInjector<MovieAppMessagingService> create(
      Provider<AuthRepository> authRepositoryProvider,
      Provider<NotificationHelper> notificationHelperProvider) {
    return new MovieAppMessagingService_MembersInjector(authRepositoryProvider, notificationHelperProvider);
  }

  @InjectedFieldSignature("com.example.movieapp.message.service.MovieAppMessagingService.authRepository")
  public static void injectAuthRepository(MovieAppMessagingService instance,
      AuthRepository authRepository) {
    instance.authRepository = authRepository;
  }

  @InjectedFieldSignature("com.example.movieapp.message.service.MovieAppMessagingService.notificationHelper")
  public static void injectNotificationHelper(MovieAppMessagingService instance,
      NotificationHelper notificationHelper) {
    instance.notificationHelper = notificationHelper;
  }
}
