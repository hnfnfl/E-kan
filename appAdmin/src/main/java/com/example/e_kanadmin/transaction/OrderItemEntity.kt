package com.example.e_kanadmin.transaction

import android.os.Parcel
import android.os.Parcelable

class OrderItemEntity(
    var idproduk: String? = "",
    var produk: String? = "",
    var jumlah: String? = "",
    var harga: String? = "",
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(idproduk)
        parcel.writeString(produk)
        parcel.writeString(jumlah)
        parcel.writeString(harga)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<OrderItemEntity> {
        override fun createFromParcel(parcel: Parcel): OrderItemEntity {
            return OrderItemEntity(parcel)
        }

        override fun newArray(size: Int): Array<OrderItemEntity?> {
            return arrayOfNulls(size)
        }
    }
}