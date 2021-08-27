package com.app.colorsofgujarat.ui.activity

import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.app.colorsofgujarat.R
import com.app.colorsofgujarat.apputils.SPreferenceManager
import com.app.colorsofgujarat.apputils.getUserSelectedDistrictIndex
import com.app.colorsofgujarat.apputils.isConnected
import com.app.colorsofgujarat.apputils.showSnackBar
import com.app.colorsofgujarat.databinding.ActivityUpdateProfileBinding
import com.app.colorsofgujarat.pojo.SettingsResponse
import com.app.colorsofgujarat.viewmodel.SettingsViewModel

class UpdateProfileActivity : ExtendedToolbarActivity() {

    private var districtId = ""
    private lateinit var settingsViewModel: SettingsViewModel
    private var districtList: ArrayList<SettingsResponse.District> = ArrayList()
    private lateinit var binding: ActivityUpdateProfileBinding

    override val layoutId: Int
        get() = R.layout.activity_update_profile

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateProfileBinding.inflate(layoutInflater)
        setToolbarTitle(getString(R.string.Update_Profile))

        binding.commonToolbar.abLayout.setBackgroundResource(android.R.color.transparent)

        setupDistrictSpinner()

        binding.btUpdateProfile.setOnClickListener {
            if (isConnected(this)) {
                if (areFieldsValid()) {
                    binding.pbRegister.visibility = View.VISIBLE
                    binding.btUpdateProfile.visibility = View.INVISIBLE
                    settingsViewModel.editProfile(
                        SPreferenceManager.getInstance(this).session,
                        binding.etName.text.toString(),
                        districtId,
                        binding.etCity.text.toString()
                    )
                }
            } else {
                showSnackBar(getString(R.string.no_internet), this)
            }
        }

        settingsViewModel = ViewModelProvider(this).get(SettingsViewModel::class.java)

//        settingsViewModel.settingsResponse().observe(this, {
//            handleResponse(it)
//        })

        settingsViewModel.updateProfileResponse().observe(this, {
            binding.pbRegister.visibility = View.GONE
            binding.btUpdateProfile.visibility = View.VISIBLE

            val settings = SPreferenceManager.getInstance(this).settings
            settings.user_city = it.user_city
            settings.user_district = it.user_district
            settings.user_name = it.user_name

            SPreferenceManager.getInstance(this).saveSettings(settings)

            showAlertDialog(it.message)
        })

        binding.etCity.setText(SPreferenceManager.getInstance(this).settings.user_city)
        binding.etName.setText(SPreferenceManager.getInstance(this).settings.user_name)
    }

    private fun areFieldsValid(): Boolean {
        return when {
            districtList.isEmpty() -> {
                showSnackBar(getString(R.string.something_went_wrong))
                false
            }
            TextUtils.isEmpty(binding.etCity.text.toString()) -> {
                showSnackBar(getString(R.string.invalid_city))
                false
            }
            TextUtils.isEmpty(binding.etName.text.toString()) -> {
                showSnackBar(getString(R.string.invalid_name))
                false
            }
            else -> true
        }
    }

//    private fun setupDistrictSpinner() {
//        val adapter: ArrayAdapter<SettingsResponse.District> = ArrayAdapter(
//            applicationContext,
//            R.layout.simple_spinner_dropdown_item,
//            districtList
//        )
//
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
//        sp_district.adapter = adapter
//
//        sp_district.onItemSelectedListener = object : OnItemSelectedListener {
//            override fun onItemSelected(
//                parent: AdapterView<*>,
//                view: View,
//                position: Int,
//                id: Long
//            ) {
//                val district: SettingsResponse.District =
//                    parent.selectedItem as SettingsResponse.District
//                districtId = district.id
//            }
//
//            override fun onNothingSelected(parent: AdapterView<*>?) {}
//        }
//    }

//    private fun handleResponse(settingsResponse: SettingsResponse?) {
//        pbRegister.visibility = View.GONE
//        btUpdateProfile.visibility = View.VISIBLE
//        if (null != settingsResponse) {
//            this.settingsResponse = settingsResponse
//            districtList.addAll(settingsResponse.district_list)
//            setupDistrictSpinner()
//        } else {
//            showSnackBar(getString(R.string.something_went_wrong), this)
//        }
//    }

//    private fun fetchSettings() {
//        if (isConnected(this)) {
//            pbRegister.visibility = View.VISIBLE
//            btUpdateProfile.visibility = View.INVISIBLE
//            settingsViewModel.getSettings("")
//        } else {
//            showSnackBar(getString(R.string.no_internet), this)
//        }
//    }

    private fun setupDistrictSpinner() {
        districtList.addAll(SPreferenceManager.getInstance(this).settings.district_list)

        val adapter: ArrayAdapter<SettingsResponse.District> = ArrayAdapter(
            applicationContext,
            R.layout.simple_spinner_dropdown_item,
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
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun showAlertDialog(msg: String) {
        val alertDialogBuilder: AlertDialog.Builder = AlertDialog.Builder(this)
        alertDialogBuilder.setMessage(msg)
        alertDialogBuilder.setCancelable(true)

        alertDialogBuilder.setPositiveButton(
            getString(android.R.string.ok)
        ) { dialog, _ ->
            dialog.dismiss()
            finish()
        }

        val alertDialog: AlertDialog = alertDialogBuilder.create()
        alertDialog.show()
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE)
            .setTextColor(ContextCompat.getColor(this, R.color.red_CC252C))
    }
}