package com.example.mysubmission3.db.Data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
@Parcelize
data class UserDetail(
    var username: String? = null,
    var avatar: String? = null,
    var name: String? = null,
    var company: String? = null,
    var location: String? = null,
    var repository: Int? = null,
    var followers: Int? = null,
    var following: Int? = null
): Parcelable