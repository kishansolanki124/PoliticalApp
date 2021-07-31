package com.app.colorsofgujarat.ui.activity

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.colorsofgujarat.R
import com.app.colorsofgujarat.adapter.NotificationAdapter
import com.app.colorsofgujarat.apputils.*
import com.app.colorsofgujarat.pojo.NotificationResponse
import com.app.colorsofgujarat.viewmodel.UserAdviseViewModel
import kotlinx.android.synthetic.main.activity_notification.*

class NotificationActivity : ExtendedToolbarActivity() {

    private lateinit var govtWorkNewsAdapter: NotificationAdapter
    private var totalRecords = 0
    private var loading = false
    private var pageNo = 0
    private lateinit var govtWorkViewModel: UserAdviseViewModel

    override val layoutId: Int
        get() = R.layout.activity_notification

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setToolbarTitle(getString(R.string.notification))
        setupList()

        govtWorkViewModel = ViewModelProvider(this).get(UserAdviseViewModel::class.java)

        govtWorkViewModel.notificationResponse().observe(this, {
            handleResponse(it)
        })

        getNews()
    }

    private fun handleResponse(userAdviseResponse: NotificationResponse) {
        loading = false
        rvPollAndSurvey.visibility = View.VISIBLE
        pbQuestionSuggestion.visibility = View.GONE

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
        rvPollAndSurvey.layoutManager = layoutManager

        rvPollAndSurvey.addOnScrollListener(object :
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
        rvPollAndSurvey.adapter = govtWorkNewsAdapter
    }

    private fun getNews() {
        if (isConnected(this)) {
            pageNo = 0
            loading = true
            pbQuestionSuggestion.visibility = View.VISIBLE
            rvPollAndSurvey.visibility = View.GONE
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