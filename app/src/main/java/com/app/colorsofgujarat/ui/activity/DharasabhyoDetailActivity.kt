package com.app.colorsofgujarat.ui.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager.widget.ViewPager
import app.app.patidarsaurabh.apputils.AppConstants
import com.app.colorsofgujarat.R
import com.app.colorsofgujarat.apputils.SPreferenceManager
import com.app.colorsofgujarat.apputils.isConnected
import com.app.colorsofgujarat.apputils.showProgressDialog
import com.app.colorsofgujarat.apputils.showSnackBar
import com.app.colorsofgujarat.databinding.ActivityDharasabhyoDetailBinding
import com.app.colorsofgujarat.pojo.MLADetailResponse
import com.app.colorsofgujarat.pojo.MLAListResponse
import com.app.colorsofgujarat.pojo.PopupBannerResponse
import com.app.colorsofgujarat.ui.fragment.DharasabhyoProfileFragment
import com.app.colorsofgujarat.ui.fragment.DharasabhyoSpecialWorkFragment
import com.app.colorsofgujarat.ui.fragment.DharasabhyoYourReviewFragment
import com.app.colorsofgujarat.viewmodel.MLAViewModel
import com.bumptech.glide.Glide


class DharasabhyoDetailActivity : ExtendedToolbarActivity() {

    private lateinit var viewPagerShraddhanjaliAdapter: ViewPagerShraddhanjaliAdapter
    private lateinit var viewPagerWith2PageAdapter: ViewPagerWith2PageAdapter
    private lateinit var mlaDetailResponse: MLADetailResponse
    private var govMla: MLAListResponse.GovMla? = null
    private lateinit var viewPager: ViewPager
    private lateinit var mlaViewModel: MLAViewModel
    private var handler: Handler? = null
    private var runnableCode: Runnable? = null
    private lateinit var binding: ActivityDharasabhyoDetailBinding


    override val layoutId: Int
        get() = R.layout.activity_dharasabhyo_detail

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDharasabhyoDetailBinding.inflate(layoutInflater)

        setToolbarTitle(getString(R.string.dharasabhyo))

        govMla = intent.getSerializableExtra(AppConstants.MLA) as MLAListResponse.GovMla

        setupViews()

        mlaViewModel = ViewModelProvider(this).get(MLAViewModel::class.java)

        mlaViewModel.mlaDetail().observe(this, {
            handleResponse(it)
        })

        if (isConnected(this)) {
            binding.pbMLADetail.visibility = View.VISIBLE
            binding.pager.visibility = View.GONE
            mlaViewModel.getMLADetail(govMla!!.id, SPreferenceManager.getInstance(this).session)
        } else {
            showSnackBar(getString(R.string.no_internet))
        }

        if (!SPreferenceManager.getInstance(this).banners.popup_banner.isNullOrEmpty()) {
            setupRepeatableBannerAd(
                SPreferenceManager.getInstance(this).banners.delay_time,
                SPreferenceManager.getInstance(this).banners.initial_time,
                SPreferenceManager.getInstance(this).banners.popup_banner
            )
        }
    }

    private fun handleResponse(mlaDetailResponse: MLADetailResponse?) {
        binding.pager.visibility = View.VISIBLE
        binding.pbMLADetail.visibility = View.GONE
        if (null != mlaDetailResponse) {
            this.mlaDetailResponse = mlaDetailResponse
            setupViewPager()
        } else {
            showSnackBar(getString(R.string.something_went_wrong))
        }
    }

    private fun setupViewPager() {
        if (this.mlaDetailResponse.gov_mla_detail[0].mla_work.isNotEmpty()) {
            viewPagerShraddhanjaliAdapter = ViewPagerShraddhanjaliAdapter(supportFragmentManager)
            viewPager = findViewById(R.id.pager)
            viewPager.adapter = viewPagerShraddhanjaliAdapter
        } else {
            viewPagerWith2PageAdapter = ViewPagerWith2PageAdapter(supportFragmentManager)
            viewPager = findViewById(R.id.pager)
            viewPager.adapter = viewPagerWith2PageAdapter
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setupViews() {
        if (null != govMla) {
            binding.layoutDharasabhyoLarge.tvMLAName.text = govMla!!.mla_name
            binding.layoutDharasabhyoLarge.tvPartyName.text = govMla!!.political_party
            binding.layoutDharasabhyoLarge.tvCityName.text = govMla!!.city
            binding.layoutDharasabhyoLarge.pbMLA.progress = govMla!!.percentage.toFloat().toInt()
            binding.layoutDharasabhyoLarge.tvPercentage.text =
                getString(R.string.percentage_, govMla!!.percentage)
            binding.layoutDharasabhyoLarge.tvVotesTotal.text = "Votes: " + govMla!!.votes

            Glide.with(binding.layoutDharasabhyoLarge.ivMLA.context)
                .load(govMla!!.up_pro_img)
                .into(binding.layoutDharasabhyoLarge.ivMLA)
//
        }
    }

    // Since this is an object collection, use a FragmentStatePagerAdapter,
    // and NOT a FragmentPagerAdapter.
    class ViewPagerShraddhanjaliAdapter(fm: FragmentManager) :
        FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

        override fun getCount(): Int = 3

        override fun getItem(i: Int): Fragment {

            return when (i) {
                0 -> {
                    DharasabhyoProfileFragment()
                }
                1 -> {
                    DharasabhyoSpecialWorkFragment()
                }
                else -> {
                    DharasabhyoYourReviewFragment()
                }
            }
        }

        override fun getPageTitle(position: Int): CharSequence {
            //return "OBJECT ${(position + 1)}"
            return if (position == 0) {
                "પ્રોફાઇલ"
            } else if (position == 1) {
                "વિશેષ કામગીરી"
            } else {
                "તમારો રીવ્યુ"
            }
        }
    }

    class ViewPagerWith2PageAdapter(fm: FragmentManager) :
        FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

        override fun getCount(): Int = 2

        override fun getItem(i: Int): Fragment {

            return when (i) {
                0 -> {
                    DharasabhyoProfileFragment()
                }
                1 -> {
                    DharasabhyoYourReviewFragment()
                }
                else -> {
                    DharasabhyoProfileFragment()
                }
            }
        }

        override fun getPageTitle(position: Int): CharSequence {
            //return "OBJECT ${(position + 1)}"
            return if (position == 0) {
                "પ્રોફાઇલ"
            } else if (position == 1) {
                "તમારો રીવ્યુ"
            } else {
                "પ્રોફાઇલ"
            }
        }
    }

    fun getMLADetailResponse(): MLADetailResponse {
        return mlaDetailResponse
    }

    override fun onBackPressed() {
        val intent = Intent()
        intent.putExtra(AppConstants.REFRESH, true)
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
                if (!isDestroyed && !this@DharasabhyoDetailActivity.isFinishing) {
                    if (this@DharasabhyoDetailActivity.lifecycle.currentState
                            .isAtLeast(Lifecycle.State.RESUMED)
                    ) {
                        showProgressDialog(this@DharasabhyoDetailActivity, popupBanner)
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
//todo : show toolbar when collapsed