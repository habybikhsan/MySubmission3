package com.example.mysubmission3.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.example.mysubmission3.db.UserFavorite
import com.example.mysubmission3.db.UserFavoriteDao
import com.example.mysubmission3.db.UserFavoriteRoomDatabase
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class UserFavoriteRepository (application: Application) {
    private val mUserFavoriteDao: UserFavoriteDao
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()
    init {
        val db = UserFavoriteRoomDatabase.getDatabase(application)
        mUserFavoriteDao = db.userFavoriteDao()
    }
    fun getAllNotes(): LiveData<List<UserFavorite>> = mUserFavoriteDao.getAllNotes()
    fun insert(userFavorite: UserFavorite) {
        executorService.execute { mUserFavoriteDao.insert(userFavorite) }
    }
    fun delete(userFavorite: UserFavorite) {
        executorService.execute { mUserFavoriteDao.delete(userFavorite) }
    }
    fun update(userFavorite: UserFavorite) {
        executorService.execute { mUserFavoriteDao.update(userFavorite) }
    }
}