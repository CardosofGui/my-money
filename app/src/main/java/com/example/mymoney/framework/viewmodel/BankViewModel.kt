package com.example.mymoney.framework.viewmodel

import androidx.lifecycle.*
import com.example.mymoney.data.EconomyRepository
import com.example.mymoney.domain.Economy
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException

class BankViewModel(private val repository : EconomyRepository) : ViewModel() {

    val getEconomy : LiveData<List<Economy>> = repository.getAllEconomy.asLiveData()

    fun insertEconomy(economy: Economy) = viewModelScope.launch {
        repository.insertEconomy(economy)
    }

    fun editEconomy(idEconomy: Int, newName : String, newGoal : Double) = viewModelScope.launch {
        repository.updateEconomy(idEconomy, newName, newGoal)
    }

    fun deleteEconomy(idEconomy: Int) = viewModelScope.launch {
        repository.deleteEconomy(idEconomy)
    }
}

class BankViewModelFactory(private val repository: EconomyRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(BankViewModel::class.java)){
            return BankViewModel(repository) as T
        }
        throw IllegalArgumentException("Viewmodel desconhecida")
    }
}