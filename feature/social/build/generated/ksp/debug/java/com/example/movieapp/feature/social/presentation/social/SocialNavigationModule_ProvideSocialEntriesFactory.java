package com.example.movieapp.feature.social.presentation.social;

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
public final class SocialNavigationModule_ProvideSocialEntriesFactory implements Factory<Function1<EntryProviderScope<NavKey>, Unit>> {
  private final Provider<Navigator> navigatorProvider;

  private SocialNavigationModule_ProvideSocialEntriesFactory(
      Provider<Navigator> navigatorProvider) {
    this.navigatorProvider = navigatorProvider;
  }

  @Override
  public Function1<EntryProviderScope<NavKey>, Unit> get() {
    return provideSocialEntries(navigatorProvider.get());
  }

  public static SocialNavigationModule_ProvideSocialEntriesFactory create(
      Provider<Navigator> navigatorProvider) {
    return new SocialNavigationModule_ProvideSocialEntriesFactory(navigatorProvider);
  }

  public static Function1<EntryProviderScope<NavKey>, Unit> provideSocialEntries(
      Navigator navigator) {
    return Preconditions.checkNotNullFromProvides(SocialNavigationModule.INSTANCE.provideSocialEntries(navigator));
  }
}
