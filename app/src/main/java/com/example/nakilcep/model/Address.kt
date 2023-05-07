package com.example.nakilcep.model
// parcelable
import android.os.Parcel
import android.os.Parcelable
import kotlinx.parcelize.Parcelize


data class Address(
    val documentID: String?,
    val curentUser: String?,
    val authorizedName: String?,
    val authorizedPhone: String?,
    val adressTitle: String?,
    val ProvinceName: String?,
    val districtName: String?,
    val neighbourhoodName: String?,
    val streetName: String?,
    val aparmentNo: String?,
    val doorNo: String?,
    val openAddress: String?
)