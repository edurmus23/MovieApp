package com.example.movieapp.feature.profile.di;

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
public final class ProfileNavigationModule_ProvideProfileEntriesFactory implements Factory<Function1<EntryProviderScope<NavKey>, Unit>> {
  private final Provider<Navigator> navigatorProvider;

  private ProfileNavigationModule_ProvideProfileEntriesFactory(
      Provider<Navigator> navigatorProvider) {
    this.navigatorProvider = navigatorProvider;
  }

  @Override
  public Function1<EntryProviderScope<NavKey>, Unit> get() {
    return provideProfileEntries(navigatorProvider.get());
  }

  public static ProfileNavigationModule_ProvideProfileEntriesFactory create(
      Provider<Navigator> navigatorProvider) {
    return new ProfileNavigationModule_ProvideProfileEntriesFactory(navigatorProvider);
  }

  public static Function1<EntryProviderScope<NavKey>, Unit> provideProfileEntries(
      Navigator navigator) {
    return Preconditions.checkNotNullFromProvides(ProfileNavigationModule.INSTANCE.provideProfileEntries(navigator));
  }
}
