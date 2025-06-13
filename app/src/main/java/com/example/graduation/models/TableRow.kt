package com.example.graduation.models
import android.os.Parcel
import android.os.Parcelable

data class TableRow(val levelCm: Double, val percentage: Double, val volumeM3: Double, val massT: Double) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readDouble(),
        parcel.readDouble(),
        parcel.readDouble(),
        parcel.readDouble()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeDouble(levelCm)
        parcel.writeDouble(percentage)
        parcel.writeDouble(volumeM3)
        parcel.writeDouble(massT)
    }

    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<TableRow> {
        override fun createFromParcel(parcel: Parcel): TableRow = TableRow(parcel)
        override fun newArray(size: Int): Array<TableRow?> = arrayOfNulls(size)
    }
}