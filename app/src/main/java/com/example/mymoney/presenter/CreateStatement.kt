package com.example.mymoney.presenter

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.mymoney.R
import com.example.mymoney.databinding.ActivityCreateStatementBinding
import com.example.mymoney.presenter.EconomyView.Companion.STATEMENT_DATE
import com.example.mymoney.presenter.EconomyView.Companion.STATEMENT_ID
import com.example.mymoney.presenter.EconomyView.Companion.STATEMENT_MONEY
import com.example.mymoney.presenter.EconomyView.Companion.STATEMENT_NAME
import com.example.mymoney.presenter.EconomyView.Companion.STATEMENT_TYPE
import com.shashank.sony.fancytoastlib.FancyToast
import java.text.SimpleDateFormat
import java.util.*

class CreateStatement : AppCompatActivity() {

    private val binding by lazy { ActivityCreateStatementBinding.inflate(layoutInflater) }
    private var typeStatement : String? = null

    private var idStatement = -1


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        init()
        initClicks()
    }

    private fun init(){
        val requestCode = intent.getIntExtra("requestCode", 0)

        if(requestCode == 1002) {
            idStatement = intent.getIntExtra(STATEMENT_ID, -1)
            val nameStatementActual = intent.getStringExtra(STATEMENT_NAME)
            val moneyStatementActual = intent.getDoubleExtra(STATEMENT_MONEY, 0.0)
            val typeStatementActual = intent.getStringExtra(STATEMENT_TYPE)

            if(typeStatementActual == "Entrada") entradaSelected()
            else saidaSelected()

            binding.edtDescription.setText(nameStatementActual)
            binding.edtMoney.setText(moneyStatementActual.toString())

            binding.labelEconomias.setText(">> Editando extrato")
            binding.btnSubmit.setText("Editar")
        } else if(requestCode == 0) finish()
    }

    private fun createStatement() {
        if(binding.edtDescription.text.isNullOrEmpty() || binding.edtMoney.text.isNullOrEmpty()){
            FancyToast.makeText(this,"Preencha todos os campos!", FancyToast.LENGTH_LONG, FancyToast.INFO,false).show()
        } else {
            val name = binding.edtDescription.text.toString()
            val money = binding.edtMoney.text.toString().toDouble()
            val date = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(Date()).toString()

            Intent().let {
                if(idStatement != -1) it.putExtra(STATEMENT_ID, idStatement)

                it.putExtra(STATEMENT_NAME, name)
                it.putExtra(STATEMENT_DATE, date)
                it.putExtra(STATEMENT_MONEY, money)
                it.putExtra(STATEMENT_TYPE, typeStatement)

                setResult(Activity.RESULT_OK, it)
            }

            finish()
        }
    }

    private fun initClicks() {
        binding.btnEntrada.setOnClickListener {
            entradaSelected()
        }

        binding.btnSaida.setOnClickListener {
            saidaSelected()
        }

        binding.btnSubmit.setOnClickListener {
            createStatement()
        }

        binding.btnCancel.setOnClickListener {
            finish()
        }
    }

    private fun saidaSelected() {
        typeStatement = "Saida"

        binding.btnEntrada.setBackgroundColor(ContextCompat.getColor(this,
            R.color.unselected_button_entrada
        ))
        binding.btnSaida.setBackgroundColor(ContextCompat.getColor(this,
            R.color.selected_button_saida
        ))
    }

    private fun entradaSelected() {
        typeStatement = "Entrada"

        binding.btnEntrada.setBackgroundColor(ContextCompat.getColor(this,
            R.color.selected_button_entrada
        ))
        binding.btnSaida.setBackgroundColor(ContextCompat.getColor(this,
            R.color.unselected_button_saida
        ))
    }
}