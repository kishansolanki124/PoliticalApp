package com.app.colorsofgujarat.ui.activity

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.app.colorsofgujarat.R
import com.app.colorsofgujarat.apputils.SPreferenceManager
import com.app.colorsofgujarat.apputils.isConnected
import com.app.colorsofgujarat.apputils.showSnackBar
import com.app.colorsofgujarat.databinding.ActivityRegisterBinding
import com.app.colorsofgujarat.pojo.SettingsResponse
import com.app.colorsofgujarat.pojo.request.RegisterRequest
import com.app.colorsofgujarat.viewmodel.SettingsViewModel

class RegisterActivity : AppCompatActivity() {

    private var districtId = ""
    private lateinit var settingsViewModel: SettingsViewModel
    private lateinit var settingsResponse: SettingsResponse
    private var districtList: ArrayList<SettingsResponse.District> = ArrayList()
    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //setContentView(R.layout.activity_register)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btSubmitRegister.setOnClickListener {
            if (isConnected(this)) {
                if (areFieldsValid()) {
                    showAlertDialog()
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
//            pbRegister.visibility = View.GONE
//            btSubmitRegister.visibility = View.VISIBLE
            SPreferenceManager.getInstance(this)
                .saveSession(
                    this.settingsResponse,
                    binding.etMobile.text.toString(),
                    binding.etName.text.toString()
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
        } else if (districtId == "-1") {
            showSnackBar(getString(R.string.invalid_district))
            return false
        } else if (TextUtils.isEmpty(binding.etCity.text.toString())) {
            showSnackBar(getString(R.string.invalid_city))
            return false
        } else if (TextUtils.isEmpty(binding.etName.text.toString())) {
            showSnackBar(getString(R.string.invalid_name))
            return false
        } else if (TextUtils.isEmpty(binding.etMobile.text.toString()) || binding.etMobile.text.toString().length < 10) {
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
        binding.spDistrict.adapter = adapter

        binding.spDistrict.onItemSelectedListener = object : OnItemSelectedListener {
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
        binding.pbRegister.visibility = View.GONE
        binding.btSubmitRegister.visibility = View.VISIBLE
        if (null != settingsResponse) {
            this.settingsResponse = settingsResponse
            districtList.add(SettingsResponse.District("-1", "તમારો જિલ્લો પસંદ કરો"))
            districtList.addAll(settingsResponse.district_list)
            setupDistrictSpinner()
        } else {
            showSnackBar(getString(R.string.something_went_wrong), this)
        }
    }

    private fun fetchSettings() {
        if (isConnected(this)) {
            binding.pbRegister.visibility = View.VISIBLE
            binding.btSubmitRegister.visibility = View.INVISIBLE
            settingsViewModel.getSettings("")
        } else {
            showSnackBar(getString(R.string.no_internet), this)
        }
    }

    private fun showAlertDialog() {
        val alertDialogBuilder: AlertDialog.Builder = AlertDialog.Builder(this)
        alertDialogBuilder.setMessage(
            "Hey ! Friends for the participate in Quiz & Contest, Please Confirm Your Mobile No. ("
                    + binding.etMobile.text.toString() + ") once Again.\n" +
                    "You Can't change it later."
        )
        alertDialogBuilder.setCancelable(false)

        alertDialogBuilder.setPositiveButton(
            getString(R.string.submit)
        ) { dialog, _ ->
            dialog.dismiss()
            binding.pbRegister.visibility = View.VISIBLE
            binding.btSubmitRegister.visibility = View.INVISIBLE
            settingsViewModel.registration(
                RegisterRequest(
                    districtId,
                    binding.etCity.text.toString(),
                    binding.etName.text.toString(),
                    binding.etMobile.text.toString()
                )
            )
        }

        alertDialogBuilder.setNegativeButton(
            getString(R.string.cancel)
        ) { dialog, _ ->
            dialog.dismiss()
        }

        val alertDialog: AlertDialog = alertDialogBuilder.create()
        alertDialog.show()
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE)
            .setTextColor(ContextCompat.getColor(this, R.color.red_CC252C))

        alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE)
            .setTextColor(ContextCompat.getColor(this, R.color.red_CC252C))
    }
}