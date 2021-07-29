package com.politics.politicalapp.ui.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager.widget.ViewPager
import app.app.patidarsaurabh.apputils.AppConstants
import com.bumptech.glide.Glide
import com.politics.politicalapp.R
import com.politics.politicalapp.apputils.SPreferenceManager
import com.politics.politicalapp.apputils.isConnected
import com.politics.politicalapp.apputils.showSnackBar
import com.politics.politicalapp.pojo.MLADetailResponse
import com.politics.politicalapp.pojo.MLAListResponse
import com.politics.politicalapp.ui.fragment.DharasabhyoProfileFragment
import com.politics.politicalapp.ui.fragment.DharasabhyoSpecialWorkFragment
import com.politics.politicalapp.ui.fragment.DharasabhyoYourReviewFragment
import com.politics.politicalapp.viewmodel.MLAViewModel
import kotlinx.android.synthetic.main.activity_dharasabhyo_detail.*
import kotlinx.android.synthetic.main.dharasabhyo_item_large.*


class DharasabhyoDetailActivity : ExtendedToolbarActivity() {

    private lateinit var viewPagerShraddhanjaliAdapter: ViewPagerShraddhanjaliAdapter
    private lateinit var viewPagerWith2PageAdapter: ViewPagerWith2PageAdapter
    private lateinit var mlaDetailResponse: MLADetailResponse
    private var govMla: MLAListResponse.GovMla? = null
    private lateinit var viewPager: ViewPager
    private lateinit var mlaViewModel: MLAViewModel

    override val layoutId: Int
        get() = R.layout.activity_dharasabhyo_detail

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setToolbarTitle(getString(R.string.dharasabhyo))

        govMla = intent.getSerializableExtra(AppConstants.MLA) as MLAListResponse.GovMla

        setupViews()

        mlaViewModel = ViewModelProvider(this).get(MLAViewModel::class.java)

        mlaViewModel.mlaDetail().observe(this, {
            handleResponse(it)
        })

        if (isConnected(this)) {
            pbMLADetail.visibility = View.VISIBLE
            pager.visibility = View.GONE
            mlaViewModel.getMLADetail(govMla!!.id, SPreferenceManager.getInstance(this).session)
        } else {
            showSnackBar(getString(R.string.no_internet))
        }
    }

    private fun handleResponse(mlaDetailResponse: MLADetailResponse?) {
        pager.visibility = View.VISIBLE
        pbMLADetail.visibility = View.GONE
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
            tvMLAName.text = govMla!!.mla_name
            tvPartyName.text = govMla!!.political_party
            tvCityName.text = govMla!!.city
            pbMLA.progress = govMla!!.percentage.toFloat().toInt()
            tvPercentage.text = getString(R.string.percentage_, govMla!!.percentage)
            tvVotesTotal.text = "Votes: " + govMla!!.votes

            Glide.with(ivMLA.context)
                .load(govMla!!.up_pro_img)
                .into(ivMLA)
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
}
//todo : show toolbar when collapsed