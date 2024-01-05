package com.features.growharvest.Transaksi

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.features.growharvest.R

class ProdukAdapter(
    private val context: Context,
    private var produkList: List<Produk>,
    private val onItemClick: (Int) -> Unit
) : RecyclerView.Adapter<ProdukAdapter.ProdukViewHolder>(), Filterable {

    private var produkListFull: List<Produk> = ArrayList(produkList)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProdukViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_produk, parent, false)
        return ProdukViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProdukViewHolder, position: Int) {
        val produk = produkList[position]

        holder.idTextView.text = produk.id_produk
        holder.namaTextView.text = produk.nama_produk
        holder.hargaTextView.text = "Harga: ${produk.harga_produk}"
        holder.stokTextView.text = "Stok: ${produk.stok_produk}"

        // Gunakan library Glide untuk memuat gambar dari URL
        Glide.with(context)
            .load(produk.gambar_produk)
            .placeholder(R.drawable.placeholder_image) // Placeholder image jika gambar tidak dapat dimuat
            .error(R.drawable.iconerrorimage) // Image yang ditampilkan jika terjadi kesalahan
            .into(holder.gambarImageView)

        holder.itemView.setOnClickListener {
            onItemClick.invoke(position) // Panggil listener saat item diklik
        }
    }

    override fun getItemCount(): Int {
        return produkList.size
    }


    inner class ProdukViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        val idTextView : TextView = itemView.findViewById(R.id.productId)
        val namaTextView: TextView = itemView.findViewById(R.id.productName)
        val hargaTextView: TextView = itemView.findViewById(R.id.productPrice)
        val stokTextView: TextView = itemView.findViewById(R.id.productStock)
        val gambarImageView: ImageView = itemView.findViewById(R.id.productImage)
        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(view: View?) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {

            }
        }
    }


    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val filteredList = ArrayList<Produk>()
                if (constraint.isNullOrBlank()) {
                    filteredList.addAll(produkListFull)
                } else {
                    val filterPattern = constraint.toString().toLowerCase().trim()
                    for (produk in produkListFull) {
                        if (produk.id_produk.toLowerCase().contains(filterPattern) ||
                            produk.nama_produk.toLowerCase().contains(filterPattern)
                        ) {
                            filteredList.add(produk)
                        }
                    }
                }
                val results = FilterResults()
                results.values = filteredList
                return results
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                produkList = results?.values as List<Produk>
                notifyDataSetChanged()
            }
        }
    }
    fun updateData(newProdukList: List<Produk>) {
        produkList = newProdukList
        produkListFull = ArrayList(newProdukList)
        notifyDataSetChanged()
    }
}

