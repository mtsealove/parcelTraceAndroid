package kr.geekble.parceltracker.Restful

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class Rest() {
    private val client = OkHttpClient.Builder()
        .connectTimeout(1, TimeUnit.MINUTES)
        .readTimeout(1, TimeUnit.MINUTES)
        .writeTimeout(1, TimeUnit.MINUTES)
        .build()


    private val retrofit = Retrofit.Builder()
        .baseUrl("https://info.sweettracker.co.kr/")
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    fun getService(): RetrofitService = retrofit.create(RetrofitService::class.java)
}