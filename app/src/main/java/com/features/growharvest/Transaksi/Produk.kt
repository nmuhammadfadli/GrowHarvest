package com.features.growharvest.Transaksi

import android.os.Parcel
import android.os.Parcelable

data class Produk(
    val id_produk: String,
    val nama_produk: String,
    val harga_produk: Int,
    var stok_produk: Int,
    val deskripsi: String,
    val gambar_produk: String
) : Parcelable {
    // Konstruktor untuk Parcelable
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString() ?: "",
        parcel.readString() ?: ""
    )

    // Override untuk Parcelable
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id_produk)
        parcel.writeString(nama_produk)
        parcel.writeInt(harga_produk)
        parcel.writeInt(stok_produk)
        parcel.writeString(deskripsi)
        parcel.writeString(gambar_produk)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Produk> {
        override fun createFromParcel(parcel: Parcel): Produk {
            return Produk(parcel)
        }

        override fun newArray(size: Int): Array<Produk?> {
            return arrayOfNulls(size)
        }
    }
}

