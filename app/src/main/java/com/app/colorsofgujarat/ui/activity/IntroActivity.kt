package com.app.colorsofgujarat.ui.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.app.colorsofgujarat.BuildConfig
import com.app.colorsofgujarat.R
import com.app.colorsofgujarat.adapter.IntroAdapter
import com.app.colorsofgujarat.apputils.*
import com.app.colorsofgujarat.pojo.SettingsResponse
import com.app.colorsofgujarat.viewmodel.SettingsViewModel
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.activity_intro.*
import java.util.*

class IntroActivity : AppCompatActivity() {

    private lateinit var settingsViewModel: SettingsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)

        btGetStarted.setOnClickListener {
            openHome()
        }

        settingsViewModel = ViewModelProvider(this).get(SettingsViewModel::class.java)

        fetchSettings()

        settingsViewModel.settingsResponse().observe(this, {
            handleResponse(it)
        })
    }

    private fun setupOffersViewPager(homeBanner: List<SettingsResponse.WelcomeBanner>) {
        val adapter = IntroAdapter {
            browserIntent(this, it.url)
        }
        adapter.setItem(homeBanner)
        introViewpager.adapter = adapter

        TabLayoutMediator(introTabLayout, introViewpager as ViewPager2) { _, _ ->
        }.attach()

        var currentPage = 0
        val handler = Handler(Looper.getMainLooper())

        val update = Runnable {
            if (currentPage == homeBanner.size) {
                currentPage = 0
            }
            introViewpager.setCurrentItem(currentPage++, true)
        }

        Timer().schedule(object : TimerTask() {
            override fun run() {
                handler.post(update)
            }
        }, 2000, 3500)

        introViewpager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                currentPage = position
            }
        })
    }

    private fun fetchSettings() {
        if (isConnected(this)) {
            pbIntro.visibility = View.VISIBLE
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
        pbIntro.visibility = View.GONE

        if (null != settingsResponse) {
            SPreferenceManager.getInstance(this).saveSettings(settingsResponse)
            setupOffersViewPager(settingsResponse.welcome_banner)

            checkForUpdate(
                settingsResponse.settings[0].android_version,
                settingsResponse.settings[0].updatemsg,
                settingsResponse.settings[0].isfourceupdate
            )
        } else {
            showSnackBar(getString(R.string.something_went_wrong), this)
        }
    }

    private fun checkForUpdate(apVersion: String, updateMsg: String, forceUpdate: String) {
        val versionCode: Int = BuildConfig.VERSION_CODE
        if (versionCode >= apVersion.toInt()) {
            return
        }

        val alertDialogBuilder: AlertDialog.Builder = AlertDialog.Builder(this)
        alertDialogBuilder.setMessage(updateMsg)
        if (forceUpdate == "no") {
            alertDialogBuilder.setCancelable(true)
            alertDialogBuilder.setNegativeButton(
                getString(R.string.cancel)
            ) { dialog, _ ->
                dialog.cancel()
            }
        } else {
            alertDialogBuilder.setCancelable(false)
        }

        alertDialogBuilder.setPositiveButton(
            getString(R.string.update_now)
        ) { dialog, _ ->
            dialog.cancel()
            rateApp()
            finish()
        }

        val alertDialog: AlertDialog = alertDialogBuilder.create()
        alertDialog.show()
        if (forceUpdate == "no") {
            alertDialog.getButton(AlertDialog.BUTTON_POSITIVE)
                .setTextColor(ContextCompat.getColor(this, R.color.red_CC252C))
            alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE)
                .setTextColor(ContextCompat.getColor(this, R.color.red_CC252C))
        } else {
            alertDialog.getButton(AlertDialog.BUTTON_POSITIVE)
                .setTextColor(ContextCompat.getColor(this, R.color.red_CC252C))
        }
    }
}