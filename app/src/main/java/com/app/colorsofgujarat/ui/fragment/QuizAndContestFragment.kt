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
import com.app.colorsofgujarat.adapter.QuizAndContestAdapter
import com.app.colorsofgujarat.apputils.EndlessRecyclerOnScrollListener
import com.app.colorsofgujarat.apputils.isConnected
import com.app.colorsofgujarat.apputils.shareIntent
import com.app.colorsofgujarat.apputils.showSnackBar
import com.app.colorsofgujarat.databinding.FragmentQuizAndCommentBinding
import com.app.colorsofgujarat.pojo.QuizAndContestResponse
import com.app.colorsofgujarat.ui.activity.HomeActivity
import com.app.colorsofgujarat.ui.activity.QuizAndContestRunningActivity
import com.app.colorsofgujarat.ui.activity.QuizAndContestWinnerActivity
import com.app.colorsofgujarat.viewmodel.QuizAndContestViewModel

class QuizAndContestFragment : Fragment() {

    private var _binding: FragmentQuizAndCommentBinding? = null
    private val binding get() = _binding!!

    private lateinit var govtWorkNewsAdapter: QuizAndContestAdapter
    private var totalRecords = 0
    private var loading = false
    private var pageNo = 0
    private lateinit var settingsViewModel: QuizAndContestViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        //return inflater.inflate(R.layout.fragment_quiz_and_comment, container, false)
        _binding = FragmentQuizAndCommentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupList()

        settingsViewModel = ViewModelProvider(this).get(QuizAndContestViewModel::class.java)

        settingsViewModel.quizAndContest().observe(requireActivity(), {
            handleResponse(it)
        })

        if (isConnected(requireContext())) {
            loading = true
            binding.rvPollAndSurvey.visibility = View.GONE
            binding.pbQuizAndContest.visibility = View.VISIBLE
            settingsViewModel.getQuizAndContest(pageNo.toString(), "10")
        } else {
            showSnackBar(getString(R.string.no_internet), requireActivity())
        }
    }

    private fun handleResponse(quizAndContestResponse: QuizAndContestResponse) {
        loading = false
        binding.rvPollAndSurvey.visibility = View.VISIBLE
        binding.pbQuizAndContest.visibility = View.GONE
        when {
            quizAndContestResponse.quiz_list.isNotEmpty() -> {
                totalRecords = quizAndContestResponse.total_records
                addItems(quizAndContestResponse.quiz_list)
            }
            pageNo == 0 -> {
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
        val layoutManager = LinearLayoutManager(requireContext())
        binding.rvPollAndSurvey.layoutManager = layoutManager

        binding.rvPollAndSurvey.addOnScrollListener(object :
            EndlessRecyclerOnScrollListener(layoutManager, 3) {
            override fun onLoadMore() {
                if (!loading && totalRecords != govtWorkNewsAdapter.itemCount) {
                    loading = true

                    pageNo += 10

                    settingsViewModel.getQuizAndContest(pageNo.toString(), "10")
                }
            }
        })

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
        binding.rvPollAndSurvey.adapter = govtWorkNewsAdapter
    }

    override fun onResume() {
        super.onResume()
        (activity as HomeActivity).setToolbarTitle(getString(R.string.quiz_and_contest))
    }

}