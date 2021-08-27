package com.app.colorsofgujarat.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.annotation.Nullable
import androidx.appcompat.app.AppCompatActivity
import com.app.colorsofgujarat.apputils.hideKeyboard
import com.app.colorsofgujarat.databinding.CommonToolbarBinding

abstract class ExtendedToolbarActivity : AppCompatActivity() {

    abstract val layoutId: Int
    private lateinit var binding: CommonToolbarBinding

    override fun onCreate(@Nullable savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = CommonToolbarBinding.inflate(layoutInflater)
        setContentView(layoutId)

        setSupportActionBar(binding.toolbar)
        //ibBack.visibility = View.VISIBLE
        //ivNavigation.visibility = View.VISIBLE

        binding.ibBack.setOnClickListener {
            hideKeyboard(this)
            onBackPressed()
        }

        binding.ivNotification.setOnClickListener {
            hideUnreadCount()
            startActivity(Intent(this, NotificationActivity::class.java))
        }
    }

    fun showUnreadCount() {
        binding.ivNotificationUnread.visibility = View.VISIBLE
    }

    fun hideUnreadCount() {
        binding.ivNotificationUnread.visibility = View.GONE
    }

    fun setToolbarTitle(title: String?) {
        supportActionBar?.setDisplayShowTitleEnabled(false)
        binding.tvTitle.visibility = View.VISIBLE
        binding.tvTitle.text = title
        setTitle("")
    }

    fun hideBackButton() {
        binding.ibBack.visibility = View.GONE
    }

    fun showNotificationIcon() {
        binding.ivNotification.visibility = View.VISIBLE
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