package com.example.mymoney.data

import com.example.mymoney.domain.Economy
import com.example.mymoney.domain.Statement
import com.example.mymoney.framework.DAO
import kotlinx.coroutines.flow.Flow

class EconomyRepository(private val economyDAO : DAO) {

    val getAllEconomy : Flow<List<Economy>> = economyDAO.getAllEconomy()

    fun getStatement(id : Int) : Flow<List<Statement>> {
        return economyDAO.getStatement(id)
    }

    suspend fun insertEconomy(economy: Economy){
        economyDAO.insertEconomy(economy)
    }

    suspend fun insertStatement(statement: Statement){
        economyDAO.insertStatement(statement)
    }

    suspend fun updateActualEconomy(newMoney : Double, id : Int){
        economyDAO.updateActualEconomy(newMoney, id)
    }

    suspend fun deleteEconomy(id : Int){
        economyDAO.deleteEconomy(id)
    }

    suspend fun updateEconomy(idEconomy : Int, newName : String, newGoal : Double){
        economyDAO.updateEconomy(newName, newGoal, idEconomy)
    }

    suspend fun deleteStatement(id : Int){
        economyDAO.deleteStatement(id)
    }

    suspend fun updateStatement(newName : String, money : Double, newType : String, idStatement : Int){
        economyDAO.updateStatement(newName, money, newType, idStatement)
    }
}