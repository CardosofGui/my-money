package com.example.mymoney.framework

import android.app.Application
import com.example.mymoney.data.EconomyDatabase
import com.example.mymoney.data.EconomyRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class BaseApplication : Application() {


    val applicationScope = CoroutineScope(SupervisorJob())

    val database by lazy { EconomyDatabase.getDatabase(this, applicationScope) }
    val repository by lazy { EconomyRepository(database.economyDAO()) }

}