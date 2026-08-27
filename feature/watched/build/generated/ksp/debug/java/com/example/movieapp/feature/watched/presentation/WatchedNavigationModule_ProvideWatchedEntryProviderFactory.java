package com.example.movieapp.feature.watched.presentation;

import androidx.navigation3.runtime.EntryProviderScope;
import androidx.navigation3.runtime.NavKey;
import com.example.movieapp.navigation.Navigator;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.Provider;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;

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
public final class WatchedNavigationModule_ProvideWatchedEntryProviderFactory implements Factory<Function1<EntryProviderScope<NavKey>, Unit>> {
  private final Provider<Navigator> navigatorProvider;

  private WatchedNavigationModule_ProvideWatchedEntryProviderFactory(
      Provider<Navigator> navigatorProvider) {
    this.navigatorProvider = navigatorProvider;
  }

  @Override
  public Function1<EntryProviderScope<NavKey>, Unit> get() {
    return provideWatchedEntryProvider(navigatorProvider.get());
  }

  public static WatchedNavigationModule_ProvideWatchedEntryProviderFactory create(
      Provider<Navigator> navigatorProvider) {
    return new WatchedNavigationModule_ProvideWatchedEntryProviderFactory(navigatorProvider);
  }

  public static Function1<EntryProviderScope<NavKey>, Unit> provideWatchedEntryProvider(
      Navigator navigator) {
    return Preconditions.checkNotNullFromProvides(WatchedNavigationModule.INSTANCE.provideWatchedEntryProvider(navigator));
  }
}
