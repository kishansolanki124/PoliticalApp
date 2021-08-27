package com.app.colorsofgujarat.ui.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.MenuItem
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import com.app.colorsofgujarat.R
import com.app.colorsofgujarat.apputils.*
import com.app.colorsofgujarat.databinding.ActivityHomeBinding
import com.app.colorsofgujarat.pojo.PopupBannerResponse
import com.app.colorsofgujarat.ui.fragment.HomeFragment
import com.app.colorsofgujarat.ui.fragment.LivePollFragment
import com.app.colorsofgujarat.ui.fragment.QuizAndContestFragment
import com.app.colorsofgujarat.ui.fragment.WinnerFragment
import com.app.colorsofgujarat.viewmodel.SettingsViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView

class HomeActivity : ExtendedToolbarActivity(),
    BottomNavigationView.OnNavigationItemSelectedListener {

    private lateinit var mTransaction: FragmentTransaction
    private var handler: Handler? = null
    private var runnableCode: Runnable? = null
    private lateinit var settingsViewModel: SettingsViewModel
    private lateinit var binding: ActivityHomeBinding
    override val layoutId: Int
        get() = R.layout.activity_home

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)

        setToolbarTitle(getString(R.string.colors_of_guj))
        hideBackButton()
        showNotificationIcon()
        setupListener()
        binding.bottomNavigationView.setOnNavigationItemSelectedListener(this)
        switchFragment(HomeFragment(), false)

        settingsViewModel = ViewModelProvider(this).get(SettingsViewModel::class.java)

        fetchPopupBanner()

        settingsViewModel.popupBanners().observe(this, {
            handleResponse(it)
        })
    }

    private fun setupListener() {
        binding.newsCategory.ivAboutUs.setOnClickListener {
            hideMenu()
            startActivity(Intent(this, AboutActivity::class.java))
        }

        binding.newsCategory.tvContactUs.setOnClickListener {
            hideMenu()
            startActivity(Intent(this, ContactUsActivity::class.java))
        }

        binding.newsCategory.ivShareApp.setOnClickListener {
            hideMenu()
            shareApp()
        }

        binding.newsCategory.ivRateUs.setOnClickListener {
            hideMenu()
            rateApp()
        }

        binding.newsCategory.ivTNC.setOnClickListener {
            hideMenu()
            startActivity(Intent(this, TNCActivity::class.java))
        }

//        tvLogout.setOnClickListener {
//            hideMenu()
//            SPreferenceManager.getInstance(this).clearSession()
//            val mIntent = Intent(this, SplashActivity::class.java)
//            finishAffinity()
//            startActivity(mIntent)
//        }

        binding.newsCategory.ivClose.setOnClickListener {
            if (binding.newsCategory.root.isVisible) {
                binding.newsCategory.root.visibility = View.GONE
            } else {
                binding.newsCategory.root.visibility = View.VISIBLE
            }
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        if (binding.bottomNavigationView.selectedItemId != item.itemId) {
            when (item.itemId) {
                R.id.navigation_home -> {
                    switchFragment(HomeFragment(), false)
                }
                R.id.navigation_live_poll -> {
                    switchFragment(LivePollFragment(), false)
                }
                R.id.navigation_quiz -> {
                    switchFragment(QuizAndContestFragment(), false)
                }
                R.id.navigation_menu -> {
                    if (!binding.newsCategory.root.isVisible) {
                        binding.newsCategory.root.visibility = View.VISIBLE
                    }
                    return false
                }
                R.id.navigation_winners -> {
                    switchFragment(WinnerFragment(), false)
                }
            }
        }
        return true
    }

    private fun switchFragment(fragment: Fragment, addToBackStack: Boolean) {
        hideMenu()

        mTransaction = supportFragmentManager.beginTransaction()
        mTransaction.replace(R.id.fragmentContainer, fragment)
        if (addToBackStack) {
            mTransaction.addToBackStack(null)
        }
        mTransaction.commit()
    }

    private var doubleBackToExitPressedOnce = false
    override fun onBackPressed() {
        if (binding.newsCategory.root.isVisible) {
            binding.newsCategory.root.visibility = View.GONE
        } else {
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed()
                return
            }
            this.doubleBackToExitPressedOnce = true
            showToast("Please click BACK again to exit")
            Handler(Looper.getMainLooper()).postDelayed(
                { doubleBackToExitPressedOnce = false },
                2000
            )
        }
    }

    private fun hideMenu() {
        if (binding.newsCategory.root.isVisible) {
            binding.newsCategory.root.visibility = View.GONE
        }
    }

    private fun fetchPopupBanner() {
        if (isConnected(this)) {
            settingsViewModel.getPopupBanner()
        } else {
            showSnackBar(getString(R.string.no_internet), this)
        }
    }

    private fun handleResponse(popupBannerResponse: PopupBannerResponse) {
        SPreferenceManager.getInstance(this).saveBanners(popupBannerResponse)

        if (!popupBannerResponse.popup_banner.isNullOrEmpty()) {
            setupRepeatableBannerAd(
                popupBannerResponse.delay_time, popupBannerResponse.initial_time,
                popupBannerResponse.popup_banner
            )
        }
    }

    private fun setupRepeatableBannerAd(
        delayTime: String,
        initialTime: String,
        popupBanner: List<PopupBannerResponse.PopupBanner>
    ) {
        handler = Handler(Looper.getMainLooper())
        runnableCode = object : Runnable {
            override fun run() {
                if (!isDestroyed && !this@HomeActivity.isFinishing) {
                    if (this@HomeActivity.lifecycle.currentState
                            .isAtLeast(Lifecycle.State.RESUMED)
                    ) {
                        showProgressDialog(this@HomeActivity, popupBanner)
                    }
                    handler?.postDelayed(this, delayTime.toLong() * 1000)
                }
            }
        }

        if (!isDestroyed && !this.isFinishing) {
            runnableCode?.let {
                handler?.postDelayed(it, initialTime.toLong() * 1000)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (null != handler) {
            runnableCode?.let {
                handler!!.removeCallbacks(it)
            }
        }
    }
}