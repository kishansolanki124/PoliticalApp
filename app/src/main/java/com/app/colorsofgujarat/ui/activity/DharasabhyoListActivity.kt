package com.app.colorsofgujarat.ui.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import app.app.patidarsaurabh.apputils.AppConstants
import com.app.colorsofgujarat.R
import com.app.colorsofgujarat.adapter.DharasabhyoAdapter
import com.app.colorsofgujarat.apputils.*
import com.app.colorsofgujarat.databinding.ActivityDharasabhyoBinding
import com.app.colorsofgujarat.pojo.MLAListResponse
import com.app.colorsofgujarat.pojo.SettingsResponse
import com.app.colorsofgujarat.viewmodel.MLAViewModel

class DharasabhyoListActivity : ExtendedToolbarActivity() {

    private lateinit var mlaViewModel: MLAViewModel
    private var districtId = ""
    private var totalRecords = 0
    private var loading = false
    private var pageNo = 0
    private var districtList: ArrayList<SettingsResponse.District> = ArrayList()
    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var govtWorkNewsAdapter: DharasabhyoAdapter
    private lateinit var binding: ActivityDharasabhyoBinding

    override val layoutId: Int
        get() = R.layout.activity_dharasabhyo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDharasabhyoBinding.inflate(layoutInflater)
        setToolbarTitle(getString(R.string.dharasabhyo))
        setupList()

        mlaViewModel = ViewModelProvider(this).get(MLAViewModel::class.java)

        mlaViewModel.govtWorkList().observe(this, {
            handleResponse(it)
        })

        setupDistrictSpinner()
    }

    private fun handleResponse(mlaListResponse: MLAListResponse?) {
        loading = false
        binding.pbMLAs.visibility = View.GONE
        binding.rvMLAs.visibility = View.VISIBLE
        if (null != mlaListResponse) {
            when {
                mlaListResponse.gov_mla_list.isNotEmpty() -> {
                    totalRecords = mlaListResponse.total_records
                    addItems(mlaListResponse.gov_mla_list)
                }
                pageNo == 0 -> {
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
            pageNo = 0
            loading = true
            binding.pbMLAs.visibility = View.VISIBLE
            binding.rvMLAs.visibility = View.GONE
            govtWorkNewsAdapter.reset()
            mlaViewModel.getGovtWorkList(districtId, pageNo.toString(), "10")
        } else {
            showSnackBar(getString(R.string.no_internet))
        }
    }

    private fun setupList() {
        layoutManager = GridLayoutManager(this, 2)
        binding.rvMLAs.layoutManager = layoutManager

        binding.rvMLAs.addOnScrollListener(object :
            EndlessRecyclerOnScrollListener(layoutManager, 3) {
            override fun onLoadMore() {
                if (!loading && totalRecords != govtWorkNewsAdapter.itemCount) {
                    loading = true

                    pageNo += 10

                    mlaViewModel.getGovtWorkList(districtId, pageNo.toString(), "10")
                }
            }
        })

        govtWorkNewsAdapter = DharasabhyoAdapter(
            {
                //callIntent(this, it.contact_no!!)
                resultLauncher.launch(
                    Intent(this, DharasabhyoDetailActivity::class.java)
                        .putExtra(AppConstants.MLA, it)
                )
            }, {
                //browserIntent(this, it.website!!)
            }
        )
        binding.rvMLAs.adapter = govtWorkNewsAdapter
    }

    private fun addItems(govMlaList: ArrayList<MLAListResponse.GovMla>) {
        govtWorkNewsAdapter.setItem(govMlaList)
    }

    private var resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                result.data?.let {
                    if (it.getBooleanExtra(AppConstants.REFRESH, false)) {
                        getNews()
                    }
                }
            }
        }

    private fun setupDistrictSpinner() {
        districtList.addAll(SPreferenceManager.getInstance(this).settings.district_list)

        val adapter: ArrayAdapter<SettingsResponse.District> = ArrayAdapter(
            applicationContext,
            R.layout.simple_spinner_dropdown_item_black,
            districtList
        )

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spDistrict.adapter = adapter

        binding.spDistrict.setSelection(getUserSelectedDistrictIndex())

        binding.spDistrict.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                val district: SettingsResponse.District =
                    parent.selectedItem as SettingsResponse.District
                districtId = district.id
                getNews()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }
}