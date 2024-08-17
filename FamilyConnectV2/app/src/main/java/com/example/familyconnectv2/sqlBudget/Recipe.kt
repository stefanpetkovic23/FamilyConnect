package com.example.familyconnectv2.sqlBudget

import android.os.Parcel
import android.os.Parcelable

data class Recipe(
    val image: String = "",
    val ingredients: String = "",
    val naziv: String = "",
    val nutrition: String = "",
    val prep_time: String = "",
    val priprema: String = "",
    val servings: Int = 0,
    val tezina: String = ""
) : Parcelable {
    constructor() : this("", "", "", "", "", "", 0, "")

    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readInt(),
        parcel.readString() ?: ""
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(image)
        parcel.writeString(ingredients)
        parcel.writeString(naziv)
        parcel.writeString(nutrition)
        parcel.writeString(prep_time)
        parcel.writeString(priprema)
        parcel.writeInt(servings)
        parcel.writeString(tezina)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Recipe> {
        override fun createFromParcel(parcel: Parcel): Recipe {
            return Recipe(parcel)
        }

        override fun newArray(size: Int): Array<Recipe?> {
            return arrayOfNulls(size)
        }
    }
}
