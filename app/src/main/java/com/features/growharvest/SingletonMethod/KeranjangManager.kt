package com.features.growharvest.SingletonMethod

import com.features.growharvest.Transaksi.Produk

object KeranjangManager {
    data class KeranjangItem(var produk: Produk?, var jumlahPesan: Int)
    private val daftarBelanja = mutableListOf<KeranjangItem>()

    fun tambahItem(produk: Produk?, jumlahPesan: Int) {
        // Cek apakah produk sudah ada di keranjang
        val existingItem = daftarBelanja.find { it.produk?.id_produk == produk?.id_produk }

        if (existingItem != null) {
            existingItem.jumlahPesan += jumlahPesan
//            existingItem.totalHarga = existingItem.produk?.harga_produk?.times(existingItem.jumlahPesan) ?: 0
        } else {
            // Tambahkan item baru ke keranjang
            val item = KeranjangItem(produk, jumlahPesan)
            daftarBelanja.add(item)
        }
    }

    fun hapusItem(item: KeranjangItem) {
        daftarBelanja.remove(item)
    }

    fun updateStokProduk(idProduk: String, newStok: Int){
        val existingItem = daftarBelanja.find {it.produk?.id_produk == idProduk}
        existingItem?.produk?.stok_produk = newStok
    }

    fun getDaftarBelanja(): List<KeranjangItem> {
        return daftarBelanja.toList()
    }

    fun clearDaftarBelanja() {
        daftarBelanja.clear()
    }
}