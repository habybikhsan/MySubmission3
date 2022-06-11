package com.example.mysubmission3.helper

import android.database.Cursor
import com.example.mysubmission3.Data.UserFavorite
import java.util.ArrayList

object MappingHelper {
    fun mapCursorToArrayList(notesCursor: Cursor?): ArrayList<UserFavorite> {
        val favoriteList = ArrayList<UserFavorite>()
        notesCursor?.apply {
            while (moveToNext()) {
                val username = getString(getColumnIndexOrThrow(DatabaseContractFavorite.FavoriteColumns.USERNAME))
                val name = getString(getColumnIndexOrThrow(DatabaseContractFavorite.FavoriteColumns.NAME))
                val avatar = getString(getColumnIndexOrThrow(DatabaseContractFavorite.FavoriteColumns.AVATAR))
                val company = getString(getColumnIndexOrThrow(DatabaseContractFavorite.FavoriteColumns.COMPANY))
                val location = getString(getColumnIndexOrThrow(DatabaseContractFavorite.FavoriteColumns.LOCATION))
                val repository = getInt(getColumnIndexOrThrow(DatabaseContractFavorite.FavoriteColumns.REPOSITORY))
                val followers = getInt(getColumnIndexOrThrow(DatabaseContractFavorite.FavoriteColumns.FOLLOWERS))
                val following = getInt(getColumnIndexOrThrow(DatabaseContractFavorite.FavoriteColumns.FOLLOWING))
                val favourite =
                    getString(getColumnIndexOrThrow(DatabaseContractFavorite.FavoriteColumns.FAVORITE))
                favoriteList.add(
                    UserFavorite(
                        username,
                        name,
                        avatar,
                        company,
                        location,
                        repository,
                        followers,
                        following,
                        favourite
                    )
                )
            }
        }
        return favoriteList
    }

}