package com.politics.politicalapp.ui.activity

import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import app.app.patidarsaurabh.apputils.AppConstants
import com.bumptech.glide.Glide
import com.politics.politicalapp.R
import com.politics.politicalapp.adapter.NewsCommentAdapter
import com.politics.politicalapp.apputils.SPreferenceManager
import com.politics.politicalapp.apputils.isConnected
import com.politics.politicalapp.apputils.showSnackBar
import com.politics.politicalapp.pojo.GiveUserRatingToGovtWorkResponse
import com.politics.politicalapp.pojo.GovtWorkDetailResponse
import com.politics.politicalapp.viewmodel.GovtWorkViewModel
import kotlinx.android.synthetic.main.activity_govt_work_detail.*

class GovtWorkDetailActivity : ExtendedToolbarActivity() {

    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var govtWorkNewsAdapter: NewsCommentAdapter
    private lateinit var govtWorkViewModel: GovtWorkViewModel
    private var gid = ""
    private var ratingDone = true
    private var rating = 0

    override val layoutId: Int
        get() = R.layout.activity_govt_work_detail

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        gid = intent.getStringExtra(AppConstants.GID)!!

        setToolbarTitle(getString(R.string.govt_work))
        setupList()

        govtWorkViewModel = ViewModelProvider(this).get(GovtWorkViewModel::class.java)

        govtWorkViewModel.govtWorkDetail().observe(this, {
            handleResponse(it)
        })

        govtWorkViewModel.userRating().observe(this, {
            handleResponseOfRating(it)
        })

