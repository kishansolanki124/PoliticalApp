package com.app.colorsofgujarat.ui.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.core.text.HtmlCompat
import androidx.lifecycle.ViewModelProvider
import app.app.patidarsaurabh.apputils.AppConstants
import com.app.colorsofgujarat.R
import com.app.colorsofgujarat.apputils.isConnected
import com.app.colorsofgujarat.apputils.shareIntent
import com.app.colorsofgujarat.apputils.showSnackBar
import com.app.colorsofgujarat.databinding.ActivityQuestionSuggestionDetailBinding
import com.app.colorsofgujarat.pojo.UserAdviseDetailResponse
import com.app.colorsofgujarat.viewmodel.UserAdviseViewModel
import com.bumptech.glide.Glide

class QuestionSuggestionDetailActivity : ExtendedToolbarActivity() {

    private lateinit var govtWorkViewModel: UserAdviseViewModel
    private var aid = ""
    private lateinit var binding: ActivityQuestionSuggestionDetailBinding

    override val layoutId: Int
        get() = R.layout.activity_question_suggestion_detail

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQuestionSuggestionDetailBinding.inflate(layoutInflater)

        setToolbarTitle(getString(R.string.que_suggestion))

        aid = intent.getStringExtra(AppConstants.ID)!!

        govtWorkViewModel = ViewModelProvider(this).get(UserAdviseViewModel::class.java)

        govtWorkViewModel.userAdviseDetail().observe(this, {
            handleResponse(it)
        })

        if (isConnected(this)) {
            binding.pbQuestionSuggestionDetail.visibility = View.VISIBLE
            binding.llMainContent.visibility = View.GONE
            govtWorkViewModel.getUserAdviseDetail(aid)
        } else {
            showSnackBar(getString(R.string.no_internet))
        }
    }

    private fun handleResponse(userAdviseDetailResponse: UserAdviseDetailResponse?) {
        binding.llMainContent.visibility = View.VISIBLE
        binding.pbQuestionSuggestionDetail.visibility = View.GONE
        if (null != userAdviseDetailResponse && userAdviseDetailResponse.advice_detail.isNotEmpty()) {
            setupViews(userAdviseDetailResponse)
        } else {
            showSnackBar(getString(R.string.something_went_wrong))
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setupViews(userAdviseDetailResponse: UserAdviseDetailResponse) {
        Glide.with(this)
            .load(userAdviseDetailResponse.advice_detail[0].up_pro_img)
            .into(binding.ivQuestionSuggestion)

        binding.tvQuestionSuggestion.text = userAdviseDetailResponse.advice_detail[0].title
        binding.tvQuestionSuggestionUserName.text =
            userAdviseDetailResponse.advice_detail[0].user_name
        binding.tvQuestionSuggestionDescription.text = HtmlCompat.fromHtml(
            userAdviseDetailResponse.advice_detail[0].description,
            HtmlCompat.FROM_HTML_MODE_LEGACY
        )

        binding.tvQuestionSuggestionCityDistrict.text =
            userAdviseDetailResponse.advice_detail[0].city + "," +
                    userAdviseDetailResponse.advice_detail[0].district

        binding.ivShareQuestionSuggestionDetail.setOnClickListener {
            shareIntent(
                userAdviseDetailResponse.advice_detail[0].title,
                userAdviseDetailResponse.advice_detail[0].up_pro_img, this
            )
        }
    }
}