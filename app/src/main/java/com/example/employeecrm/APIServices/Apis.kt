package com.example.employeecrm.APIServices

import com.example.employeecrm.model.Employee
import com.example.employeecrm.model.EmployeeDetails
import com.example.employeecrm.model.LoginRequest
import com.example.employeecrm.model.LoginResponse
import com.example.employeecrm.model.MyProfile
import com.example.employeecrm.model.ProjectDetailsRes
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

//    api for get employee details
    @GET("api/v1/employee/all")
    suspend fun getEmployeeDetails(@Header("Cookie") token: String) :Response<EmployeeDetails>

    @GET("api/v1/project/all")
    suspend fun getProjectDetails(@Header("Cookie") token: String) : Response<ProjectDetailsRes>
}


