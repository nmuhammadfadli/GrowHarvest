package com.features.growharvest.Transaksi
import android.os.Parcel
import android.os.Parcelable

data class Keranjang(
    var id_produk : String,
    var nama_produk: String,
    var harga_produk: Int,
    var jumlahPesan: Int, // Jumlah produk yang dipilih
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readInt(),
        parcel.readInt()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id_produk)
        parcel.writeString(nama_produk)
        parcel.writeInt(harga_produk)
        parcel.writeInt(jumlahPesan)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Keranjang> {
        override fun createFromParcel(parcel: Parcel): Keranjang {
            return Keranjang(parcel)
        }

        override fun newArray(size: Int): Array<Keranjang?> {
            return arrayOfNulls(size)
        }
    }
}
