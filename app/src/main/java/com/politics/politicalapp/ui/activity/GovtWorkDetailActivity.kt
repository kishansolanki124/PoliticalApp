package com.politics.politicalapp.ui.activity

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.politics.politicalapp.R
import com.politics.politicalapp.adapter.NewsCommentAdapter
import kotlinx.android.synthetic.main.activity_govt_work_detail.*

class GovtWorkDetailActivity : ExtendedToolbarActivity() {

    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var govtWorkNewsAdapter: NewsCommentAdapter

    override val layoutId: Int
        get() = R.layout.activity_govt_work_detail

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setToolbarTitle(getString(R.string.govt_work))
        setupList()
    }

    private fun setupList() {

        layoutManager = LinearLayoutManager(this)
        rvComments.layoutManager = layoutManager

        val stringList: ArrayList<String> = ArrayList()
        stringList.add("S")
        stringList.add("S")
        stringList.add("S")
        stringList.add("S")
        stringList.add("S")

        govtWorkNewsAdapter = NewsCommentAdapter(
            {
                //callIntent(this, it.contact_no!!)
            }, {
                //browserIntent(this, it.website!!)
            }
        )
        govtWorkNewsAdapter.setItem(stringList)
        rvComments.adapter = govtWorkNewsAdapter
    }
}