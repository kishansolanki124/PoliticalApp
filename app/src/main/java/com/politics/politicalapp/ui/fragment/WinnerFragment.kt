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
import com.politics.politicalapp.adapter.WinnerAdapter
import com.politics.politicalapp.apputils.isConnected
import com.politics.politicalapp.apputils.showSnackBar
import com.politics.politicalapp.pojo.WinnerListResponse
import com.politics.politicalapp.ui.activity.WinnerNamesActivity
import com.politics.politicalapp.ui.activity.WinnerPrizeDetailActivity
import com.politics.politicalapp.viewmodel.WinnerViewModel
import kotlinx.android.synthetic.main.fragment_winner.*

class WinnerFragment : Fragment() {

    private lateinit var govtWorkNewsAdapter: WinnerAdapter
    private var startPage = 0
    private lateinit var settingsViewModel: WinnerViewModel
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_winner, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupList()

        settingsViewModel = ViewModelProvider(this).get(WinnerViewModel::class.java)

        settingsViewModel.winnerList().observe(requireActivity(), {
            handleResponse(it)
        })

        if (isConnected(requireContext())) {
            rvWinner.visibility = View.GONE
            pbWinnerList.visibility = View.VISIBLE
            settingsViewModel.getWinnerList(
                "0",
                "10"
            )
        } else {
            showSnackBar(getString(R.string.no_internet), requireActivity())
        }
    }

    private fun setupList() {
        val stringList: ArrayList<String> = ArrayList()
        stringList.add("S")
        stringList.add("S")
        stringList.add("S")
        stringList.add("S")
        stringList.add("S")

        govtWorkNewsAdapter = WinnerAdapter(
            { item, showButton ->
                //callIntent(this, it.contact_no!!)
                startActivity(
                    Intent(requireContext(), WinnerPrizeDetailActivity::class.java)
                        .putExtra(AppConstants.ID, item.id)
                )

            }, {
                //browserIntent(this, it.website!!)
                startActivity(
                    //Intent(requireContext(), ::class.java)
                    Intent(requireContext(), WinnerNamesActivity::class.java)
                        .putExtra(AppConstants.ID, it.id)
                )
            }
        )
        rvWinner.adapter = govtWorkNewsAdapter
    }

    private fun handleResponse(livePollListResponse: WinnerListResponse?) {
        if (null != livePollListResponse) {
            rvWinner.visibility = View.VISIBLE
            pbWinnerList.visibility = View.GONE
            when {
                livePollListResponse.points_prize_list.isNotEmpty() -> {
                    addItems(livePollListResponse.points_prize_list)
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

    private fun addItems(quizList: ArrayList<WinnerListResponse.PointsPrize>) {
        govtWorkNewsAdapter.setItem(quizList)
    }
}