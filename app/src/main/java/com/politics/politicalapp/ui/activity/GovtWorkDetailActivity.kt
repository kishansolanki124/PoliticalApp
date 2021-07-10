package com.politics.politicalapp.ui.activity

import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import androidx.core.content.ContextCompat
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

        val greenText = SpannableString(getString(R.string.give_rate_get_10_point_1))
        val greenText2 = SpannableString(getString(R.string.give_opinion_get_10_point_1))
        greenText.setSpan(
            ForegroundColorSpan(ContextCompat.getColor(this, R.color.black)),
            0, greenText.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        greenText2.setSpan(
            ForegroundColorSpan(ContextCompat.getColor(this, R.color.black)),
            0, greenText.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        tvGive_rate_get_10_point.text = greenText
        tvGive_opinion_get_10_point.text = greenText2

        val yellowText = SpannableString(getString(R.string.give_rate_get_10_point_2))
        yellowText.setSpan(ForegroundColorSpan(ContextCompat.getColor(this, R.color.red_CC252C)),
            0, yellowText.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

        tvGive_rate_get_10_point.append(yellowText)
        tvGive_opinion_get_10_point.append(yellowText)


        val thirdText = SpannableString(getString(R.string.give_rate_get_10_point_3))
        thirdText.setSpan(
            ForegroundColorSpan(ContextCompat.getColor(this, R.color.black)),
            0, thirdText.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        tvGive_rate_get_10_point.append(thirdText)
        tvGive_opinion_get_10_point.append(thirdText)
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