package com.app.colorsofgujarat.ui.activity

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import app.app.patidarsaurabh.apputils.AppConstants
import com.app.colorsofgujarat.R
import com.app.colorsofgujarat.adapter.NewsCommentAdapter
import com.app.colorsofgujarat.apputils.EndlessRecyclerOnScrollListener
import com.app.colorsofgujarat.apputils.isConnected
import com.app.colorsofgujarat.apputils.showSnackBar
import com.app.colorsofgujarat.pojo.GovtWorkAllCommentResponse
import com.app.colorsofgujarat.pojo.GovtWorkDetailResponse
import com.app.colorsofgujarat.viewmodel.GovtWorkViewModel
import kotlinx.android.synthetic.main.activity_govt_work_all_comment.*

class GovtWorkAllCommentActivity : ExtendedToolbarActivity() {

    private var totalRecords = 0
    private var loading = false
    private var pageNo = 0
    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var govtWorkNewsAdapter: NewsCommentAdapter
    private lateinit var govtWorkViewModel: GovtWorkViewModel
    private var gid = ""

    override val layoutId: Int
        get() = R.layout.activity_govt_work_all_comment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        gid = intent.getStringExtra(AppConstants.GID)!!

        setToolbarTitle(getString(R.string.govt_work))
        initList()

        govtWorkViewModel = ViewModelProvider(this).get(GovtWorkViewModel::class.java)

        govtWorkViewModel.govtWorkAllComments().observe(this, {
            handleResponse(it)
        })

        if (isConnected(this)) {
            loading = true
            pbNewsDetail.visibility = View.VISIBLE
            rvComments.visibility = View.GONE
            govtWorkViewModel.getGovtWorkAllComments(gid, pageNo.toString(), "10")
        } else {
            showSnackBar(getString(R.string.no_internet))
        }
    }

    private fun handleResponse(govtWorkDetailResponse: GovtWorkAllCommentResponse) {
        loading = false
        pbNewsDetail.visibility = View.GONE
        rvComments.visibility = View.VISIBLE
        when {
            !govtWorkDetailResponse.user_comment.isNullOrEmpty() -> {
                totalRecords = govtWorkDetailResponse.total_records
                addItems(govtWorkDetailResponse.user_comment)
            }
            pageNo == 0 -> {
                govtWorkNewsAdapter.reset()
                showSnackBar(getString(R.string.no_record_found), this)
            }
            else -> {
                showSnackBar(getString(R.string.something_went_wrong))
            }
        }
    }

    private fun initList() {
        layoutManager = LinearLayoutManager(this)
        rvComments.layoutManager = layoutManager

        rvComments.addOnScrollListener(object :
            EndlessRecyclerOnScrollListener(layoutManager, 3) {
            override fun onLoadMore() {
                if (!loading && totalRecords != govtWorkNewsAdapter.itemCount) {
                    loading = true

                    pageNo += 10

                    govtWorkViewModel.getGovtWorkAllComments(gid, pageNo.toString(), "10")
                }
            }
        })

        govtWorkNewsAdapter = NewsCommentAdapter(
            {
                //callIntent(this, it.contact_no!!)
            }, {
                //browserIntent(this, it.website!!)
            }
        )
        rvComments.adapter = govtWorkNewsAdapter
    }

    private fun addItems(userComment: ArrayList<GovtWorkDetailResponse.UserComment>?) {
        if (null != userComment && userComment.isNotEmpty()) {
            govtWorkNewsAdapter.setItem(userComment)
        }
    }
}