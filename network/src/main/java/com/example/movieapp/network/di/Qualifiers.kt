package com.example.movieapp.network.di

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class TmdbRetrofit

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class AiRetrofit

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class TmdbOkHttp

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class AiOkHttp
