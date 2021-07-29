package com.politics.politicalapp.ui.activity

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.ViewPager
import com.politics.politicalapp.R
import com.politics.politicalapp.apputils.SPreferenceManager
import com.politics.politicalapp.apputils.getUserSelectedDistrictIndex
import com.politics.politicalapp.pojo.SettingsResponse
import com.politics.politicalapp.ui.fragment.PollAndSurveyFragment
import com.politics.politicalapp.ui.fragment.PollAndSurveyResultFragment
import kotlinx.android.synthetic.main.activity_poll_and_survey.*


class PollAndSurveyActivity : ExtendedToolbarActivity() {

    private lateinit var viewPagerShraddhanjaliAdapter: ViewPagerShraddhanjaliAdapter
    private var districtId = ""
    private var districtList: ArrayList<SettingsResponse.District> = ArrayList()
    private lateinit var viewPager: ViewPager

    override val layoutId: Int
        get() = R.layout.activity_poll_and_survey

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setToolbarTitle("પોલ અને સર્વે")
        setupDistrictSpinner()

        viewPagerShraddhanjaliAdapter =
            ViewPagerShraddhanjaliAdapter(supportFragmentManager)
        viewPager = findViewById(R.id.pager)
        viewPager.adapter = viewPagerShraddhanjaliAdapter
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
        spDistrictPollAndSurvey.adapter = adapter

        spDistrictPollAndSurvey.setSelection(getUserSelectedDistrictIndex())

        spDistrictPollAndSurvey.onItemSelectedListener =
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
}