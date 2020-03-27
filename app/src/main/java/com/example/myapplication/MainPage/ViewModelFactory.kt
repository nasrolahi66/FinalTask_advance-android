package com.example.myapplication.MainPage

import android.app.Application
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import java.lang.IllegalArgumentException

class ViewModelFactory(val context: Context):ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        when{
            modelClass.isAssignableFrom(MainPageViewModel::class.java) ->return MainPageViewModel(context) as T
            else ->throw IllegalArgumentException("View Model not provided")
        }
    }
}