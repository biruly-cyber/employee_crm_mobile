package com.example.employeecrm.activities.home

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar;

import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.lifecycleScope
import com.example.employeecrm.APIServices.Apis
import com.example.employeecrm.MainActivity
import com.example.employeecrm.R
import com.example.employeecrm.activities.admin.AdminDashboard
import com.example.employeecrm.databinding.ActivityHomeBinding
import com.example.employeecrm.model.LoginManager
import com.example.employeecrm.model.LoginRequest
import com.example.employeecrm.model.LoginResponse
import com.google.android.material.navigation.NavigationView
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.lang.Exception

class Home : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private lateinit var token: String

    // Base URL of your API
    val BASE_URL = "http://192.168.1.7:4000/"

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)


        //support actionbar
        setUpActionBar()

        // Checking if a login response is stored and accessing its properties
        val storedLoginResponse = LoginManager.loginResponse

        if (storedLoginResponse != null) {
            token = storedLoginResponse.token
            Log.d("response result", token)
        }

        updateNavigationUserDetails(storedLoginResponse)



//        navigation bar
        val navigationView = findViewById<NavigationView>(R.id.navigationView)
        navigationView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.admin_dashboard -> {
                    startActivity(Intent(this, AdminDashboard::class.java))
                    // Optionally, add logic here after starting the activity for the selected item
                }
                // Add other menu item cases here if needed
                else -> {
                    // Handle other menu item clicks here if needed
                }
            }
            findViewById<DrawerLayout>(R.id.drawerLayout).closeDrawer(GravityCompat.START)
            true // Return true to indicate the item is selected
        }

        //get profile
        getProfile()

    }

    private fun updateNavigationUserDetails(storedLoginResponse: LoginResponse?) {
        val header = binding.navigationView.getHeaderView(0)
        val userName = header.findViewById<TextView>(R.id.tv_name)
        val userDesignation = header.findViewById<TextView>(R.id.tv_designation)

        if (storedLoginResponse != null && userName != null) {
            userName.text = "Name: ${storedLoginResponse.user.name}"
            userDesignation.text = "Desination: ${storedLoginResponse.user.designation}"
        }
    }



    private fun setUpActionBar() {
//        set drawer laout
        setSupportActionBar(findViewById<Toolbar>(R.id.my_toolbar))

        findViewById<Toolbar>(R.id.my_toolbar).setNavigationIcon(R.drawable.home)

        findViewById<Toolbar>(R.id.my_toolbar).setNavigationOnClickListener {
            Log.d("clicked", "clicked")
            toggleDrawer()
        }
    }

    //    toggle the navigation bar
    private fun toggleDrawer() {
        if (findViewById<DrawerLayout>(R.id.drawerLayout).isDrawerOpen(GravityCompat.START)) {
            findViewById<DrawerLayout>(R.id.drawerLayout).closeDrawer(GravityCompat.START)
        } else {
            findViewById<DrawerLayout>(R.id.drawerLayout).openDrawer(GravityCompat.START)
        }
    }

    private fun getProfile() {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService = retrofit.create(Apis::class.java)




        lifecycleScope.launch {
            try {
                val response = apiService.myProfile("token=$token")
                if (response.isSuccessful) {
                    val loginResponse = response.body()
                    if (loginResponse != null) {
                        // Handle successful login response
                        Log.d("myProfile", loginResponse.toString())
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