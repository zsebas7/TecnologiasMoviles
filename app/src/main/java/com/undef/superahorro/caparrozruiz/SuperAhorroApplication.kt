package com.undef.superahorro.caparrozruiz

import android.app.Application
import com.undef.superahorro.caparrozruiz.core.AppContainer

class SuperAhorroApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        AppContainer.initialize(this)
    }
}
