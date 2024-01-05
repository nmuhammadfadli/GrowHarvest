package com.features.growharvest.Transaksi

import android.os.Parcel
import android.os.Parcelable


data class KeranjangItem(
    val produk: String,
    val jumlahPesan: Int,
    // ... (lainnya sesuai kebutuhan)
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readInt(),
        // ... (baca data lainnya dari parcel)
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(produk)
        parcel.writeInt(jumlahPesan)
        // ... (tulis data lainnya ke parcel)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<KeranjangItem> {
        override fun createFromParcel(parcel: Parcel): KeranjangItem {
            return KeranjangItem(parcel)
        }

        override fun newArray(size: Int): Array<KeranjangItem?> {
            return arrayOfNulls(size)
        }
    }
}
