package com.features.growharvest.DataProduct

data class DataProduct(
    val id_produk: String,
    val nama_produk: String,
    val harga_produk: Int,
    val stok_produk: Int,
    val deskripsi: String,
    val gambar_produk: String
)