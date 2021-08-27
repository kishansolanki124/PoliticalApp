package com.app.colorsofgujarat.ui.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import androidx.lifecycle.ViewModelProvider
import app.app.patidarsaurabh.apputils.AppConstants
import com.app.colorsofgujarat.R
import com.app.colorsofgujarat.apputils.hideKeyboard
import com.app.colorsofgujarat.apputils.isConnected
import com.app.colorsofgujarat.apputils.showSnackBar
import com.app.colorsofgujarat.databinding.ActivityWinnerPrizeDetailBinding
import com.app.colorsofgujarat.pojo.PrizeDetailResponse
import com.app.colorsofgujarat.viewmodel.WinnerViewModel
import com.bumptech.glide.Glide

class WinnerPrizeDetailActivity : AppCompatActivity() {

    private var pid = ""

//    override val layoutId: Int
//        get() = R.layout.activity_winner_prize_detail

    private lateinit var binding: ActivityWinnerPrizeDetailBinding

    private lateinit var settingsViewModel: WinnerViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityWinnerPrizeDetailBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.commonToolbar.tvTitle.visibility = View.VISIBLE
        binding.commonToolbar.tvTitle.text = getString(R.string.winner)
        title = ""
        binding.commonToolbar.ibBack.setOnClickListener {
            hideKeyboard(this)
            onBackPressed()
        }

        pid = intent.getStringExtra(AppConstants.ID)!!

        //setToolbarTitle(getString(R.string.winner))

        settingsViewModel = ViewModelProvider(this).get(WinnerViewModel::class.java)

        settingsViewModel.prizeDetail().observe(this, {
            handleResponse(it)
        })

        if (isConnected(this)) {
            binding.llWinnerPrizeDetail.visibility = View.GONE
            binding.pbWinnerPrizeDetail.visibility = View.VISIBLE
            settingsViewModel.getPrizeDetail(pid)
        } else {
            showSnackBar(getString(R.string.no_internet))
        }

    }

    private fun handleResponse(prizeDetailResponse: PrizeDetailResponse?) {
        binding.pbWinnerPrizeDetail.visibility = View.GONE
        binding.llWinnerPrizeDetail.visibility = View.VISIBLE
        if (null != prizeDetailResponse) {
            setupViews(prizeDetailResponse)
        } else {
            showSnackBar(getString(R.string.something_went_wrong))
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setupViews(prizeDetailResponse: PrizeDetailResponse) {
        Glide.with(this)
            .load(prizeDetailResponse.prize_detail[0].points_banner)
            .into(binding.ivPrizeDetail)

        binding.tvPrizeDetail.text = HtmlCompat.fromHtml(
            prizeDetailResponse.prize_detail[0].prize_detail,
            HtmlCompat.FROM_HTML_MODE_LEGACY
        )

        binding.tvPrizeRules.text = HtmlCompat.fromHtml(
            prizeDetailResponse.prize_detail[0].prize_rules,
            HtmlCompat.FROM_HTML_MODE_LEGACY
        )

        binding.tvQuestionSuggestion.text =
            prizeDetailResponse.prize_detail[0].name1 + "\n" + prizeDetailResponse.prize_detail[0].name2
    }
}