        if (isConnected(this)) {
            pbNewsDetail.visibility = View.VISIBLE
            nsvGovtWorkDetail.visibility = View.GONE
            govtWorkViewModel.getGovtWorkDetail(gid, SPreferenceManager.getInstance(this).session)
        } else {
            showSnackBar(getString(R.string.no_internet))
        }

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
        yellowText.setSpan(
            ForegroundColorSpan(ContextCompat.getColor(this, R.color.red_CC252C)),
            0, yellowText.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

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

    private fun handleResponseOfRating(giveUserRatingToGovtWorkResponse: GiveUserRatingToGovtWorkResponse?) {
        pbRateGovtWork.visibility = View.GONE
        btSubmitRatingOfGovtWorkDetail.visibility = View.VISIBLE

        if (null != giveUserRatingToGovtWorkResponse) {
            showAlertDialog(giveUserRatingToGovtWorkResponse.message)
            ratingDone = true
            //rating of this article is done now
            llRatingSubmit.visibility = View.GONE
            tvYourRating.text = getString(R.string.your_ratings)

        }
    }

    private fun handleResponse(govtWorkDetailResponse: GovtWorkDetailResponse?) {
        pbNewsDetail.visibility = View.GONE
        nsvGovtWorkDetail.visibility = View.VISIBLE
        if (null != govtWorkDetailResponse) {
            setupViews(govtWorkDetailResponse)
        } else {
            showSnackBar(getString(R.string.something_went_wrong))
        }
    }

    private fun setupViews(govtWorkDetailResponse: GovtWorkDetailResponse) {
        Glide.with(this)
            .load(govtWorkDetailResponse.gov_work_detail[0].up_pro_img)
            .into(ivNewsDetail)

        tvRateReceived.text = govtWorkDetailResponse.gov_work_detail[0].average_rating
        tvNewsDetailTitle.text = govtWorkDetailResponse.gov_work_detail[0].name

        tvNewsDetailDesc.text = HtmlCompat.fromHtml(
            govtWorkDetailResponse.gov_work_detail[0].description,
            HtmlCompat.FROM_HTML_MODE_LEGACY
        )

        setupRatings()
        if (govtWorkDetailResponse.gov_work_detail[0].user_rating.isEmpty()) {
            //rating is pending
            ratingDone = false
            llRatingSubmit.visibility = View.VISIBLE
        } else {
            ratingDone = true
            //rating of this article is done
            llRatingSubmit.visibility = View.GONE
            tvYourRating.text = getString(R.string.your_ratings)
            setupRates(govtWorkDetailResponse.gov_work_detail[0].user_rating)
        }
    }

    private fun setupRatings() {
        btSubmitRatingOfGovtWorkDetail.setOnClickListener {
            if (isConnected(this)) {
                if (rating > 0) {
                    btSubmitRatingOfGovtWorkDetail.visibility = View.INVISIBLE
                    pbRateGovtWork.visibility = View.VISIBLE
                    govtWorkViewModel.giveUserRating(
                        gid,
                        SPreferenceManager.getInstance(this).session,
                        rating.toString()
                    )
                } else {
                    showSnackBar(getString(R.string.invalid_rating))
                }
            }
        }

        tvRate1.setOnClickListener {
            if (!ratingDone) {
                oneStar()
            }
        }

        tvRate2.setOnClickListener {
            if (!ratingDone) {
                twoStar()
            }
        }

        tvRate3.setOnClickListener {
            if (!ratingDone) {
                threeStar()
            }
        }

        tvRate4.setOnClickListener {
            if (!ratingDone) {
                fourStar()
            }
        }

        tvRate5.setOnClickListener {
            if (!ratingDone) {
                fiveStar()
            }
        }

        tvRate6.setOnClickListener {
            if (!ratingDone) {
                sixStar()
            }
        }

        tvRate7.setOnClickListener {
            if (!ratingDone) {
                sevenStar()
            }
        }

        tvRate8.setOnClickListener {
            if (!ratingDone) {
                eightStar()
            }
        }

        tvRate9.setOnClickListener {
            if (!ratingDone) {
                nineStar()
            }
        }

        tvRate10.setOnClickListener {
            if (!ratingDone) {
                tenStar()
            }
        }
    }

    private fun tenStar() {
        rating = 10
        setFilled(tvRate1)
        setFilled(tvRate2)
        setFilled(tvRate3)
        setFilled(tvRate4)
        setFilled(tvRate5)
        setFilled(tvRate6)
        setFilled(tvRate7)
        setFilled(tvRate8)
        setFilled(tvRate9)
        setFilled(tvRate10)
    }

    private fun nineStar() {
        rating = 9
        setFilled(tvRate1)
        setFilled(tvRate2)
        setFilled(tvRate3)
        setFilled(tvRate4)
        setFilled(tvRate5)
        setFilled(tvRate6)
        setFilled(tvRate7)
        setFilled(tvRate8)
        setFilled(tvRate9)
        setEmpty(tvRate10)
    }

    private fun eightStar() {
        rating = 8
        setFilled(tvRate1)
        setFilled(tvRate2)
        setFilled(tvRate3)
        setFilled(tvRate4)
        setFilled(tvRate5)
        setFilled(tvRate6)
        setFilled(tvRate7)
        setFilled(tvRate8)
        setEmpty(tvRate9)
        setEmpty(tvRate10)
    }

    private fun sevenStar() {
        rating = 7
        setFilled(tvRate1)
        setFilled(tvRate2)
        setFilled(tvRate3)
        setFilled(tvRate4)
        setFilled(tvRate5)
        setFilled(tvRate6)
        setFilled(tvRate7)
        setEmpty(tvRate8)
        setEmpty(tvRate9)
        setEmpty(tvRate10)
    }

    private fun sixStar() {
        rating = 6
        setFilled(tvRate1)
        setFilled(tvRate2)
        setFilled(tvRate3)
        setFilled(tvRate4)
        setFilled(tvRate5)
        setFilled(tvRate6)
        setEmpty(tvRate7)
        setEmpty(tvRate8)
        setEmpty(tvRate9)
        setEmpty(tvRate10)
    }

    private fun fiveStar() {
        rating = 5
        setFilled(tvRate1)
        setFilled(tvRate2)
        setFilled(tvRate3)
        setFilled(tvRate4)
        setFilled(tvRate5)
        setEmpty(tvRate6)
        setEmpty(tvRate7)
        setEmpty(tvRate8)
        setEmpty(tvRate9)
        setEmpty(tvRate10)
    }

    private fun fourStar() {
        rating = 4
        setFilled(tvRate1)
        setFilled(tvRate2)
        setFilled(tvRate3)
        setFilled(tvRate4)
        setEmpty(tvRate5)
        setEmpty(tvRate6)
        setEmpty(tvRate7)
        setEmpty(tvRate8)
        setEmpty(tvRate9)
        setEmpty(tvRate10)
    }

    private fun threeStar() {
        rating = 3
        setFilled(tvRate1)
        setFilled(tvRate2)
        setFilled(tvRate3)
        setEmpty(tvRate4)
        setEmpty(tvRate5)
        setEmpty(tvRate6)
        setEmpty(tvRate7)
        setEmpty(tvRate8)
        setEmpty(tvRate9)
        setEmpty(tvRate10)
    }

    private fun twoStar() {
        rating = 2
        setFilled(tvRate1)
        setFilled(tvRate2)
        setEmpty(tvRate3)
        setEmpty(tvRate4)
        setEmpty(tvRate5)
        setEmpty(tvRate6)
        setEmpty(tvRate7)
        setEmpty(tvRate8)
        setEmpty(tvRate9)
        setEmpty(tvRate10)
    }

    private fun setupRates(userRating: String) {
        when (userRating.toInt()) {
            1 -> {
                oneStar()
            }
            2 -> {
                twoStar()
            }
            3 -> {
                threeStar()
            }
            4 -> {
                fourStar()
            }
            5 -> {
                fiveStar()
            }
            6 -> {
                sixStar()
            }
            7 -> {
                sevenStar()
            }
            8 -> {
                eightStar()
            }
            9 -> {
                nineStar()
            }
            10 -> {
                tenStar()
            }
        }
    }

    private fun oneStar() {
        rating = 1
        setFilled(tvRate1)
        setEmpty(tvRate2)
        setEmpty(tvRate3)
        setEmpty(tvRate4)
        setEmpty(tvRate5)
        setEmpty(tvRate6)
        setEmpty(tvRate7)
        setEmpty(tvRate8)
        setEmpty(tvRate9)
        setEmpty(tvRate10)
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

    private fun setFilled(textView: TextView) {
        textView.background = ContextCompat.getDrawable(this, R.drawable.circle_rating_done)
        textView.setTextColor(ContextCompat.getColor(this, R.color.white))
    }

    private fun setEmpty(textView: TextView) {
        textView.background = ContextCompat.getDrawable(this, R.drawable.circle_rating)
        textView.setTextColor(ContextCompat.getColor(this, R.color.gray))
    }

    private fun showAlertDialog(msg: String) {
        val alertDialogBuilder: AlertDialog.Builder = AlertDialog.Builder(this)
        alertDialogBuilder.setMessage(msg)
        alertDialogBuilder.setCancelable(true)

        alertDialogBuilder.setPositiveButton(
            getString(android.R.string.ok)
        ) { dialog, _ ->
            dialog.cancel()
        }

        val alertDialog: AlertDialog = alertDialogBuilder.create()
        alertDialog.show()
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE)
            .setTextColor(ContextCompat.getColor(this, R.color.red_CC252C))
    }

}