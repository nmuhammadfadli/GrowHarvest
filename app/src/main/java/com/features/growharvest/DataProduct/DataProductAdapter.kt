package com.features.growharvest.DataProduct

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.features.growharvest.R

class DataProductAdapter(private val context: Context, private var productList: List<DataProduct>) :
    RecyclerView.Adapter<DataProductAdapter.ViewHolder>() {

    private var productListFull: List<DataProduct> = ArrayList(productList)

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val productId: TextView = itemView.findViewById(R.id.productId)
        val productName: TextView = itemView.findViewById(R.id.productName)
        val productPrice: TextView = itemView.findViewById(R.id.productPrice)
        val productImage: ImageView = itemView.findViewById(R.id.productImage)
        val productStock: TextView = itemView.findViewById(R.id.productStock)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(context).inflate(R.layout.item_produk, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val product = productList[position]

        holder.productId.text = product.id_produk
        holder.productName.text = product.nama_produk
        holder.productPrice.text = "Rp ${product.harga_produk}"
        holder.productStock.text = "Stok: ${product.stok_produk}"
        Glide.with(context)
            .load(product.gambar_produk)
            .into(holder.productImage)
    }

    override fun getItemCount(): Int {
        return productList.size
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

    fun updateData(newList: List<DataProduct>) {
        productList = newList
        productListFull = ArrayList(newList)
        notifyDataSetChanged()
    }
}
