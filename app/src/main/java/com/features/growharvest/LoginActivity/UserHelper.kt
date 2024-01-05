package com.features.growharvest.LoginActivity

import android.content.Context

class UserHelper {fun saveUser(context: Context, user: User) {
    val sharedPref = context.getSharedPreferences("USER_PREF", Context.MODE_PRIVATE)
    val editor = sharedPref.edit()
    editor.putString("id_pengguna", user.id_akun)
    editor.putString("nama_pengguna", user.nama_pengguna)
    editor.putString("kata_sandi", user.kata_sandi)
    editor.apply()
}

    fun getUser(context: Context): User? {
        val sharedPref = context.getSharedPreferences("USER_PREF", Context.MODE_PRIVATE)
        val id_pengguna = sharedPref.getString("id_pengguna", null)
        val nama_pengguna = sharedPref.getString("nama_pengguna", null)
        val password = sharedPref.getString("kata_sandi", null)

        if (id_pengguna != null && nama_pengguna != null && password != null) {
            return User(id_pengguna, nama_pengguna, password)
        }
        return null
    }

    fun clearUser(context: Context) {
        val sharedPref = context.getSharedPreferences("USER_PREF", Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.clear()
        editor.apply()
    }
}