package com.example.myapplication.Room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.myapplication.Pojo.MovieDetailResult



@Database(entities = [MovieDetailResult::class],version = 1)
abstract class Database:RoomDatabase() {
    abstract fun daoInterface():DaoInterface
    companion object
    {
        private @Volatile var instane:com.example.myapplication.Room.Database ?=null
        private  var lock=Any()
        operator fun invoke(context:Context)= instane ?: synchronized(lock)
        {

        }
        private fun buildDataBase(context:Context)
        {
            Room.databaseBuilder(context.applicationContext,)
        }
    }
}