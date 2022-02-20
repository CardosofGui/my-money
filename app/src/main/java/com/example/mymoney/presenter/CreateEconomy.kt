package com.example.mymoney.presenter

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.mymoney.R
import com.example.mymoney.databinding.ActivityCreateEconomyBinding
import com.example.mymoney.presenter.BankList.Companion.ECONOMY_GOAL
import com.example.mymoney.presenter.BankList.Companion.ECONOMY_ID
import com.example.mymoney.presenter.BankList.Companion.ECONOMY_NAME
import com.example.mymoney.presenter.adapter.EconomyAdapter
import com.shashank.sony.fancytoastlib.FancyToast

class CreateEconomy : AppCompatActivity() {

    private val binding by lazy { ActivityCreateEconomyBinding.inflate(layoutInflater) }
    private var idEconomy : Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        init()
        initClicks()
    }

    private fun initClicks() {
        binding.btnSubmit.setOnClickListener {
            createEconomy()
        }

        binding.btnCancel.setOnClickListener {
            finish()
        }
    }

    private fun createEconomy(){
        if(binding.edtName.text.isNullOrEmpty() || binding.edtGoal.text.isNullOrEmpty()){
            FancyToast.makeText(this,"Preencha todos os campos!",FancyToast.LENGTH_LONG, FancyToast.INFO,false).show()
        } else{
            val name = binding.edtName.text.toString()
            val goal = binding.edtGoal.text.toString().toDouble()

            Intent().let {
                if(idEconomy != -1) it.putExtra(ECONOMY_ID, idEconomy)

                it.putExtra(ECONOMY_NAME, name)
                it.putExtra(ECONOMY_GOAL, goal)
                setResult(Activity.RESULT_OK, it)
            }

            finish()
        }
    }

    private fun init(){
        val requestCode = intent.getIntExtra("requestCode", 0)

        if(requestCode == 2) {
            idEconomy = intent.getIntExtra(ECONOMY_ID, -1)
            val name = intent.getStringExtra(ECONOMY_NAME)
            val goal = intent.getDoubleExtra(ECONOMY_GOAL, 0.0)

            binding.edtName.setText(name)
            binding.edtGoal.setText(goal.toString())
            binding.labelEconomias.setText(">> Editando economia")
            binding.btnSubmit.setText("Editar")
        } else if(requestCode == 0) finish()
    }
}