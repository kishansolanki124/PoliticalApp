package com.app.colorsofgujarat.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import app.app.patidarsaurabh.apputils.AppConstants
import com.app.colorsofgujarat.R
import com.app.colorsofgujarat.adapter.QuizAndContestAdapter
import com.app.colorsofgujarat.apputils.isConnected
import com.app.colorsofgujarat.apputils.shareIntent
import com.app.colorsofgujarat.apputils.showSnackBar
import com.app.colorsofgujarat.pojo.QuizAndContestResponse
import com.app.colorsofgujarat.viewmodel.QuizAndContestViewModel
import kotlinx.android.synthetic.main.activity_quiz_and_contest.*

class QuizAndContestActivity : ExtendedToolbarActivity() {

    private lateinit var govtWorkNewsAdapter: QuizAndContestAdapter
    private var startPage = 0
    private lateinit var settingsViewModel: QuizAndContestViewModel

    override val layoutId: Int
        get() = R.layout.activity_quiz_and_contest

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setToolbarTitle(getString(R.string.quiz_and_contest))
        setupList()

        settingsViewModel = ViewModelProvider(this).get(QuizAndContestViewModel::class.java)

        settingsViewModel.quizAndContest().observe(this, {
            handleResponse(it)
        })

        if (isConnected(this)) {
            rvPollAndSurvey.visibility = View.GONE
            pbQuizAndContest.visibility = View.VISIBLE
            settingsViewModel.getQuizAndContest("0", "10")
        } else {
            showSnackBar(getString(R.string.no_internet))
        }
    }

    private fun handleResponse(quizAndContestResponse: QuizAndContestResponse) {
        rvPollAndSurvey.visibility = View.VISIBLE
        pbQuizAndContest.visibility = View.GONE
        when {
            quizAndContestResponse.quiz_list.isNotEmpty() -> {
                addItems(quizAndContestResponse.quiz_list)
            }
            startPage == 0 -> {
                govtWorkNewsAdapter.reset()
                showSnackBar(getString(R.string.no_record_found), this)
            }
            else -> {
                showSnackBar(getString(R.string.something_went_wrong), this)
            }
        }
    }

    private fun addItems(quizList: ArrayList<QuizAndContestResponse.Quiz>) {
        govtWorkNewsAdapter.setItem(quizList)
    }

    private fun setupList() {
        govtWorkNewsAdapter = QuizAndContestAdapter(
            {
                //callIntent(this, it.contact_no!!)
                startActivity(
                    Intent(this, QuizAndContestRunningActivity::class.java)
                        .putExtra(AppConstants.ID, it.id)
                )
            }, {
                shareIntent(
                    "Participate in Quiz and Contest and Win Prizes:\n\n" + it.name,
                    this
                )
            }, {
                //browserIntent(this, it.website!!)
                startActivity(
                    Intent(this, QuizAndContestWinnerActivity::class.java)
                        .putExtra(AppConstants.ID, it.id)
                )
            }
        )
        //govtWorkNewsAdapter.setItem(stringList)
        rvPollAndSurvey.adapter = govtWorkNewsAdapter
    }
}