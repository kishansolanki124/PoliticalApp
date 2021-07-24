package com.politics.politicalapp.ui.activity

import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import androidx.lifecycle.ViewModelProvider
import app.app.patidarsaurabh.apputils.AppConstants
import com.bumptech.glide.Glide
import com.politics.politicalapp.R
import com.politics.politicalapp.apputils.isConnected
import com.politics.politicalapp.apputils.showSnackBar
import com.politics.politicalapp.pojo.NewsDetailResponse
import com.politics.politicalapp.viewmodel.GovtWorkViewModel
import kotlinx.android.synthetic.main.activity_news_detail.*

class NewsDetailActivity : ExtendedToolbarActivity() {

    private lateinit var govtWorkViewModel: GovtWorkViewModel
    private var nid = ""

    override val layoutId: Int
        get() = R.layout.activity_news_detail

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        nid = intent.getStringExtra(AppConstants.ID)!!

        setToolbarTitle(getString(R.string.hot_news))

        govtWorkViewModel = ViewModelProvider(this).get(GovtWorkViewModel::class.java)

        govtWorkViewModel.newsDetail().observe(this, {
            handleResponse(it)
        })

        if (isConnected(this)) {
            pbNewsDetail.visibility = View.VISIBLE
            nsvGovtWorkDetail.visibility = View.GONE
            govtWorkViewModel.getNewsDetail(nid)
        } else {
            showSnackBar(getString(R.string.no_internet))
        }

        val greenText = SpannableString(getString(R.string.give_rate_get_10_point_1))
        val greenText2 = SpannableString(getString(R.string.give_opinion_get_10_point_1))
        greenText.setSpan(
            ForegroundColorSpan(ContextCompat.getColor(this, R.color.black)),
            0, greenText.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        greenText2.setSpan(
            ForegroundColorSpan(ContextCompat.getColor(this, R.color.black)),
            0, greenText.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        val yellowText = SpannableString(getString(R.string.give_rate_get_10_point_2))
        yellowText.setSpan(
            ForegroundColorSpan(ContextCompat.getColor(this, R.color.red_CC252C)),
            0, yellowText.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        val thirdText = SpannableString(getString(R.string.give_rate_get_10_point_3))
        thirdText.setSpan(
            ForegroundColorSpan(ContextCompat.getColor(this, R.color.black)),
            0, thirdText.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
    }

    private fun handleResponse(govtWorkDetailResponse: NewsDetailResponse?) {
        pbNewsDetail.visibility = View.GONE
        nsvGovtWorkDetail.visibility = View.VISIBLE
        if (null != govtWorkDetailResponse) {
            setupViews(govtWorkDetailResponse)
        } else {
            showSnackBar(getString(R.string.something_went_wrong))
        }
    }

    private fun setupViews(govtWorkDetailResponse: NewsDetailResponse) {
        Glide.with(this)
            .load(govtWorkDetailResponse.news_detail[0].up_pro_img)
            .into(ivNewsDetail)

        tvNewsDetailTitle.text = govtWorkDetailResponse.news_detail[0].name

        tvNewsDetailDesc.text = HtmlCompat.fromHtml(
            govtWorkDetailResponse.news_detail[0].description,
            HtmlCompat.FROM_HTML_MODE_LEGACY
        )
    }
}