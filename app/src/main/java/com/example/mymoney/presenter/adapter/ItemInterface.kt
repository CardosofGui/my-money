package com.example.mymoney.presenter.adapter

import com.example.mymoney.domain.Economy
import com.example.mymoney.domain.Statement

interface ItemInterface {
    fun editItem(economy : Economy?, statement : Statement?)

    fun deleteItem(idEconomy : Int?, idStatement : Int?)
}