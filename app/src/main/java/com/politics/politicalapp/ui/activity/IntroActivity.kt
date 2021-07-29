package com.politics.politicalapp.ui.activity

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.politics.politicalapp.R
import com.politics.politicalapp.adapter.IntroAdapter
import com.politics.politicalapp.apputils.SPreferenceManager
import com.politics.politicalapp.apputils.isConnected
import com.politics.politicalapp.apputils.showSnackBar
import com.politics.politicalapp.apputils.updateVersion
import com.politics.politicalapp.pojo.SettingsResponse
import com.politics.politicalapp.viewmodel.SettingsViewModel
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
            //browserIntent(this, it.)
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
                settingsResponse.settings[0].isfourceupdate
            )
        } else {
            showSnackBar(getString(R.string.something_went_wrong), this)
        }
    }

    private fun checkForUpdate(apVersion: String, forceUpdate: String) {
        var version = ""
        try {
            val pInfo = packageManager.getPackageInfo(applicationContext.packageName, 0)
            version = pInfo.versionName
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        if (!version.equals(apVersion, ignoreCase = true)) {
            if (forceUpdate == "no") {
                updateVersion(this@IntroActivity, false)
            } else {
                updateVersion(this@IntroActivity, true)
            }
        }
    }
}