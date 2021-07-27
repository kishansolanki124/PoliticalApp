package com.politics.politicalapp.ui.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.politics.politicalapp.R
import com.politics.politicalapp.apputils.SPreferenceManager
import com.politics.politicalapp.apputils.isConnected
import com.politics.politicalapp.apputils.showSnackBar
import com.politics.politicalapp.pojo.SettingsResponse
import com.politics.politicalapp.viewmodel.SettingsViewModel
import kotlinx.android.synthetic.main.activity_splash.*

class SplashActivity : AppCompatActivity() {

    private var mCountDownTime = 2000L //time in milliseconds
    private lateinit var settingsViewModel: SettingsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_splash)

        settingsViewModel = ViewModelProvider(this).get(SettingsViewModel::class.java)

        fetchSettings()

        settingsViewModel.settingsResponse().observe(this, {
            handleResponse(it)
        })
    }

    private fun fetchSettings() {
        if (isConnected(this)) {
            pbSplash.visibility = View.VISIBLE
            ivSplash.visibility = View.GONE
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
        pbSplash.visibility = View.GONE
        ivSplash.visibility = View.VISIBLE

        if (null != settingsResponse) {
            SPreferenceManager.getInstance(this).saveSettings(settingsResponse)

            Glide.with(this)
                .load(settingsResponse.welcome_banner[0].up_pro_img)
                .into(ivSplash)

            Handler(Looper.getMainLooper()).postDelayed({
                openHome()
            }, mCountDownTime)
        } else {
            showSnackBar(getString(R.string.something_went_wrong), this)
        }
    }
}