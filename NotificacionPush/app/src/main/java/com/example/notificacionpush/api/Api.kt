package com.example.notificacionpush.api

import com.example.notificacionpush.models.LoginResponse
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface Api {
    @FormUrlEncoded
    @POST("users")
    fun userLogin(
        @Field("id") email:String
    ): Call<LoginResponse>

}