package com.app.colorsofgujarat.ui.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Spannable
import android.text.SpannableString
import android.text.TextUtils
import android.text.style.ForegroundColorSpan
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import app.app.patidarsaurabh.apputils.AppConstants
import com.app.colorsofgujarat.R
import com.app.colorsofgujarat.adapter.NewsCommentAdapter
import com.app.colorsofgujarat.adapter.NewsDetailsImageAdapter
import com.app.colorsofgujarat.apputils.*
import com.app.colorsofgujarat.databinding.ActivityNewsDetailBinding
import com.app.colorsofgujarat.pojo.GiveUserRatingToGovtWorkResponse
import com.app.colorsofgujarat.pojo.GovtWorkDetailResponse
import com.app.colorsofgujarat.pojo.NewsDetailResponse
import com.app.colorsofgujarat.pojo.PopupBannerResponse
import com.app.colorsofgujarat.viewmodel.GovtWorkViewModel
import java.io.Serializable

class NewsDetailActivity : ExtendedToolbarActivity() {

    private lateinit var govtWorkViewModel: GovtWorkViewModel
    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var govtWorkNewsAdapter: NewsCommentAdapter
    private var nid = ""
    private var handler: Handler? = null
    private var runnableCode: Runnable? = null
    private lateinit var binding: ActivityNewsDetailBinding

    override val layoutId: Int
        get() = R.layout.activity_news_detail

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewsDetailBinding.inflate(layoutInflater)
        nid = intent.getStringExtra(AppConstants.ID)!!

        setToolbarTitle(getString(R.string.latest_news))
        initList()

        govtWorkViewModel = ViewModelProvider(this).get(GovtWorkViewModel::class.java)

        govtWorkViewModel.newsDetail().observe(this, {
            handleResponse(it)
        })

        govtWorkViewModel.newComment().observe(this, {
            handleResponseOfNewComment(it)
        })

        if (isConnected(this)) {
            binding.pbNewsDetail.visibility = View.VISIBLE
            binding.nsvGovtWorkDetail.visibility = View.GONE
            govtWorkViewModel.getNewsDetail(nid)
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

        //val yellowText = SpannableString(getString(R.string.give_rate_get_10_point_2))
        val yellowText = SpannableString(getPollPoints())

        yellowText.setSpan(
            ForegroundColorSpan(ContextCompat.getColor(this, R.color.red_CC252C)),
            0, yellowText.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        binding.tvGiveOpinionGet10Point.append(yellowText)

        val thirdText = SpannableString(getString(R.string.give_rate_get_10_point_3))
        thirdText.setSpan(
            ForegroundColorSpan(ContextCompat.getColor(this, R.color.black)),
            0, thirdText.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        binding.tvGiveOpinionGet10Point.append(thirdText)

        if (!SPreferenceManager.getInstance(this).banners.popup_banner.isNullOrEmpty()) {
            setupRepeatableBannerAd(
                SPreferenceManager.getInstance(this).banners.delay_time,
                SPreferenceManager.getInstance(this).banners.initial_time,
                SPreferenceManager.getInstance(this).banners.popup_banner
            )
        }
    }

    private fun handleResponse(govtWorkDetailResponse: NewsDetailResponse?) {
        binding.pbNewsDetail.visibility = View.GONE
        binding.nsvGovtWorkDetail.visibility = View.VISIBLE
        if (null != govtWorkDetailResponse) {
            setupViews(govtWorkDetailResponse)
            addItems(govtWorkDetailResponse.user_comment)
        } else {
            showSnackBar(getString(R.string.something_went_wrong))
        }
    }

    private fun setupViews(govtWorkDetailResponse: NewsDetailResponse) {

        binding.tvViewAllComment.setOnClickListener {
            startActivity(
                Intent(this, NewsAllCommentActivity::class.java)
                    .putExtra(AppConstants.ID, nid)
            )
        }

        binding.btSubmitComment.setOnClickListener {
            if (!TextUtils.isEmpty(binding.etUserComment.text.toString())) {
                if (isConnected(this)) {
                    hideKeyboard(this)
                    binding.pbComment.visibility = View.VISIBLE
                    binding.btSubmitComment.visibility = View.INVISIBLE
                    govtWorkViewModel.addNewsComment(
                        nid,
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

        binding.ivShareNews.setOnClickListener {
            shareIntent(
                govtWorkDetailResponse.news_detail[0].name,
                govtWorkDetailResponse.news_detail[0].up_pro_img,
                this
            )
        }

        val imageList: ArrayList<String> = ArrayList()
        if (!govtWorkDetailResponse.news_gallery.isNullOrEmpty()) {
            imageList.add(govtWorkDetailResponse.news_detail[0].up_pro_img)
            for (item in govtWorkDetailResponse.news_gallery) {
                imageList.add(item.up_pro_img)
            }
        } else {
            imageList.add(govtWorkDetailResponse.news_detail[0].up_pro_img)
        }

//        Glide.with(this)
//            .load(govtWorkDetailResponse.news_detail[0].up_pro_img)
//            .into(ivNewsDetail)

        binding.tvNewsDetailTitle.text = govtWorkDetailResponse.news_detail[0].name

        binding.tvNewsDetailDesc.text = HtmlCompat.fromHtml(
            govtWorkDetailResponse.news_detail[0].description,
            HtmlCompat.FROM_HTML_MODE_LEGACY
        )

        val adapter = NewsDetailsImageAdapter { _, position ->
            startActivity(
                Intent(this, DisplayPictureActivity::class.java)
                    .putExtra(AppConstants.IMAGE_POSITION, position)
                    .putExtra(
                        AppConstants.IMAGE_LIST,
                        imageList as Serializable
                    )
            )

        }
        adapter.setItem(imageList)
        binding.newsDetailsViewpager.adapter = adapter
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

    private fun addItems(userComment: ArrayList<GovtWorkDetailResponse.UserComment>) {
        if (userComment.isNotEmpty()) {
            binding.tvViewAllComment.visibility = View.VISIBLE
            govtWorkNewsAdapter.setItem(userComment)
        } else {
            binding.tvViewAllComment.visibility = View.GONE
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

    private fun setupRepeatableBannerAd(
        delayTime: String,
        initialTime: String,
        popupBanner: List<PopupBannerResponse.PopupBanner>
    ) {
        handler = Handler(Looper.getMainLooper())
        runnableCode = object : Runnable {
            override fun run() {
                if (!isDestroyed && !this@NewsDetailActivity.isFinishing) {
                    if (this@NewsDetailActivity.lifecycle.currentState
                            .isAtLeast(Lifecycle.State.RESUMED)
                    ) {
                        showProgressDialog(this@NewsDetailActivity, popupBanner)
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