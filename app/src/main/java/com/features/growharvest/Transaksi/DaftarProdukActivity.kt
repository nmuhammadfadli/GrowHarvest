package com.features.growharvest.Transaksi

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.features.growharvest.DetailProduk.DetailProdukActivity
import com.features.growharvest.R
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class DaftarProdukActivity : AppCompatActivity() {

    private val produkList = ArrayList<Produk>()
    private lateinit var recyclerView: RecyclerView
    private lateinit var produkAdapter: ProdukAdapter


    private lateinit var requestQueue: RequestQueue

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_daftar_produk)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        produkAdapter = ProdukAdapter(this, produkList) { position -> onProdukClick(position)}

        recyclerView.adapter = produkAdapter

        fetchDataFromApi()

    }




    private fun fetchDataFromApi() {
        val apiUrl = "https://growharvest.my.id/API/produk.php"

        // Menggunakan Volley untuk mengambil data JSON
        val requestQueue = Volley.newRequestQueue(this)

        val jsonArrayRequest = JsonArrayRequest(
            Request.Method.GET, apiUrl, null,
            Response.Listener { response ->
                // Mengambil data produk dari JSON response
                try {
                    for (i in 0 until response.length()) {
                        val productJson = response.getJSONObject(i)
                        val product = Produk(
                            productJson.getString("id_produk"),
                            productJson.getString("nama_produk"),
                            productJson.getInt("harga_produk"),
                            productJson.getInt("stok_produk"),
                            productJson.getString("deskripsi"),
                            productJson.getString("gambar_produk")
                        )
                        produkList.add(product)
                    }
                    // Memberitahu adapter bahwa dataset telah berubah
                    produkAdapter.notifyDataSetChanged()
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            },
            Response.ErrorListener { error ->
                // Menangani kesalahan
                error.printStackTrace()
                if (error is VolleyError) {
                    val errorMessage = error.message
                    Log.e("VolleyError", errorMessage ?: "Unknown VolleyError")
                }
            }
        )
        // Menambahkan request ke queue
        requestQueue.add(jsonArrayRequest)
    }

    private fun onProdukClick(position: Int) {
        val produk = produkList[position]

        val intent = Intent(this, DetailProdukActivity::class.java)
        intent.putExtra("produk", produk)
        startActivity(intent)
    }
}


