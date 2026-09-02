package com.example.movieapp.feature.favorites.util

import platform.Foundation.NSUUID

actual fun randomUUID(): String = NSUUID().UUIDString()
