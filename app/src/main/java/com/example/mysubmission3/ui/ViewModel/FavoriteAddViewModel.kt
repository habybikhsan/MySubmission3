package com.example.mysubmission3.ui.ViewModel

import android.app.Application
import androidx.lifecycle.ViewModel
import com.example.mysubmission3.db.UserFavorite
import com.example.mysubmission3.repository.UserFavoriteRepository

class FavoriteAddViewModel (application: Application) : ViewModel() {
    private val mUserFavoriteRepository: UserFavoriteRepository = UserFavoriteRepository(application)
    fun insert(userFavorite: UserFavorite) {
        mUserFavoriteRepository.insert(userFavorite)
    }
    fun update(userFavorite: UserFavorite) {
        mUserFavoriteRepository.update(userFavorite)
    }
    fun delete(userFavorite: UserFavorite) {
        mUserFavoriteRepository.delete(userFavorite)
    }
}