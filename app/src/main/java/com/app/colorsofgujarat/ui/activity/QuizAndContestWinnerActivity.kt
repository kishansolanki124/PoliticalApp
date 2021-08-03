package com.app.colorsofgujarat.ui.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.core.text.HtmlCompat
import androidx.lifecycle.ViewModelProvider
import app.app.patidarsaurabh.apputils.AppConstants
import com.app.colorsofgujarat.R
import com.app.colorsofgujarat.apputils.SPreferenceManager
import com.app.colorsofgujarat.apputils.isConnected
import com.app.colorsofgujarat.apputils.openBrowser
import com.app.colorsofgujarat.apputils.showSnackBar
import com.app.colorsofgujarat.pojo.QuizAndContestRunningResponse
import com.app.colorsofgujarat.viewmodel.QuizAndContestViewModel
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_quiz_and_contest_winner.*

class QuizAndContestWinnerActivity : ExtendedToolbarActivity() {

    private var qid = ""
    private var browserURL = ""
    private lateinit var settingsViewModel: QuizAndContestViewModel

    override val layoutId: Int
        get() = R.layout.activity_quiz_and_contest_winner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setToolbarTitle(getString(R.string.quiz_and_contest))

        qid = intent.getStringExtra(AppConstants.ID)!!

        settingsViewModel = ViewModelProvider(this).get(QuizAndContestViewModel::class.java)

        settingsViewModel.quizAndContestDetail().observe(this, {
            handleResponse(it)
        })

        if (isConnected(this)) {
            cvQuizAndContestWinner.visibility = View.GONE
            pbQuizAndContestWinner.visibility = View.VISIBLE
            settingsViewModel.getQuizAndContestDetail(
                qid,
                SPreferenceManager.getInstance(this).session
            )
        } else {
            showSnackBar(getString(R.string.no_internet))
        }

        ivSponsor.setOnClickListener {
            openBrowser(this, browserURL)
        }
    }

    private fun handleResponse(quizAndContestRunningResponse: QuizAndContestRunningResponse?) {
        pbQuizAndContestWinner.visibility = View.GONE
        cvQuizAndContestWinner.visibility = View.VISIBLE

        if (null != quizAndContestRunningResponse) {
            setupViews(quizAndContestRunningResponse)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setupViews(quizAndContestRunningResponse: QuizAndContestRunningResponse) {
        if (!quizAndContestRunningResponse.quiz_detail[0].sponser_img.isNullOrEmpty()) {
            tvSponsor.visibility = View.VISIBLE
            Glide.with(this)
                .load(quizAndContestRunningResponse.quiz_detail[0].sponser_img)
                .into(ivSponsor)
        } else {
            tvSponsor.visibility = View.GONE
        }

        tvQuestionSuggestion.text = quizAndContestRunningResponse.quiz_question[0].question
        tvAnswer.text = "જવાબ: " + quizAndContestRunningResponse.quiz_detail[0].correct_answer
        tvQuizWinnerNames.text = HtmlCompat.fromHtml(
            quizAndContestRunningResponse.quiz_detail[0].quiz_winner,
            HtmlCompat.FROM_HTML_MODE_LEGACY
        )

        browserURL = quizAndContestRunningResponse.quiz_detail[0].sponser_url
    }
}