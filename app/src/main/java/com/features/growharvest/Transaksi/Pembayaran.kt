package com.features.growharvest.Transaksi

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.features.growharvest.R
import com.features.growharvest.SingletonMethod.KeranjangManager
import org.json.JSONArray
import org.json.JSONObject

class Pembayaran : AppCompatActivity() {
    private lateinit var txtGrandTotal: TextView
    private lateinit var txtBayar: TextView
    private lateinit var btnBackToTransaksi: ImageButton
    private lateinit var btnSimpanAPI: ImageButton
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pembayaran)

        // Inisialisasi komponen UI Anda di sini...
        // ...
        txtGrandTotal = findViewById(R.id.txtGrandTotal2)
        btnBackToTransaksi = findViewById(R.id.btnBackTransaksi)
        btnSimpanAPI = findViewById(R.id.btnSimpanAPI)

        val editText = findViewById<EditText>(R.id.txtBayar)
        val userInput = editText.text.toString()


        val receivedKeranjangList: ArrayList<KeranjangItem>? =
            intent.getParcelableArrayListExtra("keranjangList")
        val grandTotal: Int = intent.getIntExtra("grandTotal", 0)


        txtGrandTotal.text = "Total: Rp. ${grandTotal}"

        btnBackToTransaksi.setOnClickListener {
            finish()
        }

        btnSimpanAPI.setOnClickListener {
            postToApi(receivedKeranjangList, grandTotal)
        }
    }

    private fun postToApi(keranjangList: ArrayList<KeranjangItem>?, grandTotal: Int) {
        val url = "https://growharvest.my.id/API/transaksi.php"

        val jsonArray = JSONArray()
        keranjangList?.forEach { item ->
            val jsonObject = JSONObject()
            jsonObject.put("produk", item.produk)
            jsonObject.put("jumlahPesan", item.jumlahPesan)
            // Anda dapat menambahkan field lain sesuai kebutuhan
            jsonArray.put(jsonObject)
        }

        val jsonObject = JSONObject()
        jsonObject.put("grandTotal", grandTotal)
        jsonArray.put(jsonObject)

        val request = JsonArrayRequest(Request.Method.POST, url, jsonArray,
            Response.Listener { response ->
                // Handle response dari server jika sukses
            },
            Response.ErrorListener { error ->
                // Handle error jika terjadi kesalahan
                Toast.makeText(this, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
            })

        // Tambahkan request ke queue
        val requestQueue: RequestQueue = Volley.newRequestQueue(this)
        requestQueue.add(request)
    }
}