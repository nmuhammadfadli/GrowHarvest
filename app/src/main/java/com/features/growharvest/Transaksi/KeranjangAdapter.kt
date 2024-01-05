package com.features.growharvest.Transaksi

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.features.growharvest.R
import com.features.growharvest.SingletonMethod.KeranjangManager

class KeranjangAdapter(private val context: Context, private var keranjangList: List<KeranjangManager.KeranjangItem>) :
    RecyclerView.Adapter<KeranjangAdapter.KeranjangViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): KeranjangViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_keranjang, parent, false)
        return KeranjangViewHolder(view)
    }

    override fun onBindViewHolder(holder: KeranjangViewHolder, position: Int) {
        val keranjangItem = keranjangList[position]
        val hargaProduk = keranjangItem.produk?.harga_produk ?: 0
        val totalHargaProduk = hargaProduk * keranjangItem.jumlahPesan


        // Disini Anda dapat mengatur TextViews atau komponen lainnya dengan data dari keranjangItem
        holder.idProdukTextView.text = keranjangItem.produk?.id_produk
        holder.namaProdukTextView.text = keranjangItem.produk?.nama_produk ?: "Produk Tidak Diketahui"
        holder.jumlahProdukTextView.text = "${keranjangItem.jumlahPesan}"
        holder.hargaProdukTextView.text = "Harga: Rp ${keranjangItem.produk?.harga_produk?: "harga tidak diketahui"}"
        holder.totalProdukTextView.text = "Total: Rp $totalHargaProduk"

        holder.itemView.setOnClickListener {
            tampilkanDialogStok(context, keranjangList[position])
        }

    }

    override fun getItemCount(): Int {
        return keranjangList.size
    }

    fun getGrandTotal(): Int {
        var grandTotal = 0
        for (keranjangItem in keranjangList) {
            val hargaProduk = keranjangItem.produk?.harga_produk ?: 0
            grandTotal += hargaProduk * keranjangItem.jumlahPesan
        }
        return grandTotal
    }

    fun updateStok(idProduk: String, newStok: Int){
        KeranjangManager.updateStokProduk(idProduk, newStok)
        notifyDataSetChanged()
    }

    private fun tampilkanDialogStok(context: Context, keranjangItem: KeranjangManager.KeranjangItem) {
        val dialog = Dialog(context)
        dialog.setContentView(R.layout.item_edit_stok)

        val tvStok = dialog.findViewById<TextView>(R.id.tvStok)
        val btnKurang = dialog.findViewById<ImageButton>(R.id.kurangStokButton)
        val btnTambah = dialog.findViewById<ImageButton>(R.id.tambahStokButton)
        val btnSimpan = dialog.findViewById<ImageButton>(R.id.simpanButton)

        tvStok.text = "${keranjangItem.jumlahPesan}"

        btnKurang.setOnClickListener {
            // Kurangi stok jika masih lebih besar dari 0
            if (keranjangItem.jumlahPesan > 1){
                if (keranjangItem.produk?.stok_produk ?: 0 > 0) {
                    keranjangItem.jumlahPesan = keranjangItem.jumlahPesan.minus(1) ?: 0
                    tvStok.text = "${keranjangItem.jumlahPesan}"
                }
            }
            else {
                tvStok.text = "${keranjangItem.jumlahPesan}"
            }

        }

        btnTambah.setOnClickListener {
            // Tambah stok
            val stokTersedia = keranjangItem.produk?.stok_produk ?: 0
            if (stokTersedia > keranjangItem.jumlahPesan) {
                keranjangItem.jumlahPesan = keranjangItem.jumlahPesan.plus(1)
                tvStok.text = "${keranjangItem.jumlahPesan}"
            } else {
                // Tampilkan pesan atau tindakan lain jika stok telah habis
                Toast.makeText(context, "Stok telah habis", Toast.LENGTH_SHORT).show()
            }
        }

        btnSimpan.setOnClickListener {
            dialog.dismiss()
            notifyDataSetChanged() // Refresh adapter setelah perubahan
        }
        dialog.show()
    }
    fun removeItem(position: Int) {
        val newList = keranjangList.toMutableList()
        newList.removeAt(position)
        notifyItemRemoved(position)
        keranjangList = newList
    }

    fun clearKeranjang() {
        keranjangList = emptyList()
        notifyDataSetChanged()
    }

    inner class KeranjangViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val namaProdukTextView: TextView = itemView.findViewById(R.id.isiNama)
        val idProdukTextView : TextView = itemView.findViewById(R.id.isiId)
        val jumlahProdukTextView: TextView = itemView.findViewById(R.id.isiKuantitas)
        val hargaProdukTextView: TextView = itemView.findViewById(R.id.isiHarga)
        val totalProdukTextView: TextView = itemView.findViewById(R.id.isiTotal)
    }
}

