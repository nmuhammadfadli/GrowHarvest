package com.features.growharvest.LoginActivity

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.features.growharvest.R
import com.features.growharvest.Sementara.HomeActivity
import org.json.JSONArray
import org.json.JSONObject

class DataAkun : AppCompatActivity() {
    private lateinit var textUsername: TextView
    private lateinit var textIdUser: TextView
    private lateinit var textNamaUser: TextView
    private lateinit var textTelp: TextView
    private lateinit var textAlamat: TextView
    private lateinit var btnLogout : ImageButton
    private lateinit var btnKembali : Button

    private lateinit var requestQueue: RequestQueue

    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_data_akun)

        // Inisialisasi elemen UI
        textUsername = findViewById(R.id.txtUsername)
        btnKembali = findViewById(R.id.btnBackHome3)
        textIdUser = findViewById(R.id.textView9)
        btnLogout = findViewById(R.id.imgbtnLogout)
        textNamaUser = findViewById(R.id.textView10)
        textTelp = findViewById(R.id.textView11)
        textAlamat = findViewById(R.id.textViewAlamat)

        // Inisialisasi antrian permintaan Volley
        requestQueue = Volley.newRequestQueue(this)

        // Inisialisasi SharedPreferences
        sharedPreferences = getSharedPreferences("user_session", Context.MODE_PRIVATE)

        btnLogout.setOnClickListener {
            logout()
        }
        btnKembali.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            finish()
        }

        // Mendapatkan username dari SharedPreferences
        val username = getUsernameFromSharedPreferences()
        fetchDataFromApi(username)
    }

    private fun fetchDataFromApi(username: String) {
        val url = "https://growharvest.my.id/API/login.php?nama_pengguna=$username"

        val jsonArrayRequest = JsonArrayRequest(Request.Method.GET, url, null,
            Response.Listener { response ->
                // Handle response here
                try {
                    if (response.length() > 0) {
                        // Ambil objek pertama dari array
                        val responseObject = response.getJSONObject(0)
                        parseApiResponse(responseObject)
                        saveUserIdToSharedPreferences(responseObject)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            },
            Response.ErrorListener { error ->
                // Handle error here
                error.printStackTrace()
            })

        requestQueue.add(jsonArrayRequest)
    }

    private fun parseApiResponse(response: JSONObject) {
        // Parsing data JSON dari respons API sesuai kebutuhan Anda
        // Contoh:
        try {
            val userId = response.getString("id_akun")
            val userName = response.getString("nama_pengguna")
            val userFullName = response.getString("nama_lengkap")
            val userPhone = response.getString("no_hp")
            //            val userSandi = response.getString("kata_sandi")
            //            val userGambar = response.getString("gambar")
            //            val userHakAkses = response.getString("tingkat_akses")
            val userAddress = response.getString("alamat")

            // Set text pada TextView sesuai data yang didapatkan
            textUsername.text = userName
            textIdUser.text = userId
            textNamaUser.text = userFullName
            textTelp.text = userPhone
            textAlamat.text = userAddress
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    private fun saveUserIdToSharedPreferences(userData: JSONObject) {
        val userId = userData.getString("id_akun")
        with(sharedPreferences.edit()) {
            putString("id_akun", userId)
            apply()
        }
    }
    private fun logout() {
        // Pastikan sharedPreferences telah diinisialisasi sebelum digunakan
        if (!::sharedPreferences.isInitialized) {
            sharedPreferences = getSharedPreferences("user_session", Context.MODE_PRIVATE)
        }

        // Buat dialog konfirmasi
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Konfirmasi Logout")
            .setMessage("Apakah anda ingin logout?")
            .setPositiveButton("Ya") { dialogInterface: DialogInterface, i: Int ->
                with(sharedPreferences.edit()) {
                    clear()
                    apply()
                }

                val intent = Intent(this, LoginMainActivity::class.java)
                startActivity(intent)
                finish()
            }
            .setNegativeButton("Tidak") { dialogInterface: DialogInterface, i: Int ->
                dialogInterface.dismiss()
            }
        builder.show()
    }


    private fun getUsernameFromSharedPreferences(): String {
        val sharedPreferences = getSharedPreferences("user_session", Context.MODE_PRIVATE)
        return sharedPreferences.getString("username", "") ?: ""
    }
}
