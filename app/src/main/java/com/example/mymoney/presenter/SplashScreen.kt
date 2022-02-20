package com.example.mymoney.presenter

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.example.mymoney.R
import androidx.appcompat.app.AppCompatDelegate




class SplashScreen : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        setContentView(R.layout.activity_splash_screen)

        Handler().postDelayed(Runnable {
            run {
                val nameUser = getSharedPreferences(RegisterUser.SHARED_PREFERENCES, Context.MODE_PRIVATE).getString(RegisterUser.USERNAME_SHARED, "Null")

                val intent = if(nameUser == "Null") Intent(this, RegisterUser::class.java) else Intent(this, BankList::class.java)

                startActivity(intent)
                finish()
            }
        }, 3000)
    }
}