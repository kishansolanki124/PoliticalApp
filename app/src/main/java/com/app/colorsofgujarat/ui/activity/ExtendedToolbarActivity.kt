package com.app.colorsofgujarat.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.annotation.Nullable
import androidx.appcompat.app.AppCompatActivity
import com.app.colorsofgujarat.apputils.hideKeyboard
import kotlinx.android.synthetic.main.common_toolbar.*

abstract class ExtendedToolbarActivity : AppCompatActivity() {

    abstract val layoutId: Int

    override fun onCreate(@Nullable savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layoutId)

        setSupportActionBar(toolbar)
        //ibBack.visibility = View.VISIBLE
        //ivNavigation.visibility = View.VISIBLE

        ibBack.setOnClickListener {
            hideKeyboard(this)
            onBackPressed()
        }

        ivNotification.setOnClickListener {
            hideUnreadCount()
            startActivity(Intent(this, NotificationActivity::class.java))
        }
    }

    fun showUnreadCount() {
        ivNotificationUnread.visibility = View.VISIBLE
    }

    fun hideUnreadCount() {
        ivNotificationUnread.visibility = View.GONE
    }

    fun setToolbarTitle(title: String?) {
        supportActionBar?.setDisplayShowTitleEnabled(false)
        tvTitle.visibility = View.VISIBLE
        tvTitle.text = title
        setTitle("")
    }

    fun hideBackButton() {
        ibBack.visibility = View.GONE
    }

    fun showNotificationIcon() {
        ivNotification.visibility = View.VISIBLE
    }

//    fun hideappLogo() {
//        ivToolbarLogo.visibility = View.GONE
//    }

//    fun hideNavigationButton() {
//        ivNavigation.visibility = View.GONE
//    }

//    fun showNavigationButton() {
//        ivNavigation.visibility = View.VISIBLE
//    }
}