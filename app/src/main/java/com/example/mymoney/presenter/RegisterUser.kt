package com.example.mymoney.presenter

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.content.edit
import com.example.mymoney.R
import com.example.mymoney.databinding.ActivityRegisterUserBinding

class RegisterUser : AppCompatActivity() {


    private val binding by lazy { ActivityRegisterUserBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initClick()
    }

    private fun initClick() {
        binding.btnEnter.setOnClickListener {
            if (!binding.edtName.text.isNullOrEmpty()){
                registerUsername(binding.edtName.text.toString())
            }else{
                Toast.makeText(this, "Digite um nome", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun registerUsername(name : String) {
        val sharedPreferences = getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE)

        sharedPreferences.edit {
            putString(USERNAME_SHARED, name)
            commit()
        }

        Intent(this, BankList::class.java).let {
            startActivity(it)
            finish()
        }
    }

    companion object {

        const val SHARED_PREFERENCES = "com.example.mymoney.SHARED_PREFERENCE_USER"
        const val USERNAME_SHARED = "com.example.mymoney.USERNAME_SHARED"
    }
}