package com.politics.politicalapp.ui.activity

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.politics.politicalapp.R

class SplashActivity : AppCompatActivity() {

    private var mCountDownTime = 2000L //time in milliseconds

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_splash)

        Handler(Looper.getMainLooper()).postDelayed({
            openHome()
        }, mCountDownTime)
    }

    private fun openHome() {
//        if (SPreferenceManager.getInstance(this).isLogin) {
//            startActivity(Intent(this, HomeActivity::class.java))
//        } else {
//            startActivity(Intent(this, RegisterActivity::class.java))
//        }
//        finish()
    }
}