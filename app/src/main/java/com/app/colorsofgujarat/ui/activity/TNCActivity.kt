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
import com.app.colorsofgujarat.databinding.ActivityTncBinding
import com.app.colorsofgujarat.pojo.StaticPageResponse
import com.app.colorsofgujarat.viewmodel.SettingsViewModel

class TNCActivity : AppCompatActivity() {

    private lateinit var settingsViewModel: SettingsViewModel
    private lateinit var binding: ActivityTncBinding

//    override val layoutId: Int
//        get() = R.layout.activity_tnc

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTncBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.commonToolbar.tvTitle.visibility = View.VISIBLE
        binding.commonToolbar.tvTitle.text = getString(R.string.Terms_Of_Use)
        title = ""
        binding.commonToolbar.ibBack.setOnClickListener {
            hideKeyboard(this)
            onBackPressed()
        }

        //setToolbarTitle(getString(R.string.Terms_Of_Use))
        settingsViewModel = ViewModelProvider(this).get(SettingsViewModel::class.java)

        settingsViewModel.staticPages().observe(this, {
            handleResponse(it)
        })

        if (isConnected(this)) {
            binding.tvTNC.visibility = View.GONE
            binding.pbTNC.visibility = View.VISIBLE
            settingsViewModel.getStaticPages()
        } else {
            showSnackBar(getString(R.string.no_internet), this)
        }
    }

    private fun handleResponse(staticPageResponse: StaticPageResponse?) {
        binding.pbTNC.visibility = View.GONE
        binding.tvTNC.visibility = View.VISIBLE
        if (null != staticPageResponse) {
            for (item in staticPageResponse.staticpage) {
                if (item.name == "Terms & Conditions") {
                    binding.tvTNC.text = HtmlCompat.fromHtml(
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