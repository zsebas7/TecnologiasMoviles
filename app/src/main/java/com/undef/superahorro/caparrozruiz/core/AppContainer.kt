package com.undef.superahorro.caparrozruiz.core

import android.content.Context
import com.undef.superahorro.caparrozruiz.data.local.AppDatabase
import com.undef.superahorro.caparrozruiz.data.local.preferences.UserPreferencesDataSource
import com.undef.superahorro.caparrozruiz.data.remote.RetrofitClient
import com.undef.superahorro.caparrozruiz.data.repository.DefaultWalletRepository
import com.undef.superahorro.caparrozruiz.data.repository.OcrRepository
import com.undef.superahorro.caparrozruiz.data.repository.WalletRepository

object AppContainer {
    lateinit var walletRepository: WalletRepository
        private set

    lateinit var ocrRepository: OcrRepository
        private set

    fun initialize(context: Context) {
        val appContext = context.applicationContext
        val database = AppDatabase.getDatabase(appContext)
        val preferences = UserPreferencesDataSource(appContext)
        walletRepository = DefaultWalletRepository(
            purchaseDao = database.purchaseDao(),
            productDao = database.productDao(),
            preferences = preferences
        )
        ocrRepository = OcrRepository(
            apiService = RetrofitClient.ocrApiService,
            contentResolver = appContext.contentResolver
        )
    }
}
