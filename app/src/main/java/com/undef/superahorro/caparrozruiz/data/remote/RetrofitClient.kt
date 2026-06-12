package com.undef.superahorro.caparrozruiz.data.remote

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
//crea y configura las conexiones hacia las APIs
object RetrofitClient {
    private const val BASE_URL = "https://jsonplaceholder.typicode.com/"

    //by lazy para que la instancia se cree cuando el usuario lo use por primera vez, no cuando arranca la app
    val promotionApiService: PromotionApiService by lazy {
        Retrofit.Builder()
            .baseUrl("https://dummyjson.com/")
            .addConverterFactory(GsonConverterFactory.create())//convierte el JSON a objetos Kotlin
            .build()
            //retrofit lee la interfaz y genera la implementacion real que hace las llamadas HTTP
            .create(PromotionApiService::class.java)
    }

    val syncApiService: SyncApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(SyncApiService::class.java)
    }

    val ocrApiService: OcrApiService by lazy {
        Retrofit.Builder()
            .baseUrl("https://api.ocr.space/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(OcrApiService::class.java)
    }

    val geminiApiService: GeminiApiService by lazy {
        val client = OkHttpClient.Builder()
            //le pusimos timeout largo porque tardaba mucho en pensar las respuestas
            .connectTimeout(120, TimeUnit.SECONDS)
            .readTimeout(120, TimeUnit.SECONDS)
            .writeTimeout(120, TimeUnit.SECONDS)
            .build()
        Retrofit.Builder()
            .baseUrl("https://generativelanguage.googleapis.com/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(GeminiApiService::class.java)
    }
}
