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
import com.politics.politicalapp.ui.fragment.PollAndSurveyFragment
import com.politics.politicalapp.ui.fragment.PollAndSurveyResultFragment
import kotlinx.android.synthetic.main.activity_dharasabhyo.*

class PollAndSurveyActivity : ExtendedToolbarActivity() {

    private lateinit var viewPagerShraddhanjaliAdapter: ViewPagerShraddhanjaliAdapter
    private lateinit var viewPager: ViewPager

    override val layoutId: Int
        get() = R.layout.activity_poll_and_survey

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setToolbarTitle("પોલ અને સર્વે")

        viewPagerShraddhanjaliAdapter =
            ViewPagerShraddhanjaliAdapter(supportFragmentManager)
        viewPager = findViewById(R.id.pager)
        viewPager.adapter = viewPagerShraddhanjaliAdapter

        setupDistrictSpinner()
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
        val cityList: ArrayList<String> = ArrayList()
        cityList.add("તમારો જિલ્લો પસંદ કરો")
        cityList.add("તમારો જિલ્લો પસંદ કરો")
        cityList.add("તમારો જિલ્લો પસંદ કરો")
        cityList.add("તમારો જિલ્લો પસંદ કરો")

        val adapter: ArrayAdapter<String> = ArrayAdapter(
            applicationContext,
            R.layout.simple_spinner_dropdown_black_item,
            cityList
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        spDistrict.adapter = adapter

        spDistrict.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {

            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                return
            }
        }
    }
}