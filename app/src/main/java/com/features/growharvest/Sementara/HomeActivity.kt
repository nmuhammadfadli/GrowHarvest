package com.features.growharvest.Sementara

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.features.growharvest.LoginActivity.DataAkun
import com.features.growharvest.R
import com.features.growharvest.Transaksi.KeranjangActivity
import com.features.growharvest.Transaksi.ReportActivity
import com.features.growharvest.Transaksi.RiwayatTransaksi

class HomeActivity : AppCompatActivity() {
    private lateinit var btnTransaksi: ImageButton
    private lateinit var btnAccount: ImageButton
    private lateinit var btnHistory : ImageButton
    private lateinit var btnProduct: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_home)

        btnAccount = findViewById(R.id.btnAccount)
        btnTransaksi = findViewById(R.id.btnTransaksi)
        btnProduct = findViewById(R.id.btnProduct)
        btnHistory = findViewById(R.id.btnHistory)

        val username = getUsernameFromSharedPreferences()

        // Tampilkan nama pengguna pada UI
        val txtUsername: TextView = findViewById(R.id.txt_session)
        txtUsername.text = "Selamat Datang, $username!"

        // Uncomment kode berikut jika sudah ada AccountActivity
         btnAccount.setOnClickListener{
             val intent = Intent(this, DataAkun::class.java)
             startActivity(intent)
         }
        btnHistory.setOnClickListener{
            val intent = Intent(this, ReportActivity::class.java)
            startActivity(intent)
        }

        btnProduct.setOnClickListener{
            val intent = Intent(this, DataProductActivity::class.java)
            startActivity(intent)
        }

        btnTransaksi.setOnClickListener {
            val intent = Intent(this, KeranjangActivity::class.java)
            startActivity(intent)
        }
    }

    private fun getUsernameFromSharedPreferences(): String {
        val sharedPreferences = getSharedPreferences("user_session", Context.MODE_PRIVATE)
        return sharedPreferences.getString("username", "") ?: ""
    }
}
