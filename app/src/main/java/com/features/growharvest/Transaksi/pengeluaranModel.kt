package com.features.growharvest.Transaksi

data class pengeluaranModel(
    val id_produk: String,
    val nama_produk: String,
    val harga_beli: Int,
    var stok_produk: Int,

)
