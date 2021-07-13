package com.politics.politicalapp.ui.activity

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.politics.politicalapp.R
import com.politics.politicalapp.ui.fragment.HomeFragment
import com.politics.politicalapp.ui.fragment.LivePollFragment
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

        setupListener()

        bottomNavigationView.setOnNavigationItemSelectedListener(this)

        switchFragment(HomeFragment(), false)
    }

    private fun setupListener() {
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
                }
                R.id.navigation_winners -> {
                    //switchFragment(ShraddhanjaliHomeFragment(), false)
                }
            }
        }
        return true
    }

    private fun switchFragment(fragment: Fragment, addToBackStack: Boolean) {
        if (newsCategory.isVisible) {
            newsCategory.visibility = View.GONE
        }

//        if (fragment !is HomeFragment) {
//            hideNavigationButton()
//        } else {
//            showNavigationButton()
//        }

        mTransaction = supportFragmentManager.beginTransaction()
        mTransaction.replace(R.id.fragmentContainer, fragment)
        if (addToBackStack) {
            mTransaction.addToBackStack(null)
        }
        mTransaction.commit()
    }

//    override fun onBackPressed() {
//        super.onBackPressed()
//
////        supportFragmentManager.findFragmentById(R.id.fragmentContainer)?.let {
////            // the fragment exists
////            when (it) {
//
////        when (supportFragmentManager.fragments.last()) {
////            is HomeFragment -> {
////                bottomNavigationView.selectedItemId = R.id.navigation_news
////            }
////            is MenuFragment -> {
////                bottomNavigationView.selectedItemId = R.id.navigation_menu
////            }
////            is ShraddhanjaliHomeFragment -> {
////                bottomNavigationView.selectedItemId = R.id.navigation_shraddhanjali
////            }
////            is EMagazineFragment -> {
////                bottomNavigationView.selectedItemId = R.id.navigation_magazine
////            }
////            is OpinionPollFragment -> {
////                bottomNavigationView.selectedItemId = R.id.navigation_opinion_poll
////            }
////        }
//    }

    override fun onBackPressed() {
        if (newsCategory.isVisible) {
            newsCategory.visibility = View.GONE
        } else {
            super.onBackPressed()
        }
    }
}