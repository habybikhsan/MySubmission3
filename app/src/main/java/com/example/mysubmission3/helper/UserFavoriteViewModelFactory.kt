package com.example.mysubmission3.helper

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mysubmission3.ui.ViewModel.FavoriteAddViewModel
import com.example.mysubmission3.ui.ViewModel.MainViewModel

class UserFavoriteViewModelFactory private constructor(private val mApplication: Application) : ViewModelProvider.NewInstanceFactory() {
    companion object {
        @Volatile
        private var INSTANCE: UserFavoriteViewModelFactory? = null
        @JvmStatic
        fun getInstance(application: Application): UserFavoriteViewModelFactory {
            if (INSTANCE == null) {
                synchronized(UserFavoriteViewModelFactory::class.java) {
                    INSTANCE = UserFavoriteViewModelFactory(application)
                }
            }
            return INSTANCE as UserFavoriteViewModelFactory
        }
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(mApplication) as T
        } else if (modelClass.isAssignableFrom(FavoriteAddViewModel::class.java)) {
            return FavoriteAddViewModel(mApplication) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}