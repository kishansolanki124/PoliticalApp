package com.politics.politicalapp.ui.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.core.text.HtmlCompat
import androidx.lifecycle.ViewModelProvider
import app.app.patidarsaurabh.apputils.AppConstants
import com.bumptech.glide.Glide
import com.politics.politicalapp.R
import com.politics.politicalapp.apputils.isConnected
import com.politics.politicalapp.apputils.shareIntent
import com.politics.politicalapp.apputils.showSnackBar
import com.politics.politicalapp.pojo.UserAdviseDetailResponse
import com.politics.politicalapp.viewmodel.UserAdviseViewModel
import kotlinx.android.synthetic.main.activity_question_suggestion_detail.*

class QuestionSuggestionDetailActivity : ExtendedToolbarActivity() {

    private lateinit var govtWorkViewModel: UserAdviseViewModel
    private var aid = ""

    override val layoutId: Int
        get() = R.layout.activity_question_suggestion_detail

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setToolbarTitle(getString(R.string.que_suggestion))

        aid = intent.getStringExtra(AppConstants.ID)!!

        govtWorkViewModel = ViewModelProvider(this).get(UserAdviseViewModel::class.java)

        govtWorkViewModel.userAdviseDetail().observe(this, {
            handleResponse(it)
        })

        if (isConnected(this)) {
            pbQuestionSuggestionDetail.visibility = View.VISIBLE
            llMainContent.visibility = View.GONE
            govtWorkViewModel.getUserAdviseDetail(aid)
        } else {
            showSnackBar(getString(R.string.no_internet))
        }
    }

    private fun handleResponse(userAdviseDetailResponse: UserAdviseDetailResponse?) {
        llMainContent.visibility = View.VISIBLE
        pbQuestionSuggestionDetail.visibility = View.GONE
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
            .into(ivQuestionSuggestion)

        tvQuestionSuggestion.text = userAdviseDetailResponse.advice_detail[0].title
        tvQuestionSuggestionUserName.text = userAdviseDetailResponse.advice_detail[0].user_name
        tvQuestionSuggestionDescription.text = HtmlCompat.fromHtml(
            userAdviseDetailResponse.advice_detail[0].description,
            HtmlCompat.FROM_HTML_MODE_LEGACY
        )

        tvQuestionSuggestionCityDistrict.text =
            userAdviseDetailResponse.advice_detail[0].city + "," +
                    userAdviseDetailResponse.advice_detail[0].district

        ivShareQuestionSuggestionDetail.setOnClickListener {
            shareIntent(
                userAdviseDetailResponse.advice_detail[0].title,
                userAdviseDetailResponse.advice_detail[0].up_pro_img, this
            )
        }
    }
}