package com.features.growharvest.Transaksi

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.features.growharvest.R
import java.util.Locale

class TransactionAdapter(
    private var transactionList: List<Transaction>,
    private val onItemClick: (Transaction) -> Unit
) : RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder>() {

    private var transactionListFull: List<Transaction> = ArrayList(transactionList)
    private var totalHarga: Int = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_transaction, parent, false)
        return TransactionViewHolder(view)
    }

    fun calculateTotalHarga(): String {
        var totalHarga = 0
        for (transaction in transactionList) {
            totalHarga += transaction.untung.toIntOrNull() ?: 0
        }
        return totalHarga.toString()
    }

    private fun updateTotalHarga() {
        totalHarga = transactionList.sumBy { it.untung.toIntOrNull() ?: 0 }
    }

    override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
        val transaction = transactionList[position]

        // Set values to TextViews
        holder.txtTransactionId.text = "ID Transaksi: ${transaction.id_transaksi}"
        holder.txtTransaksiTgl.text = "Tanggal: ${transaction.tanggal_transaksi}"
        holder.txtTransaksiTotal.text = "Total Harga: ${transaction.total_harga}"
        holder.txtTransaksiAkun.text = "Laba: ${transaction.untung}"
        holder.txtJmlBeli.text = "Barang Terjual : ${transaction.jml_beli}"

        // Add onClickListener for each item
        holder.itemView.setOnClickListener {
            onItemClick.invoke(transaction)
        }
    }

    override fun getItemCount(): Int {
        return transactionList.size
    }

    fun updateData(newList: List<Transaction>) {
        transactionList = newList
        updateTotalHarga()
        notifyDataSetChanged()
    }

    fun filterTransactionsByDate(date: String) {
        Log.d("TransactionAdapter", "Search Query: $date")
        val formattedDate = date.trim().toLowerCase(Locale.getDefault())

        val filteredList = if (formattedDate.isNotEmpty()) {
            transactionListFull.filter {
                it.tanggal_transaksi.toLowerCase(Locale.getDefault()).contains(formattedDate)
            }
        } else {
            transactionListFull
        }

        updateData(filteredList)
    }

    // Getter untuk mendapatkan total harga
    fun getTotalHarga(): Int {
        return totalHarga
    }

    class TransactionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtTransactionId: TextView = itemView.findViewById(R.id.txtIdTransaksi)
        val txtTransaksiTgl: TextView = itemView.findViewById(R.id.txtTglTransaksi)
        val txtTransaksiTotal: TextView = itemView.findViewById(R.id.isiTotal)
        val txtTransaksiAkun: TextView = itemView.findViewById(R.id.txtIdBarang)
        val txtJmlBeli: TextView = itemView.findViewById(R.id.txtJmlBeli)
    }
}


