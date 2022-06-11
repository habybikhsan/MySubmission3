package com.example.mysubmission3.helper

import androidx.recyclerview.widget.DiffUtil
import com.example.mysubmission3.db.UserFavorite

class NoteDiffCallback(private val mOldNoteList: List<UserFavorite>, private val mNewNoteList: List<UserFavorite>) : DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return mOldNoteList.size
    }

    override fun getNewListSize(): Int {
        return mNewNoteList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return mOldNoteList[oldItemPosition].id == mNewNoteList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldEmployee = mOldNoteList[oldItemPosition]
        val newEmployee = mNewNoteList[newItemPosition]
        return oldEmployee.username == newEmployee.username && oldEmployee.location == newEmployee.location
    }
}