package com.example.mysubmission3.db

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface UserFavoriteDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(userFavorite: UserFavorite)
    @Update
    fun update(userFavorite: UserFavorite)
    @Delete
    fun delete(userFavorite: UserFavorite)
    @Query("SELECT * from userfavorite ORDER BY id ASC")
    fun getAllNotes(): LiveData<List<UserFavorite>>
}