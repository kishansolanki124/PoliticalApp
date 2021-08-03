package com.app.colorsofgujarat.ui.activity

import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.app.colorsofgujarat.R
import com.app.colorsofgujarat.apputils.isConnected
import com.app.colorsofgujarat.apputils.showSnackBar
import com.app.colorsofgujarat.pojo.SettingsResponse
import com.app.colorsofgujarat.viewmodel.SettingsViewModel
import kotlinx.android.synthetic.main.activity_update_profile.*

class UpdateProfileActivity : AppCompatActivity() {

    private var districtId = ""
    private lateinit var settingsViewModel: SettingsViewModel
    private lateinit var settingsResponse: SettingsResponse
    private var districtList: ArrayList<SettingsResponse.District> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_update_profile)

        btUpdateProfile.setOnClickListener {
            if (isConnected(this)) {
                if (areFieldsValid()) {
                    pbRegister.visibility = View.VISIBLE
                    btUpdateProfile.visibility = View.INVISIBLE
//                    settingsViewModel.registration(
//                        RegisterRequest(
//                            districtId,
//                            et_city.text.toString(),
//                            et_name.text.toString(),
//                        )
//                    )
                }
            } else {
                showSnackBar(getString(R.string.no_internet), this)
            }
        }

        settingsViewModel = ViewModelProvider(this).get(SettingsViewModel::class.java)

        settingsViewModel.settingsResponse().observe(this, {
            handleResponse(it)
        })

        settingsViewModel.commonResponse().observe(this, {
//            startActivity(Intent(this, HomeActivity::class.java))
//            finish()
        })

        fetchSettings()
    }

    private fun areFieldsValid(): Boolean {
        if (districtList.isEmpty()) {
            showSnackBar(getString(R.string.something_went_wrong))
            return false
        } else if (TextUtils.isEmpty(et_city.text.toString())) {
            showSnackBar(getString(R.string.invalid_city))
            return false
        } else if (TextUtils.isEmpty(et_name.text.toString())) {
            showSnackBar(getString(R.string.invalid_name))
            return false
        }
        return true
    }

    private fun setupDistrictSpinner() {
        val adapter: ArrayAdapter<SettingsResponse.District> = ArrayAdapter(
            applicationContext,
            R.layout.simple_spinner_dropdown_item,
            districtList
        )

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        sp_district.adapter = adapter

        sp_district.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                val district: SettingsResponse.District =
                    parent.selectedItem as SettingsResponse.District
                districtId = district.id
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun handleResponse(settingsResponse: SettingsResponse?) {
        pbRegister.visibility = View.GONE
        btUpdateProfile.visibility = View.VISIBLE
        if (null != settingsResponse) {
            this.settingsResponse = settingsResponse
            districtList.addAll(settingsResponse.district_list)
            setupDistrictSpinner()
        } else {
            showSnackBar(getString(R.string.something_went_wrong), this)
        }
    }

    private fun fetchSettings() {
        if (isConnected(this)) {
            pbRegister.visibility = View.VISIBLE
            btUpdateProfile.visibility = View.INVISIBLE
            settingsViewModel.getSettings("")
        } else {
            showSnackBar(getString(R.string.no_internet), this)
        }
    }
}