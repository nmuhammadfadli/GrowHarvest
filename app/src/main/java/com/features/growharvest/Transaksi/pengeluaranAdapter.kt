package com.features.growharvest.Transaksi

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.features.growharvest.R

class pengeluaranAdapter(private val context: Context,
                         private var productList: List<pengeluaranModel>
) :
    RecyclerView.Adapter<pengeluaranAdapter.ViewHolder>() {

    private var productListFull: List<pengeluaranModel> = ArrayList(productList)

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val productId: TextView = itemView.findViewById(R.id.productId)
        val productName: TextView = itemView.findViewById(R.id.productName)
        val productPrice: TextView = itemView.findViewById(R.id.productPrice)
        val productStock: TextView = itemView.findViewById(R.id.productStock)
        val productModal: TextView = itemView.findViewById(R.id.productModals)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(context).inflate(R.layout.item_pengeluaran, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val product = productList[position]

        holder.productId.text = "Id Produk: ${product.id_produk}"
        holder.productName.text = "Nama Produk: ${product.nama_produk}"
        holder.productPrice.text = "Harga Beli: Rp${product.harga_beli}"
        holder.productStock.text = "Stok Tersisa: ${product.stok_produk}"

        var totalModal = 0
        try {
            val hargaBeli = product.harga_beli.toInt()
            val stokProduk = product.stok_produk.toInt()

            totalModal = hargaBeli * stokProduk
        } catch (e: NumberFormatException) {
            // Handle the case where harga_beli or stok_produk is not a valid number
            e.printStackTrace()
        }
        holder.productModal.text = "Modal: Rp$totalModal"
    }
    fun filterProducts(query: String?) {
        productList = if (!query.isNullOrBlank()) {
            productListFull.filter {
                it.id_produk.contains(query, ignoreCase = true) ||
                        it.nama_produk.contains(query, ignoreCase = true)
            }
        } else {
            productListFull
        }
        notifyDataSetChanged()
    }



    fun calculateTotalHarga(): String {
        var totalHarga = 0
        for (product in productList) {
            try {
                val hargaBeli = product.harga_beli.toInt()
                val stokProduk = product.stok_produk.toInt()

                totalHarga += hargaBeli * stokProduk
            } catch (e: NumberFormatException) {
                // Handle the case where harga_beli or stok_produk is not a valid number
                e.printStackTrace()
            }
        }
        return totalHarga.toString()
    }

    override fun getItemCount(): Int {
        return productList.size
    }


    fun updateData(newList: List<pengeluaranModel>) {
        productList = newList
        productListFull = ArrayList(newList)
        notifyDataSetChanged()
    }
}

