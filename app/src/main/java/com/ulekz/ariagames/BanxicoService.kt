package com.ulekz.ariagames

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    private const val BASE_URL = "https://www.banxico.org.mx/SieAPIRest/service/v1/"

    // Tu token generado de Banxico
    private const val API_TOKEN = "1f5a329569365f1a251a0321f365e522bf8ff97cb6da54983a6a239ad9a82149"

    // Configuración del cliente HTTP con interceptor para el token
    private val client: OkHttpClient by lazy {
        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)

        OkHttpClient.Builder()
            .addInterceptor(logging) // Para depuración de respuestas
            .addInterceptor { chain: Interceptor.Chain ->
                val originalRequest: Request = chain.request()
                val requestWithHeaders = originalRequest.newBuilder()
                    .addHeader("Bmx-Token", API_TOKEN) // Inyectamos el token en cada solicitud
                    .build()
                chain.proceed(requestWithHeaders)
            }
            .build()
    }

    // Crear la instancia de Retrofit
    val api: BanxicoApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client) // Se añade el cliente con el interceptor
            .addConverterFactory(GsonConverterFactory.create()) // Convertidor para JSON
            .build()
            .create(BanxicoApi::class.java)
    }
}
