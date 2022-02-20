package com.example.mymoney.framework

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.mymoney.domain.Economy
import com.example.mymoney.domain.Statement
import kotlinx.coroutines.flow.Flow

@Dao
interface DAO {
    @Query("SELECT * FROM economy_table")
    fun getAllEconomy() : Flow<List<Economy>>

    @Query("SELECT * FROM statement_table WHERE id_economy = :id ORDER BY id_statement DESC")
    fun getStatement(id : Int) : Flow<List<Statement>>

    @Query("UPDATE economy_table SET actual_economy = :newEconomy WHERE id_economy = :id")
    suspend fun updateActualEconomy(newEconomy : Double, id : Int)

    @Query("UPDATE economy_table SET name_economy = :newName, goal_economy = :newGoal WHERE id_economy = :idEconomy")
    suspend fun updateEconomy(newName : String, newGoal : Double, idEconomy : Int)

    @Query("DELETE FROM economy_table WHERE id_economy = :id")
    suspend fun deleteEconomy(id : Int)

    @Query("UPDATE statement_table SET name_statement = :newName, money_statement = :money, type_statement = :newType WHERE id_statement = :idStatemet")
    suspend fun updateStatement(newName : String, money : Double, newType : String, idStatemet : Int)

    @Query("DELETE FROM statement_table WHERE id_statement = :id")
    suspend fun deleteStatement(id : Int)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertEconomy(economy: Economy)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertStatement(statement: Statement)
}