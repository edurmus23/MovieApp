package com.example.movieapp.feature.favorites;

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
public final class FavoritesNavigationModule_ProvideFavoritesEntriesFactory implements Factory<Function1<EntryProviderScope<NavKey>, Unit>> {
  private final Provider<Navigator> navigatorProvider;

  private FavoritesNavigationModule_ProvideFavoritesEntriesFactory(
      Provider<Navigator> navigatorProvider) {
    this.navigatorProvider = navigatorProvider;
  }

  @Override
  public Function1<EntryProviderScope<NavKey>, Unit> get() {
    return provideFavoritesEntries(navigatorProvider.get());
  }

  public static FavoritesNavigationModule_ProvideFavoritesEntriesFactory create(
      Provider<Navigator> navigatorProvider) {
    return new FavoritesNavigationModule_ProvideFavoritesEntriesFactory(navigatorProvider);
  }

  public static Function1<EntryProviderScope<NavKey>, Unit> provideFavoritesEntries(
      Navigator navigator) {
    return Preconditions.checkNotNullFromProvides(FavoritesNavigationModule.INSTANCE.provideFavoritesEntries(navigator));
  }
}
