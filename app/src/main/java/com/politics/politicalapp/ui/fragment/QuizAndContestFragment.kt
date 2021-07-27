package com.politics.politicalapp.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import app.app.patidarsaurabh.apputils.AppConstants
import com.politics.politicalapp.R
import com.politics.politicalapp.adapter.QuizAndContestAdapter
import com.politics.politicalapp.apputils.isConnected
import com.politics.politicalapp.apputils.shareIntent
import com.politics.politicalapp.apputils.showSnackBar
import com.politics.politicalapp.pojo.QuizAndContestResponse
import com.politics.politicalapp.ui.activity.HomeActivity
import com.politics.politicalapp.ui.activity.QuizAndContestRunningActivity
import com.politics.politicalapp.ui.activity.QuizAndContestWinnerActivity
import com.politics.politicalapp.viewmodel.QuizAndContestViewModel
import kotlinx.android.synthetic.main.fragment_quiz_and_comment.*

class QuizAndContestFragment : Fragment() {

    private lateinit var govtWorkNewsAdapter: QuizAndContestAdapter
    private var startPage = 0
    private lateinit var settingsViewModel: QuizAndContestViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_quiz_and_comment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupList()

        settingsViewModel = ViewModelProvider(this).get(QuizAndContestViewModel::class.java)

        settingsViewModel.quizAndContest().observe(requireActivity(), {
            handleResponse(it)
        })

        if (isConnected(requireContext())) {
            rvPollAndSurvey.visibility = View.GONE
            pbQuizAndContest.visibility = View.VISIBLE
            settingsViewModel.getQuizAndContest("0", "10")
        } else {
            showSnackBar(getString(R.string.no_internet), requireActivity())
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
                showSnackBar(getString(R.string.no_record_found), requireActivity())
            }
            else -> {
                showSnackBar(getString(R.string.something_went_wrong), requireActivity())
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
                    Intent(requireContext(), QuizAndContestRunningActivity::class.java)
                        .putExtra(AppConstants.ID, it.id)
                )
            }, {
                shareIntent(
                    "Participate in Quiz and Contest and Win Prizes:\n\n" + it.name,
                    requireContext()
                )
            }, {
                //browserIntent(this, it.website!!)
                startActivity(
                    Intent(requireContext(), QuizAndContestWinnerActivity::class.java)
                        .putExtra(AppConstants.ID, it.id)
                )
            }
        )
        //govtWorkNewsAdapter.setItem(stringList)
        rvPollAndSurvey.adapter = govtWorkNewsAdapter
    }

    override fun onResume() {
        super.onResume()
        (activity as HomeActivity).setToolbarTitle(getString(R.string.quiz_and_contest))
    }

}