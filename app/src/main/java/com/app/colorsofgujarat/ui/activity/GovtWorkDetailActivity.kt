package com.app.colorsofgujarat.ui.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Spannable
import android.text.SpannableString
import android.text.TextUtils
import android.text.style.ForegroundColorSpan
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import app.app.patidarsaurabh.apputils.AppConstants
import com.app.colorsofgujarat.R
import com.app.colorsofgujarat.adapter.NewsCommentAdapter
import com.app.colorsofgujarat.apputils.*
import com.app.colorsofgujarat.pojo.GiveUserRatingToGovtWorkResponse
import com.app.colorsofgujarat.pojo.GovtWorkDetailResponse
import com.app.colorsofgujarat.pojo.PopupBannerResponse
import com.app.colorsofgujarat.viewmodel.GovtWorkViewModel
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_govt_work_detail.*

class GovtWorkDetailActivity : ExtendedToolbarActivity() {

    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var govtWorkNewsAdapter: NewsCommentAdapter
    private lateinit var govtWorkViewModel: GovtWorkViewModel
    private var gid = ""
    private var sharableText = ""
    private var averageRating = ""
    private var imageURL = ""
    private var ratingDone = true
    private var refreshPage = false
    private var rating = 0
    private var handler: Handler? = null
    private var runnableCode: Runnable? = null

    override val layoutId: Int
        get() = R.layout.activity_govt_work_detail

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        gid = intent.getStringExtra(AppConstants.GID)!!

        setToolbarTitle(getString(R.string.govt_work))
        initList()

        govtWorkViewModel = ViewModelProvider(this).get(GovtWorkViewModel::class.java)

        govtWorkViewModel.govtWorkDetail().observe(this, {
            handleResponse(it)
        })

        govtWorkViewModel.userRating().observe(this, {
            handleResponseOfRating(it)
        })

        govtWorkViewModel.newComment().observe(this, {
            handleResponseOfNewComment(it)
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

        //val yellowText = SpannableString(getString(R.string.give_rate_get_10_point_2))

        val yellowText = SpannableString(getPollPoints())

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

        if (!SPreferenceManager.getInstance(this).banners.popup_banner.isNullOrEmpty()) {
            setupRepeatableBannerAd(
                SPreferenceManager.getInstance(this).banners.delay_time,
                SPreferenceManager.getInstance(this).banners.initial_time,
                SPreferenceManager.getInstance(this).banners.popup_banner
            )
        }
    }

    private fun handleResponseOfNewComment(giveUserRatingToGovtWorkResponse: GiveUserRatingToGovtWorkResponse?) {
        btSubmitComment.visibility = View.VISIBLE
        pbComment.visibility = View.GONE
        if (null != giveUserRatingToGovtWorkResponse) {
            etUserComment.setText("")
            govtWorkNewsAdapter.reset()
            addItems(giveUserRatingToGovtWorkResponse.comment_list)
            setUserPoints(giveUserRatingToGovtWorkResponse.user_points)
            //showAlertDialog(giveUserRatingToGovtWorkResponse.message)
        }
    }

    private fun handleResponseOfRating(giveUserRatingToGovtWorkResponse: GiveUserRatingToGovtWorkResponse?) {
        pbRateGovtWork.visibility = View.GONE
        btSubmitRatingOfGovtWorkDetail.visibility = View.VISIBLE

        if (null != giveUserRatingToGovtWorkResponse) {
            showAlertDialog(giveUserRatingToGovtWorkResponse.message)
            setUserPoints(giveUserRatingToGovtWorkResponse.user_points)
            ratingDone = true
            //rating of this article is done now
            llRatingSubmit.visibility = View.GONE
            tvYourRating.text = getString(R.string.your_ratings)
            refreshPage = true
            averageRating = giveUserRatingToGovtWorkResponse.average_rating
            tvRateReceived.text = giveUserRatingToGovtWorkResponse.average_rating
        }
    }

    private fun handleResponse(govtWorkDetailResponse: GovtWorkDetailResponse?) {
        pbNewsDetail.visibility = View.GONE
        nsvGovtWorkDetail.visibility = View.VISIBLE
        if (null != govtWorkDetailResponse) {
            setupViews(govtWorkDetailResponse)
            addItems(govtWorkDetailResponse.user_comment)
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

        sharableText = govtWorkDetailResponse.gov_work_detail[0].name
        imageURL = govtWorkDetailResponse.gov_work_detail[0].up_pro_img

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

        tvShareGovtWork.setOnClickListener {
            shareIntent(sharableText, imageURL, this)
        }

        tvViewAllComment.setOnClickListener {
            startActivity(
                Intent(this, GovtWorkAllCommentActivity::class.java)
                    .putExtra(AppConstants.GID, gid)
            )
        }

        btSubmitComment.setOnClickListener {
            if (!TextUtils.isEmpty(etUserComment.text.toString())) {
                if (isConnected(this)) {
                    hideKeyboard(this)
                    pbComment.visibility = View.VISIBLE
                    btSubmitComment.visibility = View.INVISIBLE
                    govtWorkViewModel.addGovtWorkComment(
                        gid,
                        SPreferenceManager.getInstance(this).session,
                        etUserComment.text.toString()
                    )
                } else {
                    showSnackBar(getString(R.string.no_internet))
                }
            } else {
                showSnackBar(getString(R.string.invalid_comment))
            }
        }

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
            tvViewAllComment.visibility = View.VISIBLE
            govtWorkNewsAdapter.setItem(userComment)
        } else {
            tvViewAllComment.visibility = View.GONE
        }
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

    override fun onBackPressed() {
        val intent = Intent()
        intent.putExtra(AppConstants.REFRESH, refreshPage)
        intent.putExtra(AppConstants.RATING, averageRating)
        setResult(Activity.RESULT_OK, intent)
        finish()
    }

    private fun setupRepeatableBannerAd(
        delayTime: String,
        initialTime: String,
        popupBanner: List<PopupBannerResponse.PopupBanner>
    ) {
        handler = Handler(Looper.getMainLooper())
        runnableCode = object : Runnable {
            override fun run() {
                if (!isDestroyed && !this@GovtWorkDetailActivity.isFinishing) {
                    if (this@GovtWorkDetailActivity.lifecycle.currentState
                            .isAtLeast(Lifecycle.State.RESUMED)
                    ) {
                        showProgressDialog(this@GovtWorkDetailActivity, popupBanner)
                    }
                    handler?.postDelayed(this, delayTime.toLong() * 1000)
                }
            }
        }

        if (!isDestroyed && !this.isFinishing) {
            runnableCode?.let {
                handler?.postDelayed(it, initialTime.toLong() * 1000)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (null != handler) {
            runnableCode?.let {
                handler!!.removeCallbacks(it)
            }
        }
    }
}