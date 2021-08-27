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
import com.app.colorsofgujarat.databinding.FragmentHomeBinding
import com.app.colorsofgujarat.pojo.SettingsResponse
import com.app.colorsofgujarat.ui.activity.*
import com.app.colorsofgujarat.viewmodel.SettingsViewModel
import com.bumptech.glide.Glide
import com.elconfidencial.bubbleshowcase.BubbleShowCaseBuilder

class HomeFragment : Fragment() {

    private lateinit var settingsViewModel: SettingsViewModel
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        //return inflater.inflate(R.layout.fragment_home, container, false)
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
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
        binding.ivPointQuestion.setOnClickListener {
            showInfoWindow()
        }

        binding.tvUserPoints.setOnClickListener {
            showInfoWindow()
        }

        binding.ivEditUser.setOnClickListener {
            startActivity(Intent(requireContext(), UpdateProfileActivity::class.java))
        }

        binding.llQueSuggestion.setOnClickListener {
            startActivity(Intent(requireContext(), QuestionSuggestionActivity::class.java))
        }

        binding.llDailYScratch.setOnClickListener {
            startActivity(Intent(requireContext(), DailySpinAndWinActivity::class.java))
        }

        binding.llQuizAndContest.setOnClickListener {
            startActivity(Intent(requireContext(), ContestDynamicActivity::class.java))
        }

        binding.llGovtWork.setOnClickListener {
            startActivity(Intent(requireContext(), GovtWorkActivity::class.java))
        }

        binding.llDharasabhyo.setOnClickListener {
            startActivity(Intent(requireContext(), DharasabhyoListActivity::class.java))
        }

        binding.llPollAndSurvey.setOnClickListener {
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

        binding.newsHomeViewPager.apply {
            clipToPadding = false   // allow full width shown with padding
            clipChildren = false    // allow left/right item is not clipped
            offscreenPageLimit = 2  // make sure left/right item is rendered
        }

        //increase this offset to show more of left/right
        val offsetPx =
            resources.getDimension(R.dimen.dp_30).toInt().dpToPx(resources.displayMetrics)
        binding.newsHomeViewPager.setPadding(0, 0, offsetPx, 0)

        //increase this offset to increase distance between 2 items
        val pageMarginPx =
            resources.getDimension(R.dimen.dp_5).toInt().dpToPx(resources.displayMetrics)
        val marginTransformer = MarginPageTransformer(pageMarginPx)
        binding.newsHomeViewPager.setPageTransformer(marginTransformer)

        binding.newsHomeViewPager.adapter = breakingNewsAdapter

        binding.newsHomeViewPager.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
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
                binding.tvNewsIndex.text = (position + 1).toString() + "/" + newsList.size
            }
        })
    }

    private fun handleResponse(settingsResponse: SettingsResponse?) {
        binding.pbHome.visibility = View.GONE
        binding.pbHomeNews.visibility = View.GONE
        binding.tvUserPoints.visibility = View.VISIBLE
        binding.newsHomeViewPager.visibility = View.VISIBLE
        if (null != settingsResponse) {
            SPreferenceManager.getInstance(requireContext()).saveSettings(settingsResponse)

            if (settingsResponse.notification_count == "0") {
                //hide unread count
                (activity as HomeActivity).hideUnreadCount()
            } else {
                //show unread count
                (activity as HomeActivity).showUnreadCount()
            }

            binding.tvMenuDynamic.text = settingsResponse.contest[0].menu_name

            Glide.with(this)
                .load(settingsResponse.contest[0].menu_icon)
                .into(binding.ivDynamicMenu)

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
        binding.tvUserName.text =
            "Hi, " + SPreferenceManager.getInstance(requireContext())
                .settings.user_name
        binding.tvUserPoints.text = SPreferenceManager.getInstance(requireContext())
            .settings.user_points
    }

    private fun fetchSettings() {
        if (isConnected(requireContext())) {
            binding.pbHome.visibility = View.VISIBLE
            binding.pbHomeNews.visibility = View.VISIBLE
            binding.tvUserPoints.visibility = View.INVISIBLE
            binding.newsHomeViewPager.visibility = View.INVISIBLE
            settingsViewModel.getSettings(SPreferenceManager.getInstance(requireContext()).session)
        } else {
            showSnackBar(getString(R.string.no_internet), requireActivity())
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onResume() {
        super.onResume()

        (activity as HomeActivity).setToolbarTitle(getString(R.string.colors_of_guj))

        binding.tvUserPoints.text = SPreferenceManager.getInstance(requireContext())
            .settings.user_points
        binding.tvUserName.text =
            "Hi, " + SPreferenceManager.getInstance(requireContext())
                .settings.user_name
    }

    private fun showInfoWindow() {
        BubbleShowCaseBuilder(requireActivity()) //Activity instance
            .title(SPreferenceManager.getInstance(requireContext()).settings.settings[0].points_tooltip)
            //.description("Lorem Ipsum is simply dummy text of the printing and typesetting") //Any title for the bubble view
            .targetView(binding.ivPointQuestion) //View to point out
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