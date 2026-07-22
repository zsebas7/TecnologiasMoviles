package com.undef.superahorro.caparrozruiz

import android.app.Application
import com.undef.superahorro.caparrozruiz.core.AppContainer

class SuperAhorroApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        AppContainer.initialize(this) //this es el contexto de la aplicacion, que room y datastore se necesitan para acceder al almacenamiento
        AppContainer.notificationHelper.createChannel()
    }
}
