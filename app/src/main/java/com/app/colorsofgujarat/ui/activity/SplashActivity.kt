package com.app.colorsofgujarat.ui.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.app.colorsofgujarat.R
import com.app.colorsofgujarat.apputils.SPreferenceManager
import com.app.colorsofgujarat.apputils.isConnected
import com.app.colorsofgujarat.apputils.showSnackBar
import com.app.colorsofgujarat.databinding.ActivitySplashBinding
import com.app.colorsofgujarat.pojo.SettingsResponse
import com.app.colorsofgujarat.viewmodel.SettingsViewModel
import com.bumptech.glide.Glide

class SplashActivity : AppCompatActivity() {

    private var mCountDownTime = 2000L //time in milliseconds
    private lateinit var settingsViewModel: SettingsViewModel

    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySplashBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        settingsViewModel = ViewModelProvider(this).get(SettingsViewModel::class.java)

        fetchSettings()

        settingsViewModel.settingsResponse().observe(this, {
            handleResponse(it)
        })
    }

    private fun fetchSettings() {
        if (isConnected(this)) {
            binding.pbSplash.visibility = View.VISIBLE
            binding.ivSplash.visibility = View.GONE
            settingsViewModel.getSettings("")
        } else {
            showSnackBar(getString(R.string.no_internet), this)
        }
    }

    private fun openHome() {
        if (SPreferenceManager.getInstance(this).isLogin) {
            startActivity(Intent(this, HomeActivity::class.java))
        } else {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
        finish()
    }

    private fun handleResponse(settingsResponse: SettingsResponse?) {
        binding.pbSplash.visibility = View.GONE
        binding.ivSplash.visibility = View.VISIBLE

        if (null != settingsResponse) {
            SPreferenceManager.getInstance(this).saveSettings(settingsResponse)

            Glide.with(this)
                .load(settingsResponse.welcome_banner[0].up_pro_img)
                .into(binding.ivSplash)

            Handler(Looper.getMainLooper()).postDelayed({
                openHome()
            }, mCountDownTime)
        } else {
            showSnackBar(getString(R.string.something_went_wrong), this)
        }
    }
}