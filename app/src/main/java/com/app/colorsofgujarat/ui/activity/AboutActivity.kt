package com.app.colorsofgujarat.ui.activity

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import androidx.lifecycle.ViewModelProvider
import com.app.colorsofgujarat.R
import com.app.colorsofgujarat.apputils.hideKeyboard
import com.app.colorsofgujarat.apputils.isConnected
import com.app.colorsofgujarat.apputils.showSnackBar
import com.app.colorsofgujarat.databinding.ActivityAboutBinding
import com.app.colorsofgujarat.pojo.StaticPageResponse
import com.app.colorsofgujarat.viewmodel.SettingsViewModel

class AboutActivity : AppCompatActivity() {

    private lateinit var settingsViewModel: SettingsViewModel

    private lateinit var binding: ActivityAboutBinding
    //override val layoutId: Int get() = R.layout.activity_about

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAboutBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.commonToolbar.tvTitle.visibility = View.VISIBLE
        binding.commonToolbar.tvTitle.text = getString(R.string.About_Us)
        title = ""
        binding.commonToolbar.ibBack.setOnClickListener {
            hideKeyboard(this)
            onBackPressed()
        }
//        binding.commonToolbar.ibBack.visibility = View.GONE
//        binding.commonToolbar.ivNotification.visibility = View.VISIBLE

        //setToolbarTitle(getString(R.string.About_Us))

        settingsViewModel = ViewModelProvider(this).get(SettingsViewModel::class.java)

        settingsViewModel.staticPages().observe(this, {
            handleResponse(it)
        })

        if (isConnected(this)) {
            binding.tvAboutUs.visibility = View.GONE
            binding.pbAboutUs.visibility = View.VISIBLE
            settingsViewModel.getStaticPages()
        } else {
            showSnackBar(getString(R.string.no_internet), this)
        }
    }

    private fun handleResponse(staticPageResponse: StaticPageResponse?) {
        binding.pbAboutUs.visibility = View.GONE
        binding.tvAboutUs.visibility = View.VISIBLE
        if (null != staticPageResponse) {
            for (item in staticPageResponse.staticpage) {
                if (item.name == "About Us") {
                    binding.tvAboutUs.text = HtmlCompat.fromHtml(
                        item.description,
                        HtmlCompat.FROM_HTML_MODE_LEGACY
                    )
                    break
                }
            }
        } else {
            showSnackBar(getString(R.string.something_went_wrong))
        }
    }
}