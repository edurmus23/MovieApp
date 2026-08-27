package com.example.movieapp.feature.movies.di;

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
public final class MoviesNavigationModule_ProvideMoviesEntryFactory implements Factory<Function1<EntryProviderScope<NavKey>, Unit>> {
  private final Provider<Navigator> navigatorProvider;

  private MoviesNavigationModule_ProvideMoviesEntryFactory(Provider<Navigator> navigatorProvider) {
    this.navigatorProvider = navigatorProvider;
  }

  @Override
  public Function1<EntryProviderScope<NavKey>, Unit> get() {
    return provideMoviesEntry(navigatorProvider.get());
  }

  public static MoviesNavigationModule_ProvideMoviesEntryFactory create(
      Provider<Navigator> navigatorProvider) {
    return new MoviesNavigationModule_ProvideMoviesEntryFactory(navigatorProvider);
  }

  public static Function1<EntryProviderScope<NavKey>, Unit> provideMoviesEntry(
      Navigator navigator) {
    return Preconditions.checkNotNullFromProvides(MoviesNavigationModule.INSTANCE.provideMoviesEntry(navigator));
  }
}
