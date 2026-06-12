package com.undef.superahorro.caparrozruiz.core

import android.content.Context
import com.undef.superahorro.caparrozruiz.data.local.AppDatabase
import com.undef.superahorro.caparrozruiz.data.local.preferences.UserPreferencesDataSource
import com.undef.superahorro.caparrozruiz.data.remote.RetrofitClient
import com.undef.superahorro.caparrozruiz.data.repository.DefaultWalletRepository
import com.undef.superahorro.caparrozruiz.data.repository.OcrRepository
import com.undef.superahorro.caparrozruiz.data.repository.WalletRepository

//existe para que cada viewmodel no cree multiples instancias de las bases de datos ni hagan multiples conexiones al mismo lugar
object AppContainer {
    lateinit var walletRepository: WalletRepository
        private set

    lateinit var ocrRepository: OcrRepository
        private set

    lateinit var database: AppDatabase
        private set

    fun initialize(context: Context) {
        val appContext = context.applicationContext
        database = AppDatabase.getDatabase(appContext)//base de datos
        val preferences = UserPreferencesDataSource(appContext)//data store
        walletRepository = DefaultWalletRepository(//repository
            purchaseDao = database.purchaseDao(),//el repository necesita de la BD y de DataStore ya inicializados
            productDao = database.productDao(),
            preferences = preferences
        )
        ocrRepository = OcrRepository(
            apiService = RetrofitClient.ocrApiService,
            contentResolver = appContext.contentResolver
        )
    }
}
