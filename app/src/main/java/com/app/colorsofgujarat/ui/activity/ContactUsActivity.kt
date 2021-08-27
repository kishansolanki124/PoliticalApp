package com.app.colorsofgujarat.ui.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.app.colorsofgujarat.R
import com.app.colorsofgujarat.apputils.hideKeyboard
import com.app.colorsofgujarat.apputils.isConnected
import com.app.colorsofgujarat.apputils.showSnackBar
import com.app.colorsofgujarat.databinding.ActivityContactUsBinding
import com.app.colorsofgujarat.pojo.CommonResponse
import com.app.colorsofgujarat.pojo.ContactUsResponse
import com.app.colorsofgujarat.viewmodel.SettingsViewModel

class ContactUsActivity : ExtendedToolbarActivity() {

    private lateinit var settingsViewModel: SettingsViewModel
    private lateinit var binding: ActivityContactUsBinding

    override val layoutId: Int
        get() = R.layout.activity_contact_us

    @SuppressLint("QueryPermissionsNeeded")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding  = ActivityContactUsBinding.inflate(layoutInflater)
        setToolbarTitle(getString(R.string.Contact_Us))
        settingsViewModel = ViewModelProvider(this).get(SettingsViewModel::class.java)

        settingsViewModel.contactUs().observe(this, {
            handleResponse(it)
        })

        settingsViewModel.commonResponse().observe(this, {
            handleContactUsResponse(it)
        })

        if (isConnected(this)) {
            binding.cvContactUs.visibility = View.GONE
            binding.pbContactUs.visibility = View.VISIBLE
            settingsViewModel.getContactUs()
        } else {
            showSnackBar(getString(R.string.no_internet), this)
        }

        binding.tvPhone.setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel:${binding.tvPhone.text}")
            startActivity(intent)
        }

        binding.tvEmail.setOnClickListener {
            val intent = Intent(Intent.ACTION_SENDTO)
            intent.data = Uri.parse("mailto:")
            intent.putExtra(Intent.EXTRA_EMAIL, arrayOf(binding.tvEmail.text.toString()))
            intent.putExtra(Intent.EXTRA_SUBJECT, "Contact Us")
            startActivity(Intent.createChooser(intent, "Email via..."))
        }

        binding.btSendContactUs.setOnClickListener {
            if (fieldsValid()) {
                if (isConnected(this)) {
                    hideKeyboard(this)
                    binding.btSendContactUs.visibility = View.INVISIBLE
                    binding.pbSendContactUs.visibility = View.VISIBLE
                    settingsViewModel.inquiry(
                        binding.etName.text.toString(),
                        binding.etMobile.text.toString(),
                        binding.etEmail.text.toString(),
                        binding.etSuggestion.text.toString()
                    )
                } else {
                    showSnackBar(getString(R.string.no_internet))
                }
            }
        }
    }

    private fun handleContactUsResponse(contactUsResponse: CommonResponse?) {
        binding.btSendContactUs.visibility = View.VISIBLE
        binding.pbSendContactUs.visibility = View.GONE
        if (null != contactUsResponse) {
            binding.etName.setText("")
            binding.etMobile.setText("")
            binding.etEmail.setText("")
            binding.etSuggestion.setText("")
            showAlertDialog(contactUsResponse.message)
        } else {
            showSnackBar(getString(R.string.something_went_wrong))
        }
    }

    private fun fieldsValid(): Boolean {
        if (TextUtils.isEmpty(binding.etName.text.toString())) {
            showSnackBar(getString(R.string.invalid_name))
            return false
        } else if (TextUtils.isEmpty(binding.etEmail.text.toString())) {
            showSnackBar(getString(R.string.invalid_email))
            return false
        } else if (TextUtils.isEmpty(binding.etMobile.text.toString()) || binding.etMobile.text.toString().length < 10) {
            showSnackBar(getString(R.string.invalid_phone))
            return false
        } else if (TextUtils.isEmpty(binding.etSuggestion.text.toString())) {
            showSnackBar(getString(R.string.invalid_question_comment))
            return false
        }
        return true
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun handleResponse(staticPageResponse: ContactUsResponse?) {
        binding.pbContactUs.visibility = View.GONE
        binding.cvContactUs.visibility = View.VISIBLE
        if (null != staticPageResponse) {

            binding.wbMap.settings.javaScriptEnabled = true
            binding.wbMap.loadData(staticPageResponse.contact_us[0].google_map, "text/html", "utf-8")

            binding.tvName.text = staticPageResponse.contact_us[0].name
            binding.tvAddress.text = staticPageResponse.contact_us[0].address
            binding.tvPhone.text = staticPageResponse.contact_us[0].phone
            binding.tvEmail.text = staticPageResponse.contact_us[0].email
        } else {
            showSnackBar(getString(R.string.something_went_wrong))
        }
    }

    private fun showAlertDialog(msg: String) {
        val alertDialogBuilder: AlertDialog.Builder = AlertDialog.Builder(this)
        alertDialogBuilder.setMessage(msg)
        alertDialogBuilder.setCancelable(true)

        alertDialogBuilder.setPositiveButton(
            getString(android.R.string.ok)
        ) { dialog, _ ->
            dialog.cancel()
        }

        val alertDialog: AlertDialog = alertDialogBuilder.create()
        alertDialog.show()
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE)
            .setTextColor(ContextCompat.getColor(this, R.color.red_CC252C))
    }

}