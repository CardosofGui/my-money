package com.example.mymoney.domain

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "statement_table")
data class Statement(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id_statement") val id : Int?,
    @ColumnInfo(name = "name_statement") val statementName: String,
    @ColumnInfo(name = "date_statement") val statementDate : String,
    @ColumnInfo(name = "money_statement") val statementMoney : Double,
    @ColumnInfo(name = "type_statement") val statementType : String,
    @ColumnInfo(name = "id_economy") val idEconomy: Int
)