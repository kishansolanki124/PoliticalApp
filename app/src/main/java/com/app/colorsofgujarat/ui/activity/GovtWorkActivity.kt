package com.app.colorsofgujarat.ui.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import app.app.patidarsaurabh.apputils.AppConstants
import com.app.colorsofgujarat.R
import com.app.colorsofgujarat.adapter.GovtWorkNewsAdapter
import com.app.colorsofgujarat.apputils.*
import com.app.colorsofgujarat.databinding.ActivityGovtWorkBinding
import com.app.colorsofgujarat.pojo.GovtWorkListResponse
import com.app.colorsofgujarat.pojo.SettingsResponse
import com.app.colorsofgujarat.viewmodel.GovtWorkViewModel

class GovtWorkActivity : AppCompatActivity() {

    private var districtId = ""
    private var totalRecords = 0
    private var selectedItemIndex = 0
    private var loading = false
    private var pageNo = 0
    private lateinit var govtWorkViewModel: GovtWorkViewModel
    private var districtList: ArrayList<SettingsResponse.District> = ArrayList()
    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var govtWorkNewsAdapter: GovtWorkNewsAdapter
    private lateinit var binding: ActivityGovtWorkBinding

//    override val layoutId: Int
//        get() = R.layout.activity_govt_work

    private var resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                result.data?.let {
                    if (it.getBooleanExtra(AppConstants.REFRESH, false)) {
                        govtWorkNewsAdapter.updateItem(
                            selectedItemIndex,
                            it.getStringExtra(AppConstants.RATING)!!
                        )
                    }
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGovtWorkBinding.inflate(layoutInflater)

        //setToolbarTitle(getString(R.string.govt_work))
        val view = binding.root
        setContentView(view)

        binding.commonToolbar.tvTitle.visibility = View.VISIBLE
        binding.commonToolbar.tvTitle.text = getString(R.string.govt_work)
        title = ""
        binding.commonToolbar.ibBack.setOnClickListener {
            hideKeyboard(this)
            onBackPressed()
        }
        setupList()

        govtWorkViewModel = ViewModelProvider(this).get(GovtWorkViewModel::class.java)

        govtWorkViewModel.govtWorkList().observe(this, {
            handleResponse(it)
        })

        setupDistrictSpinner()
    }

    private fun getNews() {
        if (isConnected(this)) {
            pageNo = 0
            loading = true
            binding.pbNewsPortal.visibility = View.VISIBLE
            binding.rvNewsPortal.visibility = View.GONE
            govtWorkViewModel.getGovtWorkList(districtId, pageNo.toString(), "10")
        } else {
            showSnackBar(getString(R.string.no_internet))
        }
    }

    private fun addItems(govWorkList: ArrayList<GovtWorkListResponse.GovWork>) {
        govtWorkNewsAdapter.setItem(govWorkList)
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
                govtWorkNewsAdapter.reset()
                getNews()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun handleResponse(govtWorkListResponse: GovtWorkListResponse) {
        loading = false
        binding.pbNewsPortal.visibility = View.GONE
        binding.rvNewsPortal.visibility = View.VISIBLE
        when {
            govtWorkListResponse.gov_work_list.isNotEmpty() -> {
                totalRecords = govtWorkListResponse.total_records
                addItems(govtWorkListResponse.gov_work_list)
            }

            pageNo == 0 -> {
                govtWorkNewsAdapter.reset()
                showSnackBar(getString(R.string.no_record_found), this)
            }
            else -> {
                showSnackBar(getString(R.string.something_went_wrong), this)
            }
        }
    }

    private fun setupList() {
        layoutManager = LinearLayoutManager(this)
        binding.rvNewsPortal.layoutManager = layoutManager

        binding.rvNewsPortal.addOnScrollListener(object :
            EndlessRecyclerOnScrollListener(layoutManager, 3) {
            override fun onLoadMore() {
                if (!loading && totalRecords != govtWorkNewsAdapter.itemCount) {
                    loading = true

                    pageNo += 10

                    govtWorkViewModel.getGovtWorkList(
                        districtId,
                        pageNo.toString(),
                        "10"
                    )
                }
            }
        })

        govtWorkNewsAdapter = GovtWorkNewsAdapter(
            { it, index ->
                selectedItemIndex = index
                resultLauncher.launch(
                    Intent(this, GovtWorkDetailActivity::class.java)
                        .putExtra(AppConstants.GID, it.id)
                )
            }, {
                shareIntent(it.name, it.up_pro_img, this)
            }
        )
        binding.rvNewsPortal.adapter = govtWorkNewsAdapter
    }
}