package com.example.employeecrm.model



data class LoginRequest(val email: String, val password: String)

data class LoginResponse(val success:Boolean, val message: String, val token: String, val user: User)
data class MyProfile(val success:Boolean, val message: String,  val user: User)


data class User(
    val _id:String,
    val name:String,
    val email: String,
    val designation: String,
    val designationType:String
)


// Singleton object to manage the login response globally
object LoginManager {
    var loginResponse: LoginResponse? = null
}

