package com.example.mymoney.presenter

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.activity.viewModels
import com.example.mymoney.R
import com.example.mymoney.databinding.ActivityEconomyBinding
import com.example.mymoney.domain.Economy
import com.example.mymoney.domain.Statement
import com.example.mymoney.framework.BaseApplication
import com.example.mymoney.framework.viewmodel.EconomyViewModel
import com.example.mymoney.framework.viewmodel.EconomyViewModelFactory
import com.example.mymoney.presenter.BankList.Companion.ECONOMY_GOAL
import com.example.mymoney.presenter.BankList.Companion.ECONOMY_ID
import com.example.mymoney.presenter.BankList.Companion.ECONOMY_NAME
import com.example.mymoney.presenter.adapter.ItemInterface
import com.example.mymoney.presenter.adapter.StatementAdapter
import com.shashank.sony.fancytoastlib.FancyToast
import kotlin.properties.Delegates

class EconomyView : AppCompatActivity(), ItemInterface {

    private val insertStatementRequestCode = 1001
    private val editStatementRequestCode = 1002

    private val binding by lazy { ActivityEconomyBinding.inflate(layoutInflater) }

    private val economyViewModel : EconomyViewModel by viewModels{
        EconomyViewModelFactory((application as BaseApplication).repository)
    }

    private lateinit var statementAdapter : StatementAdapter

    private var economyId by Delegates.notNull<Int>()
    private lateinit var economyName : String
    private var economyGoal by Delegates.notNull<Double>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        economyId = intent.getIntExtra(ECONOMY_ID, -1)
        economyName = intent.getStringExtra(ECONOMY_NAME).toString()
        economyGoal = intent.getDoubleExtra(ECONOMY_GOAL, 0.0)

        initData()
        initClicks()
    }

    private fun initClicks() {
        binding.fabAddStatement.setOnClickListener {
            Intent(this, CreateStatement::class.java).let { intent ->
                intent.putExtra("requestCode", insertStatementRequestCode)
                startActivityForResult(intent, insertStatementRequestCode)
            }
        }

        binding.btnClose.setOnClickListener {
            finish()
        }
    }

    private fun initData() {
        binding.txtNameEconomy.text = economyName
        binding.txtGoals.text = "Meta: R$ " + formatMoney(economyGoal)

        statementAdapter = StatementAdapter(arrayListOf(), this)
        binding.rcvStatements.adapter = statementAdapter

        economyViewModel.getListStatement(economyId).observe(this, {
            if(it.isNotEmpty()) {
                showHideEmptyState(false)
                statementAdapter.submitList(it as ArrayList<Statement>)

            } else showHideEmptyState(true)

            var currentMoney = 0.0
            val entradas = it.filter { it.statementType == "Entrada" }.sumOf { it.statementMoney }
            val saidas = it.filter { it.statementType == "Saida" }.sumOf { it.statementMoney }
            currentMoney += entradas - saidas

            binding.txtCurrentMoney.text = "R$ ${currentMoney}"
            binding.txtEntradas.text = "R$ $entradas"
            binding.txtSaidas.text = "R$ $saidas"

            setText(currentMoney, entradas, saidas)

            economyViewModel.updateActualEconomy(currentMoney, economyId)
        })
    }

    private fun setText(currentMoney : Double, entradas : Double, saidas : Double){

        binding.txtCurrentMoney.text = "R$ ${formatMoney(currentMoney)}"

        if(currentMoney < 0.0){
            binding.txtCurrentMoney.setTextColor(Color.parseColor("#720000"))
        }

        binding.txtEntradas.text = "R$ ${formatMoney(entradas)}"
        binding.txtSaidas.text = "R$ ${formatMoney(saidas)}"
    }

    private fun showHideEmptyState(state : Boolean){
        val showEmptyStateAnimation = AnimationUtils.loadAnimation(this, R.anim.fade_in)
        val hideEmptyStateAnimation = AnimationUtils.loadAnimation(this, R.anim.fade_out)

        showEmptyStateAnimation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(p0: Animation?) {
                binding.rcvStatements.startAnimation(AnimationUtils.loadAnimation(baseContext, R.anim.fade_out))
            }

            override fun onAnimationEnd(p0: Animation?) {
                binding.rcvStatements.visibility = View.GONE
                binding.lyEmptyState.root.visibility = View.VISIBLE
            }

            override fun onAnimationRepeat(p0: Animation?) {
                TODO("Not yet implemented")
            }
        })

        hideEmptyStateAnimation.setAnimationListener(object : Animation.AnimationListener{
            override fun onAnimationStart(p0: Animation?) {
                binding.rcvStatements.startAnimation(AnimationUtils.loadAnimation(baseContext, R.anim.fade_in))
            }

            override fun onAnimationEnd(p0: Animation?) {
                binding.rcvStatements.visibility = View.VISIBLE
                binding.lyEmptyState.root.visibility = View.GONE
            }

            override fun onAnimationRepeat(p0: Animation?) {
                TODO("Not yet implemented")
            }
        })

        if(state){
            binding.lyEmptyState.root.startAnimation(showEmptyStateAnimation)
        } else {
            binding.lyEmptyState.root.startAnimation(hideEmptyStateAnimation)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(resultCode == Activity.RESULT_OK){
            val name = data?.getStringExtra(STATEMENT_NAME)
            val date = data?.getStringExtra(STATEMENT_DATE)
            val type = data?.getStringExtra(STATEMENT_TYPE)
            val money = data?.getDoubleExtra(STATEMENT_MONEY, 0.0)
            val idStatement = data?.getIntExtra(STATEMENT_ID, -1)

            when(requestCode){
                insertStatementRequestCode -> {

                    val statement = Statement(null, name!!, date!!, money!!, type!!, economyId)
                    economyViewModel.insertStatement(statement)

                    FancyToast.makeText(this,"Cadastro realizado com sucesso",FancyToast.LENGTH_LONG,FancyToast.SUCCESS,false).show()
                }
                editStatementRequestCode -> {

                    economyViewModel.updateStatement(name!!, money!!, type!!, idStatement!!)

                    FancyToast.makeText(this,"Dados atualizados com sucesso",FancyToast.LENGTH_LONG,FancyToast.SUCCESS,false).show()
                }
            }
        } else {
            FancyToast.makeText(this,"Cancelado",FancyToast.LENGTH_LONG,FancyToast.ERROR,false).show()
        }
    }

    companion object {
        const val STATEMENT_NAME = "STATEMENT_NAME"
        const val STATEMENT_DATE = "STATEMENT_DATE"
        const val STATEMENT_TYPE = "STATEMENT_TYPE"
        const val STATEMENT_MONEY = "STATEMENT_MONEY"
        const val STATEMENT_ID = "STATEMENT_ID"

        fun formatMoney(money : Double) : String = String.format("%.2f", money).replace(".", ",")
    }

    override fun editItem(economy: Economy?, statement: Statement?) {
        Intent(this, CreateStatement::class.java).let { intent ->
            intent.putExtra(STATEMENT_NAME, statement!!.statementName)
            intent.putExtra(STATEMENT_MONEY, statement!!.statementMoney)
            intent.putExtra(STATEMENT_TYPE, statement!!.statementType)
            intent.putExtra(STATEMENT_ID, statement!!.id)
            intent.putExtra("requestCode", editStatementRequestCode)
            startActivityForResult(intent, editStatementRequestCode)
        }
    }

    override fun deleteItem(idEconomy: Int?, idStatement: Int?) {
        economyViewModel.deleteStatement(idStatement!!)
    }
}