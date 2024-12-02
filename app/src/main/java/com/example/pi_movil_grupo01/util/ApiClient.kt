package com.example.pi_movil_grupo01.util

import android.content.Context
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {

    /*private const val BASE_URL = "https://artistic-truth-prueba00.up.railway.app/"*/
    private const val BASE_URL = "http://192.168.76.44:8080/"
    private var retrofit: Retrofit? = null


    fun getClient(context: Context): Retrofit {
        if (retrofit == null) {
            val client = OkHttpClient.Builder()
                .addInterceptor(AuthInterceptor(context))
                .build()

            retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
        return retrofit!!
    }


}