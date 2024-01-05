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
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.features.growharvest.R
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

class PemasukanFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var transactionAdapter: TransactionAdapter
    private lateinit var searchView: SearchView
    private lateinit var totalHargaTextView: TextView
    private var transactions = emptyList<Transaction>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_pemasukan, container, false)

        searchView = view.findViewById(R.id.btnsearch)
        recyclerView = view.findViewById(R.id.rv_riwayat)
        totalHargaTextView = view.findViewById(R.id.ttlPenjualan)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        transactionAdapter = TransactionAdapter(emptyList()) {

        }
        recyclerView.adapter = transactionAdapter

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
                Log.d("RiwayatTransaksi", "Search Query: $newText")
                filterTransactionsByDate(newText.orEmpty())
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
            requireContext(),
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
        val url = "https://growharvest.my.id/API/get_transaksi.php"

        val request = JsonObjectRequest(
            Request.Method.GET, url, null,
            { response ->
                try {
                    transactions = parseJsonResponse(response)
                    showDataInRecyclerView()
                    Log.d("RiwayatTransaksi", "JSON Response: $response")

                } catch (e: Exception) {
                    Log.e("RiwayatTransaksi", "Error parsing JSON response: ${e.message}", e)
                }
            },
            { error ->
                Log.e("RiwayatTransaksi", "Volley error: ${error.message}", error)
            })

        Volley.newRequestQueue(requireContext()).add(request)

        // Log to check if the request is being made
        Log.d("RiwayatTransaksi", "API Request initiated.")
    }

    private fun parseJsonResponse(response: JSONObject): List<Transaction> {
        val transactions = mutableListOf<Transaction>()

        if (response.optString("status") == "success") {
            val dataArray = response.optJSONArray("data")
            for (i in 0 until dataArray.length()) {
                val transactionJson = dataArray.optJSONObject(i)
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

    private fun showDataInRecyclerView() {
        Log.d("RiwayatTransaksi", "Total Transactions before filtering: ${transactions.size}")

        transactionAdapter.updateData(transactions)

        // Set total price on the desired TextView
        totalHargaTextView.text = "Laba : Rp ${transactionAdapter.calculateTotalHarga()}"

        // Log to check if data is successfully received and updated
        Log.d("RiwayatTransaksi", "Total Transactions after filtering: ${transactionAdapter.itemCount}")
    }

    private fun filterTransactionsByDate(query: String) {
        // Logika untuk melakukan filtering transaksi sesuai dengan tanggal
        val filteredTransactions = if (!query.isNullOrBlank()) {
            transactions.filter {
                it.tanggal_transaksi.contains(query, ignoreCase = true)
            }
        } else {
            transactions
        }
        transactionAdapter.updateData(filteredTransactions)

        // Update total harga setelah filtering
        totalHargaTextView.text = "Laba : Rp ${transactionAdapter.calculateTotalHarga()}"
    }
}


