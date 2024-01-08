package com.example.employeecrm.APIServices

import com.example.employeecrm.model.LoginRequest
import com.example.employeecrm.model.LoginResponse
import com.example.employeecrm.model.MyProfile
import com.example.employeecrm.model.User
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface Apis {
    @POST("login")
     suspend fun auth(@Body loginRequest: LoginRequest): Response<LoginResponse>

     //my profile
    @GET("api/v1/users/me")
    suspend fun myProfile(@Header("Cookie") token: String): Response<MyProfile>
}


