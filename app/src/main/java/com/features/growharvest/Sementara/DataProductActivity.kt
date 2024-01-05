package com.features.growharvest.Sementara

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView  // Import SearchView from androidx
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.features.growharvest.DataProduct.DataProduct
import com.features.growharvest.DataProduct.DataProductAdapter
import com.features.growharvest.R
import org.json.JSONException

class DataProductActivity : AppCompatActivity() {

    private val productList = ArrayList<DataProduct>()
    private lateinit var recyclerView: RecyclerView
    private lateinit var dataProductAdapter: DataProductAdapter
    private lateinit var btnBackHome : Button
    private lateinit var searchView: SearchView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_data_produk)

        recyclerView = findViewById(R.id.recyclerView)
        searchView = findViewById(R.id.btnsearch)
        btnBackHome = findViewById(R.id.btnBackHome)

        btnBackHome.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            finish()
        }
        recyclerView.layoutManager = LinearLayoutManager(this)

        dataProductAdapter = DataProductAdapter(this, productList)
        recyclerView.adapter = dataProductAdapter

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
                        val product = DataProduct(
                            productJson.getString("id_produk"),
                            productJson.getString("nama_produk"),
                            productJson.getInt("harga_produk"),
                            productJson.getInt("stok_produk"),
                            productJson.getString("deskripsi"),
                            productJson.getString("gambar_produk")
                        )
                        productList.add(product)
                    }
                    // Memberitahu adapter bahwa dataset telah berubah
                    dataProductAdapter.notifyDataSetChanged()
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

        // Menambahkan listener untuk perubahan teks pada SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterProducts(newText)
                return true
            }
        })
    }

    private fun filterProducts(query: String?) {
        // Logika untuk melakukan filtering produk sesuai dengan query
        val filteredProducts = if (!query.isNullOrBlank()) {
            productList.filter {
                it.id_produk.contains(query, ignoreCase = true) ||
                        it.nama_produk.contains(query, ignoreCase = true)
            }
        } else {
            productList
        }
        dataProductAdapter.updateData(filteredProducts)
    }
}
