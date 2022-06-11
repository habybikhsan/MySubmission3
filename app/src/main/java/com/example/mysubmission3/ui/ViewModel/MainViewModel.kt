package com.example.mysubmission3.ui.ViewModel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.mysubmission3.db.UserFavorite
import com.example.mysubmission3.repository.UserFavoriteRepository

class MainViewModel (application: Application) : ViewModel() {
    private val mNoteRepository: UserFavoriteRepository = UserFavoriteRepository(application)
    fun getAllNotes(): LiveData<List<UserFavorite>> = mNoteRepository.getAllNotes()
}