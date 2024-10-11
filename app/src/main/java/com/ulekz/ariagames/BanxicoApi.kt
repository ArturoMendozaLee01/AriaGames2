package com.ulekz.ariagames

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Header

interface BanxicoApi {
    @GET("series/SF43718/datos/oportuno")
    suspend fun getTipoDeCambio(
        @Header("Bmx-Token") apiKey: String
    ): BanxicoResponse
}

object BanxicoRetrofitInstance {
    val api: BanxicoApi by lazy {
        Retrofit.Builder()
            .baseUrl("https://www.banxico.org.mx/SieAPIRest/service/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(BanxicoApi::class.java)
    }
}
