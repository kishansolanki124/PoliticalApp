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
import com.app.colorsofgujarat.databinding.ActivityGovtWorkDetailBinding
import com.app.colorsofgujarat.pojo.GiveUserRatingToGovtWorkResponse
import com.app.colorsofgujarat.pojo.GovtWorkDetailResponse
import com.app.colorsofgujarat.pojo.PopupBannerResponse
import com.app.colorsofgujarat.viewmodel.GovtWorkViewModel
import com.bumptech.glide.Glide

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
    private lateinit var binding: ActivityGovtWorkDetailBinding

    override val layoutId: Int
        get() = R.layout.activity_govt_work_detail

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGovtWorkDetailBinding.inflate(layoutInflater)

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
            binding.pbNewsDetail.visibility = View.VISIBLE
            binding.nsvGovtWorkDetail.visibility = View.GONE
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
        binding.tvGiveOpinionGet10Point.text = greenText
        binding.tvGiveOpinionGet10Point.text = greenText2

        //val yellowText = SpannableString(getString(R.string.give_rate_get_10_point_2))

        val yellowText = SpannableString(getPollPoints())

        yellowText.setSpan(
            ForegroundColorSpan(ContextCompat.getColor(this, R.color.red_CC252C)),
            0, yellowText.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        binding.tvGiveOpinionGet10Point.append(yellowText)
        binding.tvGiveOpinionGet10Point.append(yellowText)

        val thirdText = SpannableString(getString(R.string.give_rate_get_10_point_3))
        thirdText.setSpan(
            ForegroundColorSpan(ContextCompat.getColor(this, R.color.black)),
            0, thirdText.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        binding.tvGiveOpinionGet10Point.append(thirdText)
        binding.tvGiveOpinionGet10Point.append(thirdText)

        if (!SPreferenceManager.getInstance(this).banners.popup_banner.isNullOrEmpty()) {
            setupRepeatableBannerAd(
                SPreferenceManager.getInstance(this).banners.delay_time,
                SPreferenceManager.getInstance(this).banners.initial_time,
                SPreferenceManager.getInstance(this).banners.popup_banner
            )
        }
    }

    private fun handleResponseOfNewComment(giveUserRatingToGovtWorkResponse: GiveUserRatingToGovtWorkResponse?) {
        binding.btSubmitComment.visibility = View.VISIBLE
        binding.pbComment.visibility = View.GONE
        if (null != giveUserRatingToGovtWorkResponse) {
            binding.etUserComment.setText("")
            govtWorkNewsAdapter.reset()
            addItems(giveUserRatingToGovtWorkResponse.comment_list)
            setUserPoints(giveUserRatingToGovtWorkResponse.user_points)
            //showAlertDialog(giveUserRatingToGovtWorkResponse.message)
        }
    }

    private fun handleResponseOfRating(giveUserRatingToGovtWorkResponse: GiveUserRatingToGovtWorkResponse?) {
        binding.pbRateGovtWork.visibility = View.GONE
        binding.btSubmitRatingOfGovtWorkDetail.visibility = View.VISIBLE

        if (null != giveUserRatingToGovtWorkResponse) {
            showAlertDialog(giveUserRatingToGovtWorkResponse.message)
            setUserPoints(giveUserRatingToGovtWorkResponse.user_points)
            ratingDone = true
            //rating of this article is done now
            binding.llRatingSubmit.visibility = View.GONE
            binding.tvYourRating.text = getString(R.string.your_ratings)
            refreshPage = true
            averageRating = giveUserRatingToGovtWorkResponse.average_rating
            binding.tvRateReceived.text = giveUserRatingToGovtWorkResponse.average_rating
        }
    }

    private fun handleResponse(govtWorkDetailResponse: GovtWorkDetailResponse?) {
        binding.pbNewsDetail.visibility = View.GONE
        binding.nsvGovtWorkDetail.visibility = View.VISIBLE
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
            .into(binding.ivNewsDetail)

        binding.tvRateReceived.text = govtWorkDetailResponse.gov_work_detail[0].average_rating
        binding.tvNewsDetailTitle.text = govtWorkDetailResponse.gov_work_detail[0].name

        binding.tvNewsDetailDesc.text = HtmlCompat.fromHtml(
            govtWorkDetailResponse.gov_work_detail[0].description,
            HtmlCompat.FROM_HTML_MODE_LEGACY
        )

        sharableText = govtWorkDetailResponse.gov_work_detail[0].name
        imageURL = govtWorkDetailResponse.gov_work_detail[0].up_pro_img

        setupRatings()
        if (govtWorkDetailResponse.gov_work_detail[0].user_rating.isEmpty()) {
            //rating is pending
            ratingDone = false
            binding.llRatingSubmit.visibility = View.VISIBLE
        } else {
            ratingDone = true
            //rating of this article is done
            binding.llRatingSubmit.visibility = View.GONE
            binding.tvYourRating.text = getString(R.string.your_ratings)
            setupRates(govtWorkDetailResponse.gov_work_detail[0].user_rating)
        }
    }

    private fun setupRatings() {

        binding.tvShareGovtWork.setOnClickListener {
            shareIntent(sharableText, imageURL, this)
        }

        binding.tvViewAllComment.setOnClickListener {
            startActivity(
                Intent(this, GovtWorkAllCommentActivity::class.java)
                    .putExtra(AppConstants.GID, gid)
            )
        }

        binding.btSubmitComment.setOnClickListener {
            if (!TextUtils.isEmpty(binding.etUserComment.text.toString())) {
                if (isConnected(this)) {
                    hideKeyboard(this)
                    binding.pbComment.visibility = View.VISIBLE
                    binding.btSubmitComment.visibility = View.INVISIBLE
                    govtWorkViewModel.addGovtWorkComment(
                        gid,
                        SPreferenceManager.getInstance(this).session,
                        binding.etUserComment.text.toString()
                    )
                } else {
                    showSnackBar(getString(R.string.no_internet))
                }
            } else {
                showSnackBar(getString(R.string.invalid_comment))
            }
        }

        binding.btSubmitRatingOfGovtWorkDetail.setOnClickListener {
            if (isConnected(this)) {
                if (rating > 0) {
                    binding.btSubmitRatingOfGovtWorkDetail.visibility = View.INVISIBLE
                    binding.pbRateGovtWork.visibility = View.VISIBLE
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

        binding.tvRate1.setOnClickListener {
            if (!ratingDone) {
                oneStar()
            }
        }

        binding.tvRate2.setOnClickListener {
            if (!ratingDone) {
                twoStar()
            }
        }

        binding.tvRate3.setOnClickListener {
            if (!ratingDone) {
                threeStar()
            }
        }

        binding.tvRate4.setOnClickListener {
            if (!ratingDone) {
                fourStar()
            }
        }

        binding.tvRate5.setOnClickListener {
            if (!ratingDone) {
                fiveStar()
            }
        }

        binding.tvRate6.setOnClickListener {
            if (!ratingDone) {
                sixStar()
            }
        }

        binding.tvRate7.setOnClickListener {
            if (!ratingDone) {
                sevenStar()
            }
        }

        binding.tvRate8.setOnClickListener {
            if (!ratingDone) {
                eightStar()
            }
        }

        binding.tvRate9.setOnClickListener {
            if (!ratingDone) {
                nineStar()
            }
        }

        binding.tvRate10.setOnClickListener {
            if (!ratingDone) {
                tenStar()
            }
        }
    }

    private fun tenStar() {
        rating = 10
        setFilled(binding.tvRate1)
        setFilled(binding.tvRate2)
        setFilled(binding.tvRate3)
        setFilled(binding.tvRate4)
        setFilled(binding.tvRate5)
        setFilled(binding.tvRate6)
        setFilled(binding.tvRate7)
        setFilled(binding.tvRate8)
        setFilled(binding.tvRate9)
        setFilled(binding.tvRate10)
    }

    private fun nineStar() {
        rating = 9
        setFilled(binding.tvRate1)
        setFilled(binding.tvRate2)
        setFilled(binding.tvRate3)
        setFilled(binding.tvRate4)
        setFilled(binding.tvRate5)
        setFilled(binding.tvRate6)
        setFilled(binding.tvRate7)
        setFilled(binding.tvRate8)
        setFilled(binding.tvRate9)
        setEmpty(binding.tvRate10)
    }

    private fun eightStar() {
        rating = 8
        setFilled(binding.tvRate1)
        setFilled(binding.tvRate2)
        setFilled(binding.tvRate3)
        setFilled(binding.tvRate4)
        setFilled(binding.tvRate5)
        setFilled(binding.tvRate6)
        setFilled(binding.tvRate7)
        setFilled(binding.tvRate8)
        setEmpty(binding.tvRate9)
        setEmpty(binding.tvRate10)
    }

    private fun sevenStar() {
        rating = 7
        setFilled(binding.tvRate1)
        setFilled(binding.tvRate2)
        setFilled(binding.tvRate3)
        setFilled(binding.tvRate4)
        setFilled(binding.tvRate5)
        setFilled(binding.tvRate6)
        setFilled(binding.tvRate7)
        setEmpty(binding.tvRate8)
        setEmpty(binding.tvRate9)
        setEmpty(binding.tvRate10)
    }

    private fun sixStar() {
        rating = 6
        setFilled(binding.tvRate1)
        setFilled(binding.tvRate2)
        setFilled(binding.tvRate3)
        setFilled(binding.tvRate4)
        setFilled(binding.tvRate5)
        setFilled(binding.tvRate6)
        setEmpty(binding.tvRate7)
        setEmpty(binding.tvRate8)
        setEmpty(binding.tvRate9)
        setEmpty(binding.tvRate10)
    }

    private fun fiveStar() {
        rating = 5
        setFilled(binding.tvRate1)
        setFilled(binding.tvRate2)
        setFilled(binding.tvRate3)
        setFilled(binding.tvRate4)
        setFilled(binding.tvRate5)
        setEmpty(binding.tvRate6)
        setEmpty(binding.tvRate7)
        setEmpty(binding.tvRate8)
        setEmpty(binding.tvRate9)
        setEmpty(binding.tvRate10)
    }

    private fun fourStar() {
        rating = 4
        setFilled(binding.tvRate1)
        setFilled(binding.tvRate2)
        setFilled(binding.tvRate3)
        setFilled(binding.tvRate4)
        setEmpty(binding.tvRate5)
        setEmpty(binding.tvRate6)
        setEmpty(binding.tvRate7)
        setEmpty(binding.tvRate8)
        setEmpty(binding.tvRate9)
        setEmpty(binding.tvRate10)
    }

    private fun threeStar() {
        rating = 3
        setFilled(binding.tvRate1)
        setFilled(binding.tvRate2)
        setFilled(binding.tvRate3)
        setEmpty(binding.tvRate4)
        setEmpty(binding.tvRate5)
        setEmpty(binding.tvRate6)
        setEmpty(binding.tvRate7)
        setEmpty(binding.tvRate8)
        setEmpty(binding.tvRate9)
        setEmpty(binding.tvRate10)
    }

    private fun twoStar() {
        rating = 2
        setFilled(binding.tvRate1)
        setFilled(binding.tvRate2)
        setEmpty(binding.tvRate3)
        setEmpty(binding.tvRate4)
        setEmpty(binding.tvRate5)
        setEmpty(binding.tvRate6)
        setEmpty(binding.tvRate7)
        setEmpty(binding.tvRate8)
        setEmpty(binding.tvRate9)
        setEmpty(binding.tvRate10)
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
        setFilled(binding.tvRate1)
        setEmpty(binding.tvRate2)
        setEmpty(binding.tvRate3)
        setEmpty(binding.tvRate4)
        setEmpty(binding.tvRate5)
        setEmpty(binding.tvRate6)
        setEmpty(binding.tvRate7)
        setEmpty(binding.tvRate8)
        setEmpty(binding.tvRate9)
        setEmpty(binding.tvRate10)
    }

    private fun initList() {
        layoutManager = LinearLayoutManager(this)
        binding.rvComments.layoutManager = layoutManager

        govtWorkNewsAdapter = NewsCommentAdapter(
            {
                //callIntent(this, it.contact_no!!)
            }, {
                //browserIntent(this, it.website!!)
            }
        )
        binding.rvComments.adapter = govtWorkNewsAdapter
    }

    private fun addItems(userComment: ArrayList<GovtWorkDetailResponse.UserComment>?) {
        if (null != userComment && userComment.isNotEmpty()) {
            binding.tvViewAllComment.visibility = View.VISIBLE
            govtWorkNewsAdapter.setItem(userComment)
        } else {
            binding.tvViewAllComment.visibility = View.GONE
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