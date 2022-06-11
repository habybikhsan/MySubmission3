package com.example.mysubmission3.helper

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mysubmission3.ui.ViewModel.MenuViewModel
import com.example.mysubmission3.ui.ViewModel.SettingPreferences
import java.lang.IllegalArgumentException

class ViewModelFactory(private val pref : SettingPreferences) : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MenuViewModel::class.java)){
            return MenuViewModel(pref) as T
        }
        throw IllegalArgumentException("Unknown com.example.mysubmission3.ViewModel Class: " + modelClass.name)
    }
}