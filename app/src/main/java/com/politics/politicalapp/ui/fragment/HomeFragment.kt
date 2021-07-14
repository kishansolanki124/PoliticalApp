package com.politics.politicalapp.ui.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import app.app.patidarsaurabh.apputils.AppConstants
import com.politics.politicalapp.R
import com.politics.politicalapp.adapter.BreakingNewsAdapter
import com.politics.politicalapp.apputils.SPreferenceManager
import com.politics.politicalapp.apputils.isConnected
import com.politics.politicalapp.apputils.setShowSideItems
import com.politics.politicalapp.apputils.showSnackBar
import com.politics.politicalapp.pojo.SettingsResponse
import com.politics.politicalapp.ui.activity.*
import com.politics.politicalapp.viewmodel.SettingsViewModel
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : Fragment() {

    private lateinit var settingsViewModel: SettingsViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupListeners()
        setupPoints()
    }

    private fun setupPoints() {
        settingsViewModel = ViewModelProvider(this).get(SettingsViewModel::class.java)

        settingsViewModel.settingsResponse().observe(requireActivity(), {
            handleResponse(it)
        })

        fetchSettings()
    }

    private fun setupListeners() {

        llQueSuggestion.setOnClickListener {
            startActivity(Intent(requireContext(), QuestionSuggestionActivity::class.java))
        }

        llDailYScratch.setOnClickListener {
            startActivity(Intent(requireContext(), DailyScratchAndWnActivity::class.java))
        }

        llQuizAndContest.setOnClickListener {
            startActivity(Intent(requireContext(), QuizAndContestActivity::class.java))
        }

        llGovtWork.setOnClickListener {
            startActivity(Intent(requireContext(), GovtWorkActivity::class.java))
        }

        llDharasabhyo.setOnClickListener {
            startActivity(Intent(requireContext(), DharasabhyoListActivity::class.java))
        }

        llPollAndSurvey.setOnClickListener {
            startActivity(Intent(requireContext(), PollAndSurveyActivity::class.java))
        }
    }

    private fun setupRecyclerView() {

        val stringList: ArrayList<String> = ArrayList()
        stringList.add("S")
        stringList.add("S")
        stringList.add("S")
        stringList.add("S")
        stringList.add("S")

        val adapter = BreakingNewsAdapter {
//            startActivity(
//                Intent(
//                    requireActivity(), NewsDetailsActivity::class.java
//                ).putExtra(AppConstants.NEWS_ID, it.id)
//            )
        }

        adapter.setItem(stringList)

//        rvNewsHome.adapter = adapter
//
//        rvNewsHome.addOnScrollListener(object : RecyclerView.OnScrollListener() {
//            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
//                super.onScrolled(recyclerView, dx, dy)
//                val offset: Int = rvNewsHome.computeHorizontalScrollOffset()
//                if (offset % rvNewsHome.width == 0) {
//                    val position: Int = rvNewsHome
//                    Log.e("Current position is", position.toString())
//                    requireActivity().showToast(position.toString())
//                }
//            }
//        })

        //showing next page's partial visibility
        newsHomeViewPager.setShowSideItems(50, 0)

        newsHomeViewPager.adapter = adapter

        newsHomeViewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageScrollStateChanged(state: Int) {
                println(state)
            }

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels)
                println(position)
            }


            @SuppressLint("SetTextI18n")
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                tvNewsIndex.text = (position + 1).toString() + "/" + stringList.size
            }
        })
    }

    private fun handleResponse(settingsResponse: SettingsResponse?) {
        pbHome.visibility = View.GONE
        tvUserPoints.visibility = View.VISIBLE
        if (null != settingsResponse) {
            SPreferenceManager.getInstance(requireContext()).saveSettings(settingsResponse)
            setupPointViews()
        } else {
            showSnackBar(getString(R.string.something_went_wrong), requireActivity())
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setupPointViews() {
        tvUserName.text =
            "Hi, " + SPreferenceManager.getInstance(requireContext())
                .getString(AppConstants.NAME, "")
        tvUserPoints.text = SPreferenceManager.getInstance(requireContext())
            .settings.user_points
    }

    private fun fetchSettings() {
        if (isConnected(requireContext())) {
            pbHome.visibility = View.VISIBLE
            tvUserPoints.visibility = View.INVISIBLE
            settingsViewModel.getSettings(SPreferenceManager.getInstance(requireContext()).session)
        } else {
            showSnackBar(getString(R.string.no_internet), requireActivity())
        }
    }
}