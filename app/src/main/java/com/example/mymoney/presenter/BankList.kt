package com.example.mymoney.presenter

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.activity.viewModels
import com.example.mymoney.R
import com.example.mymoney.framework.BaseApplication
import com.example.mymoney.databinding.ActivityBankListBinding
import com.example.mymoney.domain.Economy
import com.example.mymoney.domain.Statement
import com.example.mymoney.framework.viewmodel.BankViewModel
import com.example.mymoney.framework.viewmodel.BankViewModelFactory
import com.example.mymoney.presenter.RegisterUser.Companion.SHARED_PREFERENCES
import com.example.mymoney.presenter.RegisterUser.Companion.USERNAME_SHARED
import com.example.mymoney.presenter.adapter.EconomyAdapter
import com.example.mymoney.presenter.adapter.EconomyInterface
import com.shashank.sony.fancytoastlib.FancyToast

class BankList : AppCompatActivity(), EconomyInterface {

    private val binding by lazy { ActivityBankListBinding.inflate(layoutInflater) }
    private val insertEconomyCode = 1
    private val editEconomyCode = 2
    private lateinit var economyAdapter : EconomyAdapter

    private val bankViewModel : BankViewModel by viewModels{
        BankViewModelFactory((application as BaseApplication).repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val nameUser = getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE).getString(USERNAME_SHARED, "Sem nome")
        binding.txtUsername.text = "Continue fazendo suas economias $nameUser"


        setTextAnimation()
        buttonClick()
        initRecyclerView()
    }

    private fun setTextAnimation() {
        binding.txtUsername.ellipsize = TextUtils.TruncateAt.MARQUEE
        binding.txtUsername.isSingleLine = true
        binding.txtUsername.isSelected = true

        binding.labelWelcome.ellipsize = TextUtils.TruncateAt.MARQUEE
        binding.labelWelcome.isSingleLine = true
        binding.labelWelcome.isSelected = true
    }

    private fun initRecyclerView() {
        economyAdapter = EconomyAdapter(arrayListOf(), this)
        binding.rcvEconomy.adapter = economyAdapter

        bankViewModel.getEconomy.observe(this, {
            if(it.isNotEmpty()) {
                showHideEmptyState(false)
                economyAdapter.submitList(it as ArrayList<Economy>)

            } else showHideEmptyState(true)
        })
    }

    private fun showHideEmptyState(state : Boolean){
        val showEmptyStateAnimation = AnimationUtils.loadAnimation(this, R.anim.fade_in)
        val hideEmptyStateAnimation = AnimationUtils.loadAnimation(this, R.anim.fade_out)

        showEmptyStateAnimation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(p0: Animation?) {
                binding.rcvEconomy.startAnimation(AnimationUtils.loadAnimation(baseContext, R.anim.fade_out))
            }

            override fun onAnimationEnd(p0: Animation?) {
                binding.rcvEconomy.visibility = View.GONE
                binding.lyEmptyState.root.visibility = View.VISIBLE
            }

            override fun onAnimationRepeat(p0: Animation?) {
                TODO("Not yet implemented")
            }
        })

        hideEmptyStateAnimation.setAnimationListener(object : Animation.AnimationListener{
            override fun onAnimationStart(p0: Animation?) {
                binding.rcvEconomy.startAnimation(AnimationUtils.loadAnimation(baseContext, R.anim.fade_in))
            }

            override fun onAnimationEnd(p0: Animation?) {
                binding.rcvEconomy.visibility = View.VISIBLE
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

    private fun buttonClick() {
        binding.btnCreate.setOnClickListener {
            Intent(this, CreateEconomy::class.java).let {
                it.putExtra("requestCode", insertEconomyCode)
                startActivityForResult(it, insertEconomyCode)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK){
            when(requestCode){
                insertEconomyCode -> {
                    val name = data?.getStringExtra(ECONOMY_NAME)
                    val goal = data?.getDoubleExtra(ECONOMY_GOAL, 0.0)

                    val economia = Economy(id = null, economyName = name.toString(), economyGoal = goal!!.toDouble(), actualEconomy = 0.0)

                    bankViewModel.insertEconomy(economia)

                    FancyToast.makeText(this,"Cadastro realizado com sucesso", FancyToast.LENGTH_LONG, FancyToast.SUCCESS,false).show()
                }
                editEconomyCode -> {
                    val name = data?.getStringExtra(ECONOMY_NAME)
                    val goal = data?.getDoubleExtra(ECONOMY_GOAL, 0.0)
                    val idEconomy = data?.getIntExtra(ECONOMY_ID, -1)

                    bankViewModel.editEconomy(idEconomy!!, name!!, goal!!)

                    FancyToast.makeText(this,"Dados atualizados com sucesso", FancyToast.LENGTH_LONG, FancyToast.SUCCESS,false).show()

                }
            }
        } else{
            FancyToast.makeText(this,"Cancelado",FancyToast.LENGTH_LONG,FancyToast.ERROR,false).show()
        }
    }

    companion object {
        const val ECONOMY_ID = "ECONOMY-ID"
        const val ECONOMY_NAME = "ECONOMY-NAME"
        const val ECONOMY_GOAL = "ECONOMY_GOAL"
    }

    override fun actionChangeActivity(economyID: Int, economyName: String, economyGoal : Double) {
        Intent(this, EconomyView::class.java).let { intent ->
            intent.putExtra(ECONOMY_ID, economyID)
            intent.putExtra(ECONOMY_NAME, economyName)
            intent.putExtra(ECONOMY_GOAL, economyGoal)
            startActivity(intent)
        }
    }

    override fun editItem(economy: Economy?, statement: Statement?) {
        Intent(this, CreateEconomy::class.java).let { intent ->
            intent.putExtra(ECONOMY_ID, economy!!.id)
            intent.putExtra(ECONOMY_NAME, economy!!.economyName)
            intent.putExtra(ECONOMY_GOAL, economy!!.economyGoal)
            intent.putExtra("requestCode", editEconomyCode)
            startActivityForResult(intent, editEconomyCode)
        }
    }

    override fun deleteItem(idEconomy: Int?, idStatement: Int?) {
        bankViewModel.deleteEconomy(idEconomy!!)
    }
}