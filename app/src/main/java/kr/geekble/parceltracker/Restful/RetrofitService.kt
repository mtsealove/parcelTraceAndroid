package kr.geekble.parceltracker.Restful

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface RetrofitService {

    @GET("/api/v1/trackingInfo")
    fun getTrackingInfo(
        @Query("t_key") key: String,
        @Query("t_code") code: String = "04",
        @Query("t_invoice") invoice: String
    ): Call<TrackingModel>
}