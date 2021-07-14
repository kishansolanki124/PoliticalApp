package com.politics.politicalapp.ui.activity

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.politics.politicalapp.R
import com.politics.politicalapp.apputils.SPreferenceManager
import com.politics.politicalapp.apputils.isConnected
import com.politics.politicalapp.apputils.showSnackBar
import com.politics.politicalapp.pojo.SettingsResponse
import com.politics.politicalapp.pojo.request.RegisterRequest
import com.politics.politicalapp.viewmodel.SettingsViewModel
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {

    private var districtId = ""
    private lateinit var settingsViewModel: SettingsViewModel
    private lateinit var settingsResponse: SettingsResponse
    private var districtList: ArrayList<SettingsResponse.District> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_register)

        btSubmitRegister.setOnClickListener {
            if (isConnected(this)) {
                if (areFieldsValid()) {
                    pbRegister.visibility = View.VISIBLE
                    btSubmitRegister.visibility = View.INVISIBLE
                    settingsViewModel.registration(
                        RegisterRequest(
                            districtId,
                            et_city.text.toString(),
                            et_name.text.toString(),
                            et_mobile.text.toString()
                        )
                    )
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
            pbRegister.visibility = View.GONE
            btSubmitRegister.visibility = View.VISIBLE
            SPreferenceManager.getInstance(this)
                .saveSession(
                    this.settingsResponse,
                    et_mobile.text.toString(),
                    et_name.text.toString()
                )
            startActivity(Intent(this, HomeActivity::class.java))
            finish()
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
        } else if (TextUtils.isEmpty(et_mobile.text.toString()) || et_mobile.text.toString().length < 10) {
            showSnackBar(getString(R.string.invalid_phone))
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
        btSubmitRegister.visibility = View.VISIBLE
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
            btSubmitRegister.visibility = View.INVISIBLE
            settingsViewModel.getSettings("")
        } else {
            showSnackBar(getString(R.string.no_internet), this)
        }
    }
}