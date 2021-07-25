package com.politics.politicalapp.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.lifecycle.ViewModelProvider
import app.app.patidarsaurabh.apputils.AppConstants
import com.politics.politicalapp.R
import com.politics.politicalapp.adapter.QuestionsAndSuggestionAdapter
import com.politics.politicalapp.apputils.SPreferenceManager
import com.politics.politicalapp.apputils.isConnected
import com.politics.politicalapp.apputils.shareIntent
import com.politics.politicalapp.apputils.showSnackBar
import com.politics.politicalapp.pojo.SettingsResponse
import com.politics.politicalapp.pojo.UserAdviseResponse
import com.politics.politicalapp.viewmodel.UserAdviseViewModel
import kotlinx.android.synthetic.main.activity_question_suggestion.*

class QuestionSuggestionActivity : ExtendedToolbarActivity() {

    private lateinit var govtWorkNewsAdapter: QuestionsAndSuggestionAdapter
    private var startPage = 0
    private lateinit var govtWorkViewModel: UserAdviseViewModel
    private var districtId = ""
    private var districtPosition = 0
    private var districtList: ArrayList<SettingsResponse.District> = ArrayList()

    override val layoutId: Int
        get() = R.layout.activity_question_suggestion

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setToolbarTitle(getString(R.string.que_suggestion))
        setupList()
        setupDistrictSpinner()

        govtWorkViewModel = ViewModelProvider(this).get(UserAdviseViewModel::class.java)

        govtWorkViewModel.userAdviseList().observe(this, {
            handleResponse(it)
        })

        tvAddQuestionSuggestion.setOnClickListener {
            startActivity(
                Intent(this, AddQuestionSuggestionActivity::class.java)
                    .putExtra(AppConstants.DISTRICTID, districtPosition)
            )
        }
    }

    private fun handleResponse(userAdviseResponse: UserAdviseResponse?) {
        rvPollAndSurvey.visibility = View.VISIBLE
        pbQuestionSuggestion.visibility = View.GONE

        if (null != userAdviseResponse) {
            when {
                userAdviseResponse.user_advice_list.isNotEmpty() -> {
                    addItems(userAdviseResponse.user_advice_list)
                }
                startPage == 0 -> {
                    govtWorkNewsAdapter.reset()
                    showSnackBar(getString(R.string.no_record_found), this)
                }
                else -> {
                    showSnackBar(getString(R.string.something_went_wrong), this)
                }
            }
        } else {
            showSnackBar(getString(R.string.something_went_wrong))
        }
    }

    private fun setupList() {
        govtWorkNewsAdapter = QuestionsAndSuggestionAdapter(
            {
                //callIntent(this, it.contact_no!!)
                startActivity(
                    Intent(this, QuestionSuggestionDetailActivity::class.java)
                        .putExtra(AppConstants.ID, it.id)
                )
            }, {
                //browserIntent(this, it.website!!)
                shareIntent(it.title, it.up_pro_img, this)
            }
        )
        rvPollAndSurvey.adapter = govtWorkNewsAdapter
    }

    private fun setupDistrictSpinner() {
        districtList.addAll(SPreferenceManager.getInstance(this).settings.district_list)

        val adapter: ArrayAdapter<SettingsResponse.District> = ArrayAdapter(
            applicationContext,
            R.layout.simple_spinner_dropdown_item_black,
            districtList
        )

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spDistrictQuestionSuggestion.adapter = adapter

        spDistrictQuestionSuggestion.onItemSelectedListener =
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
                    districtPosition = position
                    getNews()
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }
    }

    private fun getNews() {
        if (isConnected(this)) {
            pbQuestionSuggestion.visibility = View.VISIBLE
            rvPollAndSurvey.visibility = View.GONE
            govtWorkViewModel.getUserAdviseList(districtId, "0", "10")
        } else {
            showSnackBar(getString(R.string.no_internet))
        }
    }

    private fun addItems(govWorkList: ArrayList<UserAdviseResponse.UserAdvice>) {
        govtWorkNewsAdapter.setItem(govWorkList)
    }
}