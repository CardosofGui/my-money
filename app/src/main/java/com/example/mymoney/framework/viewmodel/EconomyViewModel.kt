package com.example.mymoney.framework.viewmodel

import android.app.Application
import android.view.View
import androidx.lifecycle.*
import com.example.mymoney.data.EconomyRepository
import com.example.mymoney.domain.Statement
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException

class EconomyViewModel(private val repository: EconomyRepository) : ViewModel() {

    fun getListStatement(id : Int) : LiveData<List<Statement>> {
        return repository.getStatement(id).asLiveData()
    }

    fun insertStatement(statement: Statement) = viewModelScope.launch {
        repository.insertStatement(statement)
    }

    fun updateActualEconomy(newMoney : Double, id: Int) = viewModelScope.launch {
        repository.updateActualEconomy(newMoney, id)
    }

    fun updateStatement(newName : String, money : Double, newType : String, idStatement : Int) = viewModelScope.launch {
        repository.updateStatement(newName, money, newType, idStatement)
    }

    fun deleteStatement(id : Int) = viewModelScope.launch {
        repository.deleteStatement(id)
    }
}

class EconomyViewModelFactory(private val repository: EconomyRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(EconomyViewModel::class.java)){
            return EconomyViewModel(repository) as T
        }
        throw IllegalArgumentException("Viewmodel desconhecida")
    }
}