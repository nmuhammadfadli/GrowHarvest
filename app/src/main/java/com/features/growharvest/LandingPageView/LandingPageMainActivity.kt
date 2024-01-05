package com.features.growharvest.LandingPageView

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import com.features.growharvest.LoginActivity.LoginMainActivity
import com.features.growharvest.R

class LandingPageMainActivity : AppCompatActivity() {
    private lateinit var btnLanjut: ImageButton
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_landing_page)
        btnLanjut = findViewById(R.id.btnLanjut)

        btnLanjut.setOnClickListener{
            val intent = Intent(this, LoginMainActivity::class.java)
            startActivity(intent)
        }
    }

}