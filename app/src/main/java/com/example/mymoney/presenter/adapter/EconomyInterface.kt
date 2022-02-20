package com.example.mymoney.presenter.adapter

interface EconomyInterface : ItemInterface {
    fun actionChangeActivity(economyID : Int, economyName : String, economyGoal : Double)
}