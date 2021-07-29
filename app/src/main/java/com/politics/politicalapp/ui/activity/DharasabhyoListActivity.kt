package com.politics.politicalapp.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import app.app.patidarsaurabh.apputils.AppConstants
import com.politics.politicalapp.R
import com.politics.politicalapp.adapter.DharasabhyoAdapter
import com.politics.politicalapp.apputils.SPreferenceManager
import com.politics.politicalapp.apputils.getUserSelectedDistrictIndex
import com.politics.politicalapp.apputils.isConnected
import com.politics.politicalapp.apputils.showSnackBar
import com.politics.politicalapp.pojo.MLAListResponse
import com.politics.politicalapp.pojo.SettingsResponse
import com.politics.politicalapp.viewmodel.MLAViewModel
import kotlinx.android.synthetic.main.activity_dharasabhyo.*

class DharasabhyoListActivity : ExtendedToolbarActivity() {

    private lateinit var mlaViewModel: MLAViewModel
    private var districtId = ""
    private var startPage = 0
    private var districtList: ArrayList<SettingsResponse.District> = ArrayList()
    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var govtWorkNewsAdapter: DharasabhyoAdapter

    override val layoutId: Int
        get() = R.layout.activity_dharasabhyo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setToolbarTitle(getString(R.string.dharasabhyo))
        setupList()

        mlaViewModel = ViewModelProvider(this).get(MLAViewModel::class.java)

        mlaViewModel.govtWorkList().observe(this, {
            handleResponse(it)
        })

        setupDistrictSpinner()
    }

    private fun handleResponse(mlaListResponse: MLAListResponse?) {
        pbMLAs.visibility = View.GONE
        rvMLAs.visibility = View.VISIBLE
        if (null != mlaListResponse) {
            when {
                mlaListResponse.gov_mla_list.isNotEmpty() -> {
                    addItems(mlaListResponse.gov_mla_list)
                }
                startPage == 0 -> {
                    govtWorkNewsAdapter.reset()
                    showSnackBar(getString(R.string.no_record_found), this)
                }
                else -> {
                    showSnackBar(getString(R.string.something_went_wrong), this)
                }
            }

        } else {
            showSnackBar(getString(R.string.something_went_wrong))
        }
    }

    private fun getNews() {
        if (isConnected(this)) {
            pbMLAs.visibility = View.VISIBLE
            rvMLAs.visibility = View.GONE
            mlaViewModel.getGovtWorkList(districtId, "0", "10")
        } else {
            showSnackBar(getString(R.string.no_internet))
        }
    }

    private fun setupList() {
        layoutManager = GridLayoutManager(this, 2)
        rvMLAs.layoutManager = layoutManager

        govtWorkNewsAdapter = DharasabhyoAdapter(
            {
                //callIntent(this, it.contact_no!!)
                startActivity(
                    Intent(this, DharasabhyoDetailActivity::class.java)
                        .putExtra(AppConstants.MLA, it)
                )
            }, {
                //browserIntent(this, it.website!!)
            }
        )
        rvMLAs.adapter = govtWorkNewsAdapter
    }

    private fun addItems(govMlaList: ArrayList<MLAListResponse.GovMla>) {
        govtWorkNewsAdapter.setItem(govMlaList)
    }

    private fun setupDistrictSpinner() {
        districtList.addAll(SPreferenceManager.getInstance(this).settings.district_list)

        val adapter: ArrayAdapter<SettingsResponse.District> = ArrayAdapter(
            applicationContext,
            R.layout.simple_spinner_dropdown_item_black,
            districtList
        )

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spDistrict.adapter = adapter

        spDistrict.setSelection(getUserSelectedDistrictIndex())

        spDistrict.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                val district: SettingsResponse.District =
                    parent.selectedItem as SettingsResponse.District
                districtId = district.id
                govtWorkNewsAdapter.reset()
                getNews()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }
}