package com.example.mymoney.domain

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "economy_table")
data class Economy(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id_economy") val id : Int?,
    @ColumnInfo(name = "name_economy") val economyName: String,
    @ColumnInfo(name = "goal_economy") val economyGoal : Double,
    @ColumnInfo(name = "actual_economy") val actualEconomy : Double
)