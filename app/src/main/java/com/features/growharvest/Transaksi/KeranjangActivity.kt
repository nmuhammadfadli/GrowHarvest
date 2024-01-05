package com.features.growharvest.Transaksi

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.features.growharvest.R
import com.features.growharvest.Sementara.HomeActivity
import com.features.growharvest.SingletonMethod.KeranjangManager
import java.text.SimpleDateFormat
import java.util.*

class KeranjangActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var keranjangAdapter: KeranjangAdapter
    private lateinit var btnToDaftarProdukActivity: ImageButton
    private lateinit var btnEditProduk: ImageButton
    private lateinit var txtGrandTotal: TextView
    private lateinit var btnHapus: Button
    private lateinit var txtKembalian : EditText
    private lateinit var btnSelesai: Button

    private lateinit var btnBackHome: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transaksi_keranjang)

        txtGrandTotal = findViewById(R.id.txtGrandTotal)
        btnToDaftarProdukActivity = findViewById(R.id.btnAddProduct)
        btnEditProduk = findViewById(R.id.btnEditProduct)
        txtKembalian = findViewById(R.id.kembalian)
        txtKembalian.isEnabled = false
        btnHapus = findViewById(R.id.btnHapus)
        btnSelesai = findViewById(R.id.btnSelesai)
        btnBackHome = findViewById(R.id.btnBackHome)
        recyclerView = findViewById(R.id.keranjangRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        itemTouchHelper.attachToRecyclerView(recyclerView)


            // Mendapatkan data dari Singleton method
            val daftarKeranjang = KeranjangManager.getDaftarBelanja()
            setupRecyclerView(daftarKeranjang)

            // Ambil adapter dari RecyclerView Anda
            val adapter = recyclerView.adapter as KeranjangAdapter

            // Set teks pada TextView dengan Grand Total
            txtGrandTotal.text = "${adapter.getGrandTotal()}"

            btnToDaftarProdukActivity.setOnClickListener {
                val intent = Intent(this, DaftarProdukActivity::class.java)
                startActivity(intent)
            }
            btnBackHome.setOnClickListener {
                val intent = Intent(this, HomeActivity::class.java)
                startActivity(intent)
                finish()
            }
            btnEditProduk.setOnClickListener {
                val idProduk = "id_" // Gantilah dengan implementasi yang sesuai
            }
            btnHapus.setOnClickListener {
                clearKeranjang()
            }
        btnSelesai.setOnClickListener {
            val grandTotal = adapter.getGrandTotal()

            val totalBayarEditText = findViewById<EditText>(R.id.totalBayar)
            val totalBayar = totalBayarEditText.text.toString()

            if (totalBayar.toIntOrNull() == null) {

                showToast("Total bayar harus berupa angka.")
            } else if (totalBayar.toInt() < grandTotal) {

                showToast("Total bayar kurang dari Grand Total. Harap periksa kembali.")
            } else {
                val kembalian = totalBayar.toInt() - grandTotal
                txtKembalian.setText(kembalian.toString())

                sendTransactionToServer(daftarKeranjang, grandTotal.toString())
                showToast("Data Berhasil Disimpan!")
                clearKeranjang()
            }
        }


    }
    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun setupRecyclerView(daftarKeranjang: List<KeranjangManager.KeranjangItem>) {
        keranjangAdapter = KeranjangAdapter(this, daftarKeranjang)
        recyclerView.adapter = keranjangAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)
    }

    private fun sendTransactionToServer(keranjangList: List<KeranjangManager.KeranjangItem>, grandTotal: String) {
        try {
            // Mendapatkan detail transaksi dari adapter
            val details = keranjangList.map {
                TransactionDetail("", it.jumlahPesan.toString(), it.produk?.id_produk ?: "")
            }

            val totalBayarEditText = findViewById<EditText>(R.id.totalBayar)
            val totalBayar = totalBayarEditText.text.toString()

            val kembalianEditText = findViewById<EditText>(R.id.kembalian)
            val kembalian = kembalianEditText.text.toString()

            TransactionManager(this).sendTransaction(getUsernameFromSharedPreferences(), grandTotal, totalBayar, kembalian)


            // Tambahkan log jika diperlukan
            Log.d("KeranjangActivity", "Transaksi berhasil dikirim ke server.")

        } catch (e: Exception) {
            Log.e("KeranjangActivity", "Error in sendTransactionToServer: ${e.message}", e)
            // Handle error here, mungkin dengan menampilkan pesan kepada pengguna
        }
    }


    private val itemTouchHelper by lazy {
        val simpleItemTouchCallback = object : ItemTouchHelper.SimpleCallback(
            0, // dragDirs
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT // swipeDirs
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder

            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                // Panggil method removeItem dari adapter
                keranjangAdapter.removeItem(viewHolder.adapterPosition)
                keranjangAdapter.clearKeranjang()
                KeranjangManager.clearDaftarBelanja()
                keranjangAdapter.notifyDataSetChanged()
                txtGrandTotal.text = "0"
            }
        }
        ItemTouchHelper(simpleItemTouchCallback)
    }

    private fun clearKeranjang() {
        keranjangAdapter.clearKeranjang()
        KeranjangManager.clearDaftarBelanja()
        keranjangAdapter.notifyDataSetChanged()
        txtGrandTotal.text = "0"

    }


    private fun getUserIdFromSharedPreferences(): String {

        return "US007"
    }
    private fun getUsernameFromSharedPreferences(): String {
        val sharedPreferences = getSharedPreferences("user_session", Context.MODE_PRIVATE)
        return sharedPreferences.getString("id_akun", "") ?: ""
    }
}
