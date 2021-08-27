package com.app.colorsofgujarat.ui.activity

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.colorsofgujarat.R
import com.app.colorsofgujarat.adapter.NotificationAdapter
import com.app.colorsofgujarat.apputils.*
import com.app.colorsofgujarat.databinding.ActivityNotificationBinding
import com.app.colorsofgujarat.pojo.NotificationResponse
import com.app.colorsofgujarat.viewmodel.UserAdviseViewModel

class NotificationActivity : AppCompatActivity() {

    private lateinit var govtWorkNewsAdapter: NotificationAdapter
    private var totalRecords = 0
    private var loading = false
    private var pageNo = 0
    private lateinit var govtWorkViewModel: UserAdviseViewModel
    private lateinit var binding: ActivityNotificationBinding

//    override val layoutId: Int
//        get() = R.layout.activity_notification

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNotificationBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.commonToolbar.tvTitle.visibility = View.VISIBLE
        binding.commonToolbar.tvTitle.text = getString(R.string.notification)
        title = ""
        binding.commonToolbar.ibBack.setOnClickListener {
            hideKeyboard(this)
            onBackPressed()
        }
        //setToolbarTitle(getString(R.string.notification))
        setupList()

        govtWorkViewModel = ViewModelProvider(this).get(UserAdviseViewModel::class.java)

        govtWorkViewModel.notificationResponse().observe(this, {
            handleResponse(it)
        })

        getNews()
    }

    private fun handleResponse(userAdviseResponse: NotificationResponse) {
        loading = false
        binding.rvPollAndSurvey.visibility = View.VISIBLE
        binding.pbQuestionSuggestion.visibility = View.GONE

        when {
            !userAdviseResponse.notification_list.isNullOrEmpty() -> {
                totalRecords = userAdviseResponse.total_records
                addItems(userAdviseResponse.notification_list)
            }
            pageNo == 0 -> {
                govtWorkNewsAdapter.reset()
                showSnackBar(getString(R.string.no_record_found), this)
            }
            else -> {
                showSnackBar(getString(R.string.something_went_wrong), this)
            }
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

                    govtWorkViewModel.getNotification(
                        SPreferenceManager.getInstance(this@NotificationActivity).session,
                        pageNo.toString(),
                        "10"
                    )
                }
            }
        })

        govtWorkNewsAdapter = NotificationAdapter()
        binding.rvPollAndSurvey.adapter = govtWorkNewsAdapter
    }

    private fun getNews() {
        if (isConnected(this)) {
            pageNo = 0
            loading = true
            binding.pbQuestionSuggestion.visibility = View.VISIBLE
            binding.rvPollAndSurvey.visibility = View.GONE
            govtWorkViewModel.getNotification(
                SPreferenceManager.getInstance(this@NotificationActivity).session,
                pageNo.toString(),
                "10"
            )
        } else {
            showSnackBar(getString(R.string.no_internet))
        }
    }

    private fun addItems(govWorkList: ArrayList<NotificationResponse.Notification>) {
        govtWorkNewsAdapter.setItem(govWorkList)
    }
}