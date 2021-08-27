package com.app.colorsofgujarat.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import app.app.patidarsaurabh.apputils.AppConstants
import com.app.colorsofgujarat.R
import com.app.colorsofgujarat.adapter.LivePollAdapter
import com.app.colorsofgujarat.apputils.*
import com.app.colorsofgujarat.databinding.FragmentLivePollBinding
import com.app.colorsofgujarat.pojo.LivePollListResponse
import com.app.colorsofgujarat.ui.activity.HomeActivity
import com.app.colorsofgujarat.ui.activity.LivePollRunningActivity
import com.app.colorsofgujarat.viewmodel.LivePollViewModel

class LivePollFragment : Fragment() {

    private lateinit var govtWorkNewsAdapter: LivePollAdapter
    private var totalRecords = 0
    private var loading = false
    private var pageNo = 0
    private lateinit var settingsViewModel: LivePollViewModel
    private var _binding: FragmentLivePollBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        //return inflater.inflate(R.layout.fragment_live_poll, container, false)
        _binding = FragmentLivePollBinding.inflate(inflater, container, false)
        return binding.root
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
            loading = true
            binding.rvPollAndSurvey.visibility = View.GONE
            binding.pbLivePoll.visibility = View.VISIBLE
            settingsViewModel.getLivePollList(
                SPreferenceManager.getInstance(requireContext()).session,
                pageNo.toString(),
                "10"
            )
        } else {
            showSnackBar(getString(R.string.no_internet), requireActivity())
        }
    }

    private fun handleResponse(livePollListResponse: LivePollListResponse?) {
        loading = false
        binding.rvPollAndSurvey.visibility = View.VISIBLE
        binding.pbLivePoll.visibility = View.GONE

        if (null != livePollListResponse) {
            when {
                livePollListResponse.live_poll_list.isNotEmpty() -> {
                    totalRecords = livePollListResponse.total_records
                    addItems(livePollListResponse.live_poll_list)
                }
                pageNo == 0 -> {
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
        val layoutManager = LinearLayoutManager(requireContext())
        binding.rvPollAndSurvey.layoutManager = layoutManager

        binding.rvPollAndSurvey.addOnScrollListener(object :
            EndlessRecyclerOnScrollListener(layoutManager, 3) {
            override fun onLoadMore() {
                if (!loading && totalRecords != govtWorkNewsAdapter.itemCount) {
                    loading = true

                    pageNo += 10

                    settingsViewModel.getLivePollList(
                        SPreferenceManager.getInstance(requireContext()).session,
                        pageNo.toString(), "10"
                    )
                }
            }
        })

        govtWorkNewsAdapter = LivePollAdapter(
            { it, showButton ->
                //item click call
                startActivity(
                    Intent(requireContext(), LivePollRunningActivity::class.java)
                        .putExtra(AppConstants.SHOW_SUBMIT, showButton)
                        .putExtra(AppConstants.ID, it.id)
                )
            }, {
                //item click share
                shareIntent(
                    "Participate in Live Poll and Win Prizes:\n\n" + it.name,
                    requireContext()
                )
                //browserIntent(this, it.website!!)
                //startActivity(Intent(this, QuizAndContestWinnerActivity::class.java))
            }, {
                //item click web
            }
        )

        binding.rvPollAndSurvey.adapter = govtWorkNewsAdapter
    }

    private fun addItems(quizList: ArrayList<LivePollListResponse.LivePoll>) {
        govtWorkNewsAdapter.setItem(quizList)
    }

    override fun onResume() {
        super.onResume()
        (activity as HomeActivity).setToolbarTitle(getString(R.string.live_poll))
    }
}