package com.politics.politicalapp.ui.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.MenuItem
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.politics.politicalapp.R
import com.politics.politicalapp.apputils.SPreferenceManager
import com.politics.politicalapp.apputils.showToast
import com.politics.politicalapp.ui.fragment.HomeFragment
import com.politics.politicalapp.ui.fragment.LivePollFragment
import com.politics.politicalapp.ui.fragment.WinnerFragment
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.fragment_menu.*

class HomeActivity : ExtendedToolbarActivity(),
    BottomNavigationView.OnNavigationItemSelectedListener {

    private lateinit var mTransaction: FragmentTransaction

    override val layoutId: Int
        get() = R.layout.activity_home

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setToolbarTitle(getString(R.string.colors_of_guj))
        hideBackButton()
        showNotificationIcon()
        setupListener()
        bottomNavigationView.setOnNavigationItemSelectedListener(this)
        switchFragment(HomeFragment(), false)
    }

    private fun setupListener() {
        ivAboutUs.setOnClickListener {
            if (newsCategory.isVisible) {
                newsCategory.visibility = View.GONE
            }
            startActivity(Intent(this, AboutActivity::class.java))
        }

        tvContactUs.setOnClickListener {
            if (newsCategory.isVisible) {
                newsCategory.visibility = View.GONE
            }
            startActivity(Intent(this, ContactUsActivity::class.java))
        }

        ivTNC.setOnClickListener {
            if (newsCategory.isVisible) {
                newsCategory.visibility = View.GONE
            }
            startActivity(Intent(this, TNCActivity::class.java))
        }

        tvLogout.setOnClickListener {
            if (newsCategory.isVisible) {
                newsCategory.visibility = View.GONE
            }
            SPreferenceManager.getInstance(this).clearSession()
            val mIntent = Intent(this, SplashActivity::class.java)
            finishAffinity()
            startActivity(mIntent)
        }

        ivClose.setOnClickListener {
            if (newsCategory.isVisible) {
                newsCategory.visibility = View.GONE
            } else {
                newsCategory.visibility = View.VISIBLE
            }
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        if (bottomNavigationView.selectedItemId != item.itemId) {
            when (item.itemId) {
                R.id.navigation_home -> {
                    switchFragment(HomeFragment(), false)
                }
                R.id.navigation_live_poll -> {
                    switchFragment(LivePollFragment(), false)
                }
                R.id.navigation_menu -> {
                    if (!newsCategory.isVisible) {
                        newsCategory.visibility = View.VISIBLE
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
        if (newsCategory.isVisible) {
            newsCategory.visibility = View.GONE
        }

        mTransaction = supportFragmentManager.beginTransaction()
        mTransaction.replace(R.id.fragmentContainer, fragment)
        if (addToBackStack) {
            mTransaction.addToBackStack(null)
        }
        mTransaction.commit()
    }

    private var doubleBackToExitPressedOnce = false
    override fun onBackPressed() {
        if (newsCategory.isVisible) {
            newsCategory.visibility = View.GONE
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
}