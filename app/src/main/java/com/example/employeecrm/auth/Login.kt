package com.example.employeecrm.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.example.employeecrm.APIServices.Apis
import com.example.employeecrm.MainActivity
import com.example.employeecrm.activities.home.Home
import com.example.employeecrm.databinding.ActivityLoginBinding
import com.example.employeecrm.model.LoginManager
import com.example.employeecrm.model.LoginRequest
import com.example.employeecrm.model.LoginResponse
import com.example.employeecrm.model.User
import kotlinx.coroutines.launch

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException

class Login : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private val BASE_URL = "http://192.168.1.51:4000/api/v1/users/" // Remove '/users/login' from the base URL

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Handle sign-in button click
        binding.btnSignIn.setOnClickListener {
            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()
            handleOnSignIn(email, password)
        }
    }

    // Function to handle the sign-in logic
    private fun handleOnSignIn(email: String, password: String) {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService = retrofit.create(Apis::class.java)

        // Inside a coroutine scope (e.g., using lifecycleScope.launch)
        lifecycleScope.launch {
            try {
                val response = apiService.auth(LoginRequest(email, password))
                if (response.isSuccessful) {
                    val loginResponse = response.body()
                    if (loginResponse != null) {
                        // Handle successful login response
                        Log.d("LoginSuccess", "${loginResponse.success}")
                        Log.d("LoginSuccess", loginResponse.toString())

                        val receivedLoginResponse = LoginResponse(
                            loginResponse.success,
                            loginResponse.message,
                            loginResponse.token,
                            user = User(loginResponse.user._id, loginResponse.user.name, loginResponse.user.email, loginResponse.user.designation,loginResponse.user.designationType)
                        )

                        // Storing the login response in the LoginManager
                        LoginManager.loginResponse = receivedLoginResponse

                        if(loginResponse.success){
                            Toast.makeText(this@Login, "${loginResponse.message}", Toast.LENGTH_LONG).show()
                            startActivity(Intent(this@Login, Home::class.java))
                            finish()
                        }
                    } else {
                        // Handle scenario where response body is null
                        Log.d("LoginError", "Empty response body")
                    }
                } else {
                    // Handle unsuccessful login (e.g., invalid credentials, server errors)
                    val errorBody = response.errorBody()?.string()
                    Log.d("LoginError", "Error: $errorBody")
                }
            } catch (e: IOException) {
                // Handle network issues or I/O problems
                Log.d("LoginError", "Network error: ${e.message}")
            } catch (e: Exception) {
                // Handle other exceptions
                Log.d("LoginError", "Error: ${e.message}")
            }
        }
    }
}
