package com.example.movieapp.shared.di

import com.example.movieapp.data.di.dataModule
import com.example.movieapp.feature.auth.di.authModule
import com.example.movieapp.feature.favorites.di.favoritesModule
import com.example.movieapp.feature.movies.di.moviesModule
import com.example.movieapp.feature.profile.di.profileModule
import com.example.movieapp.feature.rating.di.ratingModule
import com.example.movieapp.feature.search.di.searchModule
import com.example.movieapp.feature.social.di.socialModule
import com.example.movieapp.ai.di.aiModule
import com.example.movieapp.network.di.networkModule
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration

fun initKoin(appDeclaration: KoinAppDeclaration = {}) =
    startKoin {
        appDeclaration()
        modules(
            networkModule,
            dataModule,
            authModule,
            favoritesModule,
            moviesModule,
            profileModule,
            ratingModule,
            searchModule,
            socialModule,
            aiModule
        )
    }
