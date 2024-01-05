package com.features.growharvest.Transaksi

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.widget.DatePicker
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.features.growharvest.R
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

class RiwayatTransaksi : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var transactionAdapter: TransactionAdapter
    private lateinit var searchView: SearchView
    private lateinit var totalHargaTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_riwayat_transaksi)

        searchView = findViewById(R.id.btnsearch)
        recyclerView = findViewById(R.id.rv_riwayat)
        totalHargaTextView = findViewById(R.id.ttlPenjualan)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Initialize and set up the transaction adapter
        transactionAdapter = TransactionAdapter(emptyList()) {
            // Add actions to perform when an item is clicked, if needed
        }
        recyclerView.adapter = transactionAdapter

        // Set up search functionality
        setUpSearchView()

        // Call the method to fetch data from API
        fetchDataFromApi()
    }

    private fun setUpSearchView() {
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                Log.d("RiwayatTransaksi", "Search Query: $newText")
                transactionAdapter.filterTransactionsByDate(newText.orEmpty())
                return true
            }
        })

        searchView.setOnSearchClickListener {
            showDatePickerDialog()
        }
    }


    private fun showDatePickerDialog() {
        val currentDate = Calendar.getInstance()
        val year = currentDate.get(Calendar.YEAR)
        val month = currentDate.get(Calendar.MONTH)
        val day = currentDate.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this,
            { _: DatePicker?, selectedYear: Int, selectedMonth: Int, selectedDay: Int ->
                val selectedDate = Calendar.getInstance()
                selectedDate.set(selectedYear, selectedMonth, selectedDay)

                val formattedDate =
                    SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(selectedDate.time)

                // Set the selected date to the SearchView
                searchView.setQuery(formattedDate, false)
            },
            year,
            month,
            day
        )

        datePickerDialog.show()
    }

    private fun fetchDataFromApi() {
        val url = "https://growharvest.my.id/API/cobagettransaksi.php" // Replace with your API URL

        val request = JsonObjectRequest(
            Request.Method.GET, url, null,
            { response ->
                try {
                    val transactions = parseJsonResponse(response)
                    showDataInRecyclerView(transactions)
                } catch (e: Exception) {
                    Log.e("RiwayatTransaksi", "Error parsing JSON response: ${e.message}", e)
                }
            },
            { error ->
                Log.e("RiwayatTransaksi", "Volley error: ${error.message}", error)
            })

        Volley.newRequestQueue(this).add(request)
    }

    private fun parseJsonResponse(response: JSONObject): List<Transaction> {
        val transactions = mutableListOf<Transaction>()

        val keys = response.keys()
        while (keys.hasNext()) {
            val key = keys.next()
            val transactionJson = response.optJSONObject(key)
            if (transactionJson != null) {
                val transaction = Transaction(
                    transactionJson.optString("id_transaksi", ""),
                    transactionJson.optString("tanggal_transaksi", ""),
                    transactionJson.optString("total_harga", ""),
                    transactionJson.optString("untung", ""),
                    transactionJson.optString("jml_beli", "")
                )
                transactions.add(transaction)
            }
        }

        return transactions
    }

    private fun showDataInRecyclerView(transactions: List<Transaction>) {
        Log.d("RiwayatTransaksi", "Total Transactions before filtering: ${transactions.size}")

        transactionAdapter.updateData(transactions)

        // Set total price on the desired TextView
        totalHargaTextView.text = "Laba : Rp ${transactionAdapter.calculateTotalHarga()}"
    }
}


