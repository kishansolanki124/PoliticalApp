package com.app.colorsofgujarat.ui.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import app.app.patidarsaurabh.apputils.AppConstants
import com.app.colorsofgujarat.R
import com.app.colorsofgujarat.adapter.QuestionsAndSuggestionAdapter
import com.app.colorsofgujarat.apputils.*
import com.app.colorsofgujarat.databinding.ActivityQuestionSuggestionBinding
import com.app.colorsofgujarat.pojo.SettingsResponse
import com.app.colorsofgujarat.pojo.UserAdviseResponse
import com.app.colorsofgujarat.viewmodel.UserAdviseViewModel

class QuestionSuggestionActivity : AppCompatActivity() {

    private lateinit var govtWorkNewsAdapter: QuestionsAndSuggestionAdapter
    private var totalRecords = 0
    private var loading = false
    private var pageNo = 0
    private lateinit var govtWorkViewModel: UserAdviseViewModel
    private var districtId = ""
    private var districtPosition = 0
    private var districtList: ArrayList<SettingsResponse.District> = ArrayList()
    private lateinit var binding: ActivityQuestionSuggestionBinding

//    override val layoutId: Int
//        get() = R.layout.activity_question_suggestion

    private var resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                result.data?.let {
                    if (it.getBooleanExtra(AppConstants.REFRESH, false)) {
                        getNews()
                    }
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQuestionSuggestionBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.commonToolbar.tvTitle.visibility = View.VISIBLE
        binding.commonToolbar.tvTitle.text = getString(R.string.que_suggestion)
        title = ""
        binding.commonToolbar.ibBack.setOnClickListener {
            hideKeyboard(this)
            onBackPressed()
        }

        //setToolbarTitle(getString(R.string.que_suggestion))
        setupList()
        setupDistrictSpinner()

        govtWorkViewModel = ViewModelProvider(this).get(UserAdviseViewModel::class.java)

        govtWorkViewModel.userAdviseList().observe(this, {
            handleResponse(it)
        })

        binding.tvAddQuestionSuggestion.setOnClickListener {
            resultLauncher.launch(
                Intent(this, AddQuestionSuggestionActivity::class.java)
                    .putExtra(AppConstants.DISTRICTID, districtPosition)
            )
        }
    }

    private fun handleResponse(userAdviseResponse: UserAdviseResponse?) {
        loading = false
        binding.rvPollAndSurvey.visibility = View.VISIBLE
        binding.pbQuestionSuggestion.visibility = View.GONE

        if (null != userAdviseResponse) {
            when {
                userAdviseResponse.user_advice_list.isNotEmpty() -> {
                    totalRecords = userAdviseResponse.total_records
                    addItems(userAdviseResponse.user_advice_list)
                }
                pageNo == 0 -> {
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

        val layoutManager = LinearLayoutManager(this)
        binding.rvPollAndSurvey.layoutManager = layoutManager

        binding.rvPollAndSurvey.addOnScrollListener(object :
            EndlessRecyclerOnScrollListener(layoutManager, 3) {
            override fun onLoadMore() {
                if (!loading && totalRecords != govtWorkNewsAdapter.itemCount) {
                    loading = true

                    pageNo += 10

                    govtWorkViewModel.getUserAdviseList(districtId, pageNo.toString(), "10")
                }
            }
        })

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
        binding.rvPollAndSurvey.adapter = govtWorkNewsAdapter
    }

    private fun setupDistrictSpinner() {
        districtList.addAll(SPreferenceManager.getInstance(this).settings.district_list)

        val adapter: ArrayAdapter<SettingsResponse.District> = ArrayAdapter(
            applicationContext,
            R.layout.simple_spinner_dropdown_item_black,
            districtList
        )

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spDistrictQuestionSuggestion.adapter = adapter

        binding.spDistrictQuestionSuggestion.setSelection(getUserSelectedDistrictIndex())

        binding.spDistrictQuestionSuggestion.onItemSelectedListener =
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
            pageNo = 0
            loading = true
            govtWorkNewsAdapter.reset()
            binding.pbQuestionSuggestion.visibility = View.VISIBLE
            binding.rvPollAndSurvey.visibility = View.GONE
            govtWorkViewModel.getUserAdviseList(districtId, pageNo.toString(), "10")
        } else {
            showSnackBar(getString(R.string.no_internet))
        }
    }

    private fun addItems(govWorkList: ArrayList<UserAdviseResponse.UserAdvice>) {
        govtWorkNewsAdapter.setItem(govWorkList)
    }
}