package com.acanel.groovinapp

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class GroovinApplication: Application() {
    override fun onCreate() {
        super.onCreate()
    }
}