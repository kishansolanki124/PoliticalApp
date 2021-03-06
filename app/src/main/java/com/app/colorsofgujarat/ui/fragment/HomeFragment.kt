package com.app.colorsofgujarat.ui.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import app.app.patidarsaurabh.apputils.AppConstants
import com.app.colorsofgujarat.R
import com.app.colorsofgujarat.adapter.BreakingNewsAdapter
import com.app.colorsofgujarat.apputils.SPreferenceManager
import com.app.colorsofgujarat.apputils.dpToPx
import com.app.colorsofgujarat.apputils.isConnected
import com.app.colorsofgujarat.apputils.showSnackBar
import com.app.colorsofgujarat.pojo.SettingsResponse
import com.app.colorsofgujarat.ui.activity.*
import com.app.colorsofgujarat.viewmodel.SettingsViewModel
import com.bumptech.glide.Glide
import com.elconfidencial.bubbleshowcase.BubbleShowCaseBuilder
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
        ivPointQuestion.setOnClickListener {
            showInfoWindow()
        }

        tvUserPoints.setOnClickListener {
            showInfoWindow()
        }

        ivEditUser.setOnClickListener {
            startActivity(Intent(requireContext(), UpdateProfileActivity::class.java))
        }

        llQueSuggestion.setOnClickListener {
            startActivity(Intent(requireContext(), QuestionSuggestionActivity::class.java))
        }

        llDailYScratch.setOnClickListener {
            startActivity(Intent(requireContext(), DailySpinAndWinActivity::class.java))
        }

        llQuizAndContest.setOnClickListener {
            startActivity(Intent(requireContext(), ContestDynamicActivity::class.java))
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

    private fun setupNewsViewPager(newsList: List<SettingsResponse.News>) {
        val breakingNewsAdapter = BreakingNewsAdapter {
            startActivity(
                Intent(
                    requireActivity(), NewsDetailActivity::class.java
                ).putExtra(AppConstants.ID, it.id)
            )
        }

        breakingNewsAdapter.setItem(newsList)

        newsHomeViewPager.apply {
            clipToPadding = false   // allow full width shown with padding
            clipChildren = false    // allow left/right item is not clipped
            offscreenPageLimit = 2  // make sure left/right item is rendered
        }

        //increase this offset to show more of left/right
        val offsetPx =
            resources.getDimension(R.dimen.dp_30).toInt().dpToPx(resources.displayMetrics)
        newsHomeViewPager.setPadding(0, 0, offsetPx, 0)

        //increase this offset to increase distance between 2 items
        val pageMarginPx =
            resources.getDimension(R.dimen.dp_5).toInt().dpToPx(resources.displayMetrics)
        val marginTransformer = MarginPageTransformer(pageMarginPx)
        newsHomeViewPager.setPageTransformer(marginTransformer)

        newsHomeViewPager.adapter = breakingNewsAdapter

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
                tvNewsIndex.text = (position + 1).toString() + "/" + newsList.size
            }
        })
    }

    private fun handleResponse(settingsResponse: SettingsResponse?) {
        pbHome.visibility = View.GONE
        pbHomeNews.visibility = View.GONE
        tvUserPoints.visibility = View.VISIBLE
        newsHomeViewPager.visibility = View.VISIBLE
        if (null != settingsResponse) {
            SPreferenceManager.getInstance(requireContext()).saveSettings(settingsResponse)

            if (settingsResponse.notification_count == "0") {
                //hide unread count
                (activity as HomeActivity).hideUnreadCount()
            } else {
                //show unread count
                (activity as HomeActivity).showUnreadCount()
            }

            tvMenuDynamic.text = settingsResponse.contest[0].menu_name

            Glide.with(this)
                .load(settingsResponse.contest[0].menu_icon)
                .into(ivDynamicMenu)

//            Glide.with(this).load(settingsResponse.contest[0].menu_icon).into(object :
//                CustomTarget<Drawable>() {
//                override fun onLoadCleared(placeholder: Drawable?) {
//
//                }
//
//                override fun onResourceReady(
//                    resource: Drawable,
//                    transition: Transition<in Drawable>?
//                ) {
//                    llQuizAndContest.background = resource
//                }
//            })

            setupPointViews()
            setupNewsViewPager(settingsResponse.news_list)
        } else {
            showSnackBar(getString(R.string.something_went_wrong), requireActivity())
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setupPointViews() {
        tvUserName.text =
            "Hi, " + SPreferenceManager.getInstance(requireContext())
                .settings.user_name
        tvUserPoints.text = SPreferenceManager.getInstance(requireContext())
            .settings.user_points
    }

    private fun fetchSettings() {
        if (isConnected(requireContext())) {
            pbHome.visibility = View.VISIBLE
            pbHomeNews.visibility = View.VISIBLE
            tvUserPoints.visibility = View.INVISIBLE
            newsHomeViewPager.visibility = View.INVISIBLE
            settingsViewModel.getSettings(SPreferenceManager.getInstance(requireContext()).session)
        } else {
            showSnackBar(getString(R.string.no_internet), requireActivity())
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onResume() {
        super.onResume()

        (activity as HomeActivity).setToolbarTitle(getString(R.string.colors_of_guj))

        tvUserPoints.text = SPreferenceManager.getInstance(requireContext())
            .settings.user_points
        tvUserName.text =
            "Hi, " + SPreferenceManager.getInstance(requireContext())
                .settings.user_name
    }

    private fun showInfoWindow() {
        BubbleShowCaseBuilder(requireActivity()) //Activity instance
            .title(SPreferenceManager.getInstance(requireContext()).settings.settings[0].points_tooltip)
            //.description("Lorem Ipsum is simply dummy text of the printing and typesetting") //Any title for the bubble view
            .targetView(ivPointQuestion) //View to point out
            .backgroundColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.red_CC252C
                )
            ) //Bubble background color
            .textColor(ContextCompat.getColor(requireContext(), R.color.white)) //Bubble Text color
//            .titleTextSize(R.dimen.sp_14) //Title text size in SP (default value 16sp)
            .show() //Display the ShowCase
    }
}