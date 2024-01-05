package com.features.growharvest.SplashView

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.features.growharvest.LandingPageView.LandingPageMainActivity
import com.features.growharvest.R

class SplashMainActivity : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )
        Handler().postDelayed({
            startActivity(Intent(this, LandingPageMainActivity::class.java))
            finish()
        }, 2500)
    }

}