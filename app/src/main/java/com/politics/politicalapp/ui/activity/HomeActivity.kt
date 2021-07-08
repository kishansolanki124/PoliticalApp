package com.politics.politicalapp.ui.activity

import android.os.Bundle
import android.view.MenuItem
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.politics.politicalapp.R
import com.politics.politicalapp.ui.fragment.HomeFragment
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : ExtendedToolbarActivity(),
    BottomNavigationView.OnNavigationItemSelectedListener {

    private lateinit var mTransaction: FragmentTransaction

    override val layoutId: Int
        get() = R.layout.activity_home

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setToolbarTitle("")

        bottomNavigationView.setOnNavigationItemSelectedListener(this)

        switchFragment(HomeFragment(), false)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        if (bottomNavigationView.selectedItemId != item.itemId) {
            when (item.itemId) {
                R.id.navigation_home -> {
                    switchFragment(HomeFragment(), false)
                }
                R.id.navigation_live_poll -> {
                    //switchFragment(OpinionPollFragment(), false)
                }
                R.id.navigation_menu -> {
                    //switchFragment(MenuFragment(), false)
                }
                R.id.navigation_winners -> {
                    //switchFragment(ShraddhanjaliHomeFragment(), false)
                }
            }
        }
        return true
    }

    private fun switchFragment(fragment: Fragment, addToBackStack: Boolean) {
//        if (newsCategory.isVisible) {
//            newsCategory.visibility = View.GONE
//        }

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

//    override fun onBackPressed() {
//        if (newsCategory.isVisible) {
//            newsCategory.visibility = View.GONE
//        } else {
//            super.onBackPressed()
//        }
//    }
}