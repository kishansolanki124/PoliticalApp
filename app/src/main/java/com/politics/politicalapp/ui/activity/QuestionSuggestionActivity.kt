package com.politics.politicalapp.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.politics.politicalapp.R
import com.politics.politicalapp.adapter.QuestionsAndSuggestionAdapter
import kotlinx.android.synthetic.main.activity_dharasabhyo.spDistrict
import kotlinx.android.synthetic.main.activity_question_suggestion.*

class QuestionSuggestionActivity : ExtendedToolbarActivity() {

    private lateinit var govtWorkNewsAdapter: QuestionsAndSuggestionAdapter

    override val layoutId: Int
        get() = R.layout.activity_question_suggestion

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setToolbarTitle(getString(R.string.que_suggestion))
        setupList()
        setupDistrictSpinner()
    }

    private fun setupList() {
        val stringList: ArrayList<String> = ArrayList()
        stringList.add("S")
        stringList.add("S")
        stringList.add("S")
        stringList.add("S")
        stringList.add("S")

        govtWorkNewsAdapter = QuestionsAndSuggestionAdapter(
            {
                //callIntent(this, it.contact_no!!)
                startActivity(Intent(this, QuestionSuggestionDetailActivity::class.java))
            }, {
                //browserIntent(this, it.website!!)
            }
        )
        govtWorkNewsAdapter.setItem(stringList)
        rvPollAndSurvey.adapter = govtWorkNewsAdapter
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