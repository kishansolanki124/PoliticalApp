package com.app.colorsofgujarat.ui.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.core.text.HtmlCompat
import androidx.lifecycle.ViewModelProvider
import app.app.patidarsaurabh.apputils.AppConstants
import com.app.colorsofgujarat.R
import com.app.colorsofgujarat.apputils.isConnected
import com.app.colorsofgujarat.apputils.showSnackBar
import com.app.colorsofgujarat.databinding.ActivityWinnerNamesBinding
import com.app.colorsofgujarat.pojo.PrizeDetailResponse
import com.app.colorsofgujarat.viewmodel.WinnerViewModel
import com.bumptech.glide.Glide

class WinnerNamesActivity : ExtendedToolbarActivity() {

    private var pid = ""
    private lateinit var settingsViewModel: WinnerViewModel
    override val layoutId: Int
        get() = R.layout.activity_winner_names
    private lateinit var binding: ActivityWinnerNamesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWinnerNamesBinding.inflate(layoutInflater)

        pid = intent.getStringExtra(AppConstants.ID)!!

        setToolbarTitle(getString(R.string.winner))
        settingsViewModel = ViewModelProvider(this).get(WinnerViewModel::class.java)

        settingsViewModel.prizeDetail().observe(this, {
            handleResponse(it)
        })

        if (isConnected(this)) {
            binding.llWinnerPrizeWinner.visibility = View.GONE
            binding.pbWinnerPrizeWinner.visibility = View.VISIBLE
            settingsViewModel.getPrizeDetail(pid)
        } else {
            showSnackBar(getString(R.string.no_internet))
        }

    }

    private fun handleResponse(prizeDetailResponse: PrizeDetailResponse?) {
        binding.pbWinnerPrizeWinner.visibility = View.GONE
        binding.llWinnerPrizeWinner.visibility = View.VISIBLE
        if (null != prizeDetailResponse) {
            setupViews(prizeDetailResponse)
        } else {
            showSnackBar(getString(R.string.something_went_wrong))
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setupViews(prizeDetailResponse: PrizeDetailResponse) {
        Glide.with(this)
            .load(prizeDetailResponse.prize_detail[0].winner_banner)
            .into(binding.ivPrizeWinner)

        binding.tvPrizeWinnerList.text = HtmlCompat.fromHtml(
            prizeDetailResponse.prize_detail[0].prize_winner,
            HtmlCompat.FROM_HTML_MODE_LEGACY
        )

        binding.tvPrizeWinner.text =
            prizeDetailResponse.prize_detail[0].name1 + "\n(" + prizeDetailResponse.prize_detail[0].name2 + ")"
    }
}