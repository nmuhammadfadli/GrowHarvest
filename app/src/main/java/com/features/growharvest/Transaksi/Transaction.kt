package com.features.growharvest.Transaksi

data class Transaction(
    val id_transaksi: String,
    val tanggal_transaksi: String,
    val total_harga: String,
    val untung : String,
    val jml_beli : String
)