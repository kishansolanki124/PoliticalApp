package com.politics.politicalapp.ui.activity

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import app.app.patidarsaurabh.apputils.AppConstants
import com.politics.politicalapp.R
import com.politics.politicalapp.adapter.NewsCommentAdapter
import com.politics.politicalapp.apputils.SPreferenceManager
import com.politics.politicalapp.apputils.isConnected
import com.politics.politicalapp.apputils.showSnackBar
import com.politics.politicalapp.pojo.GovtWorkDetailResponse
import com.politics.politicalapp.viewmodel.GovtWorkViewModel
import kotlinx.android.synthetic.main.activity_govt_work_all_comment.*

class GovtWorkAllCommentActivity : ExtendedToolbarActivity() {

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

        govtWorkViewModel.govtWorkDetail().observe(this, {
            handleResponse(it)
        })

        if (isConnected(this)) {
            pbNewsDetail.visibility = View.VISIBLE
            rvComments.visibility = View.GONE
            govtWorkViewModel.getGovtWorkDetail(gid, SPreferenceManager.getInstance(this).session)
        } else {
            showSnackBar(getString(R.string.no_internet))
        }


    }

    private fun handleResponse(govtWorkDetailResponse: GovtWorkDetailResponse?) {
        pbNewsDetail.visibility = View.GONE
        rvComments.visibility = View.VISIBLE
        if (null != govtWorkDetailResponse) {
//            setupViews(govtWorkDetailResponse)
//            addItems(govtWorkDetailResponse.user_comment)
        } else {
            showSnackBar(getString(R.string.something_went_wrong))
        }
    }

    private fun initList() {
        layoutManager = LinearLayoutManager(this)
        rvComments.layoutManager = layoutManager

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