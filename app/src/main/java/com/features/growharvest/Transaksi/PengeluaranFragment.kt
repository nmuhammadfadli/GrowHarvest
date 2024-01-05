package com.features.growharvest.Transaksi

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.TextView
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.features.growharvest.R
import org.json.JSONArray
import java.text.SimpleDateFormat
import java.util.*

class PengeluaranFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var pengeluaranAdapter: pengeluaranAdapter
    private lateinit var searchView: SearchView
    private lateinit var totalHargaTextView: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_pengeluaran, container, false)

        searchView = view.findViewById(R.id.btnsearch)
        recyclerView = view.findViewById(R.id.rv_riwayat)
        totalHargaTextView = view.findViewById(R.id.ttlPenjualan)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Inisialisasi adapter sebelum menggunakannya
        pengeluaranAdapter = pengeluaranAdapter(requireContext(), emptyList())
        recyclerView.adapter = pengeluaranAdapter

        setUpSearchView(view)
        fetchDataFromApi()

        return view
    }

    private fun setUpSearchView(view: View) {
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                Log.d("PengeluaranFragment", "Search Query: $newText")
                pengeluaranAdapter.filterProducts(newText.orEmpty())
                return true
            }
        })
    }


    private fun fetchDataFromApi() {
        val url = "https://growharvest.my.id/API/produk.php"

        val request = JsonArrayRequest(
            Request.Method.GET, url, null,
            { response ->
                try {
                    val transactions = parseJsonResponse(response)
                    showDataInRecyclerView(transactions)
                } catch (e: Exception) {
                    Log.e("PengeluaranFragment", "Error parsing JSON response: ${e.message}", e)
                }
            },
            { error ->
                Log.e("PengeluaranFragment", "Volley error: ${error.message}", error)
            })

        Volley.newRequestQueue(requireContext()).add(request)
    }

    private fun parseJsonResponse(response: JSONArray): List<pengeluaranModel> {
        val transactions = mutableListOf<pengeluaranModel>()

        for (i in 0 until response.length()) {
            try {
                val transactionJson = response.optJSONObject(i)
                if (transactionJson != null) {
                    val transaction = pengeluaranModel(
                        transactionJson.optString("id_produk", ""),
                        transactionJson.optString("nama_produk", ""),
                        transactionJson.optInt("harga_beli", 0),
                        transactionJson.optInt("stok_produk", 0)
                    )
                    transactions.add(transaction)
                }
            } catch (e: Exception) {
                Log.e("PengeluaranFragment", "Error parsing JSON object at index $i: ${e.message}", e)
            }
        }

        return transactions
    }

    private fun showDataInRecyclerView(transactions: List<pengeluaranModel>) {
        Log.d("PengeluaranFragment", "Total Transactions before filtering: ${transactions.size}")

        pengeluaranAdapter.updateData(transactions)

        // Set total price on the desired TextView
        totalHargaTextView.text = "Total Modal : Rp ${pengeluaranAdapter.calculateTotalHarga()}"
    }
}


