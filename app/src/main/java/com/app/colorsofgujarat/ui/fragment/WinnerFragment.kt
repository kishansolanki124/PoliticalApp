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
import com.app.colorsofgujarat.adapter.WinnerAdapter
import com.app.colorsofgujarat.apputils.EndlessRecyclerOnScrollListener
import com.app.colorsofgujarat.apputils.isConnected
import com.app.colorsofgujarat.apputils.showSnackBar
import com.app.colorsofgujarat.pojo.WinnerListResponse
import com.app.colorsofgujarat.ui.activity.HomeActivity
import com.app.colorsofgujarat.ui.activity.WinnerNamesActivity
import com.app.colorsofgujarat.ui.activity.WinnerPrizeDetailActivity
import com.app.colorsofgujarat.viewmodel.WinnerViewModel
import kotlinx.android.synthetic.main.fragment_winner.*

class WinnerFragment : Fragment() {

    private lateinit var govtWorkNewsAdapter: WinnerAdapter
    private var totalRecords = 0
    private var loading = false
    private var pageNo = 0
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
            loading = true
            rvWinner.visibility = View.GONE
            pbWinnerList.visibility = View.VISIBLE
            settingsViewModel.getWinnerList(
                pageNo.toString(),
                "10"
            )
        } else {
            showSnackBar(getString(R.string.no_internet), requireActivity())
        }
    }

    private fun setupList() {

        val layoutManager = LinearLayoutManager(requireContext())
        rvWinner.layoutManager = layoutManager

        rvWinner.addOnScrollListener(object :
            EndlessRecyclerOnScrollListener(layoutManager, 3) {
            override fun onLoadMore() {
                if (!loading && totalRecords != govtWorkNewsAdapter.itemCount) {
                    loading = true

                    pageNo += 10

                    settingsViewModel.getWinnerList(
                        pageNo.toString(),
                        "10"
                    )
                }
            }
        })

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
        loading = false
        rvWinner.visibility = View.VISIBLE
        pbWinnerList.visibility = View.GONE

        if (null != livePollListResponse) {
            when {
                livePollListResponse.points_prize_list.isNotEmpty() -> {
                    totalRecords = livePollListResponse.total_records
                    addItems(livePollListResponse.points_prize_list)
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

    private fun addItems(quizList: ArrayList<WinnerListResponse.PointsPrize>) {
        govtWorkNewsAdapter.setItem(quizList)
    }

    override fun onResume() {
        super.onResume()
        (activity as HomeActivity).setToolbarTitle(getString(R.string.winner))
    }
}