package com.example.mymoney.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.mymoney.domain.Economy
import com.example.mymoney.domain.Statement
import com.example.mymoney.framework.DAO
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Database(entities = arrayOf(Economy::class, Statement::class), version = 1, exportSchema = false)

public abstract class EconomyDatabase : RoomDatabase() {

    abstract fun economyDAO() : DAO

    companion object{

        @Volatile
        private var INSTANCE : EconomyDatabase? = null

        fun getDatabase(
            context: Context,
            scope: CoroutineScope
        ) : EconomyDatabase{
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    EconomyDatabase::class.java,
                    "economy_database"
                ).build()

                INSTANCE = instance
                instance
            }
        }
    }
}