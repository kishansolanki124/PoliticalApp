package com.app.colorsofgujarat.ui.activity

import android.os.Bundle
import android.view.View
import androidx.core.text.HtmlCompat
import androidx.lifecycle.ViewModelProvider
import com.app.colorsofgujarat.R
import com.app.colorsofgujarat.apputils.isConnected
import com.app.colorsofgujarat.apputils.showSnackBar
import com.app.colorsofgujarat.pojo.StaticPageResponse
import com.app.colorsofgujarat.viewmodel.SettingsViewModel
import kotlinx.android.synthetic.main.activity_tnc.*

class TNCActivity : ExtendedToolbarActivity() {

    private lateinit var settingsViewModel: SettingsViewModel

    override val layoutId: Int
        get() = R.layout.activity_tnc

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setToolbarTitle(getString(R.string.Terms_Of_Use))
        settingsViewModel = ViewModelProvider(this).get(SettingsViewModel::class.java)

        settingsViewModel.staticPages().observe(this, {
            handleResponse(it)
        })

        if (isConnected(this)) {
            tvTNC.visibility = View.GONE
            pbTNC.visibility = View.VISIBLE
            settingsViewModel.getStaticPages()
        } else {
            showSnackBar(getString(R.string.no_internet), this)
        }
    }

    private fun handleResponse(staticPageResponse: StaticPageResponse?) {
        pbTNC.visibility = View.GONE
        tvTNC.visibility = View.VISIBLE
        if (null != staticPageResponse) {
            for (item in staticPageResponse.staticpage) {
                if (item.name == "Terms & Conditions") {
                    tvTNC.text = HtmlCompat.fromHtml(
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