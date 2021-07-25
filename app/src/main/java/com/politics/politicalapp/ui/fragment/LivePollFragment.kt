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
import com.politics.politicalapp.adapter.LivePollAdapter
import com.politics.politicalapp.apputils.SPreferenceManager
import com.politics.politicalapp.apputils.isConnected
import com.politics.politicalapp.apputils.shareIntent
import com.politics.politicalapp.apputils.showSnackBar
import com.politics.politicalapp.pojo.LivePollListResponse
import com.politics.politicalapp.ui.activity.HomeActivity
import com.politics.politicalapp.ui.activity.LivePollRunningActivity
import com.politics.politicalapp.viewmodel.LivePollViewModel
import kotlinx.android.synthetic.main.fragment_live_poll.*

class LivePollFragment : Fragment() {

    private lateinit var govtWorkNewsAdapter: LivePollAdapter
    private var startPage = 0
    private lateinit var settingsViewModel: LivePollViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_live_poll, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //setToolbarTitle(getString(R.string.quiz_and_contest))
        setupList()

        settingsViewModel = ViewModelProvider(this).get(LivePollViewModel::class.java)

        settingsViewModel.quizAndContest().observe(requireActivity(), {
            handleResponse(it)
        })

        if (isConnected(requireContext())) {
            rvPollAndSurvey.visibility = View.GONE
            pbLivePoll.visibility = View.VISIBLE
            settingsViewModel.getLivePollList(
                SPreferenceManager.getInstance(requireContext()).session,
                "0",
                "10"
            )
        } else {
            showSnackBar(getString(R.string.no_internet), requireActivity())
        }
    }

    private fun handleResponse(livePollListResponse: LivePollListResponse?) {
        if (null != livePollListResponse) {
            rvPollAndSurvey.visibility = View.VISIBLE
            pbLivePoll.visibility = View.GONE
            when {
                livePollListResponse.live_poll_list.isNotEmpty() -> {
                    addItems(livePollListResponse.live_poll_list)
                }
                startPage == 0 -> {
                    govtWorkNewsAdapter.reset()
                    showSnackBar(getString(R.string.no_record_found), requireActivity())
                }
                else -> {
                    showSnackBar(getString(R.string.something_went_wrong), requireActivity())
                }
            }
        } else {
            showSnackBar(getString(R.string.something_went_wrong), requireActivity())
        }
    }

    private fun setupList() {
        govtWorkNewsAdapter = LivePollAdapter(
            { it, showButton ->
                //callIntent(this, it.contact_no!!)
                startActivity(
                    Intent(requireContext(), LivePollRunningActivity::class.java)
                        .putExtra(AppConstants.SHOW_SUBMIT, showButton)
                        .putExtra(AppConstants.ID, it.id)
                )
            }, {
                shareIntent(
                    "Participate in Live Poll and Win Prizes:\n\n" + it.name,
                    requireContext()
                )
                //browserIntent(this, it.website!!)
                //startActivity(Intent(this, QuizAndContestWinnerActivity::class.java))
            }, {
                //browserIntent(this, it.website!!)
                //startActivity(Intent(this, QuizAndContestWinnerActivity::class.java))
            }
        )

        rvPollAndSurvey.adapter = govtWorkNewsAdapter
    }

    private fun addItems(quizList: ArrayList<LivePollListResponse.LivePoll>) {
        govtWorkNewsAdapter.setItem(quizList)
    }

    override fun onResume() {
        super.onResume()
        (activity as HomeActivity).setToolbarTitle(getString(R.string.live_poll))
    }
}