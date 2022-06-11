package com.example.mysubmission3.Data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User(
    var username: String? = null,
    var avatar: String? = null,
    var id: String? = null,
    var type: String? = null,
): Parcelable