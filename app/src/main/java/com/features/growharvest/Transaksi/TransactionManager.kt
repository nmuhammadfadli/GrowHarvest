package com.features.growharvest.Transaksi

import android.content.Context
import android.util.Log
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.features.growharvest.SingletonMethod.KeranjangManager
import org.json.JSONArray
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

class TransactionManager(private val context: Context) {

    private val requestQueue: RequestQueue by lazy {
        Volley.newRequestQueue(context.applicationContext)
    }

    fun sendTransaction(idAkun: String, totalHarga: String, totalBayar: String, kembalian: String) {
        try {
            // Mendapatkan data dari Singleton method
            val daftarKeranjang = KeranjangManager.getDaftarBelanja()

            // Buat objek JSONObject sesuai format yang diinginkan
            val transactionObject = JSONObject()
            transactionObject.put("id_transaksi", "")
            transactionObject.put("tanggal_transaksi", getCurrentDateTime())
            transactionObject.put("total_harga", totalHarga)
            transactionObject.put("bayar",totalBayar)
            transactionObject.put("kembalian",kembalian)
            transactionObject.put("id_akun", idAkun)

            // Buat objek JSONArray untuk menyimpan detail transaksi
            val detailsArray = JSONArray()
            for (detail in daftarKeranjang) {
                val detailObject = JSONObject()
                detailObject.put("id_detail_transaksi", "")
                detailObject.put("jumlah", detail.jumlahPesan)
                detailObject.put("laba","")
                detailObject.put("id_produk", detail.produk?.id_produk ?: "")
                detailsArray.put(detailObject)
            }

            transactionObject.put("details", detailsArray)

            // Kirim data ke server
            val url = "https://growharvest.my.id/API/post_transaksi.php" // Gantilah dengan URL API Anda
            val jsonObjectRequest = JsonObjectRequest(Request.Method.POST, url, transactionObject,
                Response.Listener { response ->
                    // Handle response here
                    // Jika transaksi berhasil, hapus keranjang
                    KeranjangManager.clearDaftarBelanja()
                    Log.d("TransactionManager", "Transaksi berhasil: $response")
                },
                Response.ErrorListener { error ->
                    // Handle error here
                    error.printStackTrace()
                    Log.e("TransactionManager", "Error in sendTransaction: ${error.message}", error)
                })

            requestQueue.add(jsonObjectRequest)

        } catch (e: Exception) {
            Log.e("TransactionManager", "Error in sendTransaction: ${e.message}", e)
            // Handle error here, mungkin dengan menampilkan pesan kepada pengguna
        }
    }

    private fun getCurrentDateTime(): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val currentDate = Date()
        return dateFormat.format(currentDate)
    }
}

data class TransactionDetail(
    val idDetailTransaksi: String,
    val jumlah: String,
    val idProduk: String
)
