package com.app.colorsofgujarat.ui.activity

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.lifecycle.Lifecycle
import androidx.viewpager.widget.ViewPager
import com.app.colorsofgujarat.R
import com.app.colorsofgujarat.apputils.SPreferenceManager
import com.app.colorsofgujarat.apputils.getUserSelectedDistrictIndex
import com.app.colorsofgujarat.apputils.hideKeyboard
import com.app.colorsofgujarat.apputils.showProgressDialog
import com.app.colorsofgujarat.databinding.ActivityPollAndSurveyBinding
import com.app.colorsofgujarat.pojo.PopupBannerResponse
import com.app.colorsofgujarat.pojo.SettingsResponse
import com.app.colorsofgujarat.ui.fragment.PollAndSurveyFragment
import com.app.colorsofgujarat.ui.fragment.PollAndSurveyResultFragment

class PollAndSurveyActivity : AppCompatActivity() {

    private var handler: Handler? = null
    private var runnableCode: Runnable? = null
    private lateinit var viewPagerShraddhanjaliAdapter: ViewPagerShraddhanjaliAdapter
    private var districtId = ""
    private var districtList: ArrayList<SettingsResponse.District> = ArrayList()
    private lateinit var viewPager: ViewPager
    private lateinit var binding: ActivityPollAndSurveyBinding

//    override val layoutId: Int
//        get() = R.layout.activity_poll_and_survey

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPollAndSurveyBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.commonToolbar.tvTitle.visibility = View.VISIBLE
        binding.commonToolbar.tvTitle.text = "પોલ અને સર્વે"
        title = ""
        binding.commonToolbar.ibBack.setOnClickListener {
            hideKeyboard(this)
            onBackPressed()
        }

        //setToolbarTitle("પોલ અને સર્વે")
        setupDistrictSpinner()

        viewPagerShraddhanjaliAdapter =
            ViewPagerShraddhanjaliAdapter(supportFragmentManager)
        viewPager = findViewById(R.id.pager)
        viewPager.adapter = viewPagerShraddhanjaliAdapter

        if (!SPreferenceManager.getInstance(this).banners.popup_banner.isNullOrEmpty()) {
            setupRepeatableBannerAd(
                SPreferenceManager.getInstance(this).banners.delay_time,
                SPreferenceManager.getInstance(this).banners.initial_time,
                SPreferenceManager.getInstance(this).banners.popup_banner
            )
        }
    }

    // Since this is an object collection, use a FragmentStatePagerAdapter,
    // and NOT a FragmentPagerAdapter.
    class ViewPagerShraddhanjaliAdapter(fm: FragmentManager) :
        FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

        override fun getCount(): Int = 2

        override fun getItem(i: Int): Fragment {

            return when (i) {
                0 -> {
                    PollAndSurveyFragment()
                }
                1 -> {
                    PollAndSurveyResultFragment()
                }
                else -> {
                    PollAndSurveyFragment()
                }
            }
        }

        override fun getPageTitle(position: Int): CharSequence {
            //return "OBJECT ${(position + 1)}"
            return if (position == 0) {
                "પોલ અને સર્વે"
            } else if (position == 1) {
                "પરિણામ"
            } else {
                "પોલ અને સર્વે"
            }
        }
    }

    private fun setupDistrictSpinner() {
        districtList.addAll(SPreferenceManager.getInstance(this).settings.district_list)

        val adapter: ArrayAdapter<SettingsResponse.District> = ArrayAdapter(
            applicationContext,
            R.layout.simple_spinner_dropdown_item_black,
            districtList
        )

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spDistrictPollAndSurvey.adapter = adapter

        binding.spDistrictPollAndSurvey.setSelection(getUserSelectedDistrictIndex())

        binding.spDistrictPollAndSurvey.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View,
                    position: Int,
                    id: Long
                ) {
                    val district: SettingsResponse.District =
                        parent.selectedItem as SettingsResponse.District
                    districtId = district.id

                    //refreshing fragments
//                    val fm = supportFragmentManager
//                    val fragment: PollAndSurveyFragment? =
//                        fm.findFragmentById(R.id.tab_layout) as PollAndSurveyFragment?
//                    fragment?.refreshPollAndSurvey()

                    val fragments = supportFragmentManager.fragments
                    for (fragment in fragments) {
                        if (fragment is PollAndSurveyFragment) {
                            fragment.refreshPollAndSurvey()
                        }
                        if (fragment is PollAndSurveyResultFragment) {
                            fragment.refreshPollAndSurveyResult()
                        }
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }
    }

    fun getDistrictId(): String {
        return districtId
    }

    private fun setupRepeatableBannerAd(
        delayTime: String,
        initialTime: String,
        popupBanner: List<PopupBannerResponse.PopupBanner>
    ) {
        handler = Handler(Looper.getMainLooper())
        runnableCode = object : Runnable {
            override fun run() {
                if (!isDestroyed && !this@PollAndSurveyActivity.isFinishing) {
                    if (this@PollAndSurveyActivity.lifecycle.currentState
                            .isAtLeast(Lifecycle.State.RESUMED)
                    ) {
                        showProgressDialog(this@PollAndSurveyActivity, popupBanner)
                    }
                    handler?.postDelayed(this, delayTime.toLong() * 1000)
                }
            }
        }

        if (!isDestroyed && !this.isFinishing) {
            runnableCode?.let {
                handler?.postDelayed(it, initialTime.toLong() * 1000)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (null != handler) {
            runnableCode?.let {
                handler!!.removeCallbacks(it)
            }
        }
    }
}