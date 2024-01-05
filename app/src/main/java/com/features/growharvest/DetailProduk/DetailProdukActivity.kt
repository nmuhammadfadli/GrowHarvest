package com.features.growharvest.DetailProduk

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.NumberPicker
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.features.growharvest.R
import com.features.growharvest.SingletonMethod.KeranjangManager
import com.features.growharvest.Transaksi.Keranjang
import com.features.growharvest.Transaksi.KeranjangActivity
import com.features.growharvest.Transaksi.Produk
import java.text.NumberFormat
import java.util.Locale

class DetailProdukActivity : AppCompatActivity() {

    private lateinit var imgProduk: ImageView
    private lateinit var txtNamaProduk: TextView
    private lateinit var txtHargaProduk: TextView
    private lateinit var txtStokProduk: TextView
    private lateinit var txtKuantitas: EditText
    private lateinit var txtIdProduk : TextView
    private lateinit var btnTambahKeranjang: ImageButton
    private lateinit var produk: Produk
    private val keranjangProdukList = mutableListOf<Keranjang>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_produk)

        // Inisialisasi view
        imgProduk = findViewById(R.id.imageProduk)
        txtNamaProduk = findViewById(R.id.namaProduk)
        txtIdProduk = findViewById(R.id.idProduk)
        txtHargaProduk = findViewById(R.id.hargaProduk)
        txtStokProduk = findViewById(R.id.stokProduk)
        txtKuantitas = findViewById(R.id.Quantity)
        btnTambahKeranjang = findViewById(R.id.tombolTambahKeranjang)


        // Mendapatkan data produk dari Intent
        val produk: Produk? = intent.getParcelableExtra("produk")

        // Menampilkan data produk pada view
        if (produk != null) {

            Log.d("DetailProdukActivity", "ID Produk: ${produk.id_produk}")
            Log.d("DetailProdukActivity", "Nama Produk: ${produk.nama_produk}")
            Log.d("DetailProdukActivity", "Harga Produk: ${produk.harga_produk}")
            Log.d("DetailProdukActivity", "Stok Produk: ${produk.stok_produk}")
            supportActionBar?.title = produk.nama_produk

            Glide.with(this)
                .load(produk.gambar_produk)
                .into(imgProduk)

            txtIdProduk.text = produk.id_produk
            txtNamaProduk.text = produk.nama_produk
            txtHargaProduk.text = "Rp ${produk.harga_produk}"
            txtStokProduk.text =  "${produk.stok_produk}"
        }


        btnTambahKeranjang.setOnClickListener {
            if (produk != null) {
                tambahItemKeKeranjang(produk)
            }
        }
    }

    private fun tambahItemKeKeranjang(produk: Produk?) {
        val jumlahPesan = txtKuantitas.text.toString().toIntOrNull() ?: 0

        if (jumlahPesan > 0) {
            // Check if the quantity does not exceed the available stock
            if (produk != null && jumlahPesan <= produk.stok_produk) {
                val item = KeranjangManager.KeranjangItem(produk, jumlahPesan)
                KeranjangManager.tambahItem(produk, jumlahPesan)
                Toast.makeText(this, "Produk ditambahkan ke keranjang", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, KeranjangActivity::class.java)
                intent.putExtra("id_produk", produk.id_produk)
                startActivity(intent)
            } else {
                // Show notification if the quantity exceeds the available stock
                Toast.makeText(this, "Jumlah pesanan melebihi stok produk", Toast.LENGTH_SHORT).show()
            }
        } else {
            // Show notification for invalid quantity
            Toast.makeText(this, "Masukkan jumlah yang valid", Toast.LENGTH_SHORT).show()
        }
    }


}
