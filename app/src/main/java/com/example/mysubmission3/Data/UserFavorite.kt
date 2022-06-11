package com.example.mysubmission3.Data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UserFavorite(
    var username: String? = null,
    var name: String? = null,
    var avatar: String? = null,
    var company: String? = null,
    var location: String? = null,
    var repository: Int = 0,
    var followers: Int = 0,
    var following: Int = 0,
    var favorite: String? = null
) : Parcelable
