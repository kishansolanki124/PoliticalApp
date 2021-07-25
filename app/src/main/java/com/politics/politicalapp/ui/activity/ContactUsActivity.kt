package com.politics.politicalapp.ui.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.politics.politicalapp.R
import com.politics.politicalapp.apputils.hideKeyboard
import com.politics.politicalapp.apputils.isConnected
import com.politics.politicalapp.apputils.showSnackBar
import com.politics.politicalapp.pojo.CommonResponse
import com.politics.politicalapp.pojo.ContactUsResponse
import com.politics.politicalapp.viewmodel.SettingsViewModel
import kotlinx.android.synthetic.main.activity_contact_us.*

class ContactUsActivity : ExtendedToolbarActivity() {

    private lateinit var settingsViewModel: SettingsViewModel

    override val layoutId: Int
        get() = R.layout.activity_contact_us

    @SuppressLint("QueryPermissionsNeeded")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setToolbarTitle(getString(R.string.Contact_Us))
        settingsViewModel = ViewModelProvider(this).get(SettingsViewModel::class.java)

        settingsViewModel.contactUs().observe(this, {
            handleResponse(it)
        })

        settingsViewModel.commonResponse().observe(this, {
            handleContactUsResponse(it)
        })

        if (isConnected(this)) {
            cvContactUs.visibility = View.GONE
            pbContactUs.visibility = View.VISIBLE
            settingsViewModel.getContactUs()
        } else {
            showSnackBar(getString(R.string.no_internet), this)
        }

        tvPhone.setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel:${tvPhone.text}")
            startActivity(intent)
        }

        tvEmail.setOnClickListener {
            val intent = Intent(Intent.ACTION_SENDTO)
            intent.data = Uri.parse("mailto:")
            intent.putExtra(Intent.EXTRA_EMAIL, arrayOf(tvEmail.text.toString()))
            intent.putExtra(Intent.EXTRA_SUBJECT, "Contact Us")
            startActivity(Intent.createChooser(intent, "Email via..."))
        }

        btSendContactUs.setOnClickListener {
            if (fieldsValid()) {
                if (isConnected(this)) {
                    hideKeyboard(this)
                    btSendContactUs.visibility = View.INVISIBLE
                    pbSendContactUs.visibility = View.VISIBLE
                    settingsViewModel.inquiry(
                        etName.text.toString(),
                        etMobile.text.toString(),
                        etEmail.text.toString(),
                        etSuggestion.text.toString()
                    )
                } else {
                    showSnackBar(getString(R.string.no_internet))
                }
            }
        }
    }

    private fun handleContactUsResponse(contactUsResponse: CommonResponse?) {
        btSendContactUs.visibility = View.VISIBLE
        pbSendContactUs.visibility = View.GONE
        if (null != contactUsResponse) {
            etName.setText("")
            etMobile.setText("")
            etEmail.setText("")
            etSuggestion.setText("")
            showAlertDialog(contactUsResponse.message)
        } else {
            showSnackBar(getString(R.string.something_went_wrong))
        }
    }

    private fun fieldsValid(): Boolean {
        if (TextUtils.isEmpty(etName.text.toString())) {
            showSnackBar(getString(R.string.invalid_name))
            return false
        } else if (TextUtils.isEmpty(etEmail.text.toString())) {
            showSnackBar(getString(R.string.invalid_email))
            return false
        } else if (TextUtils.isEmpty(etMobile.text.toString()) || etMobile.text.toString().length < 10) {
            showSnackBar(getString(R.string.invalid_phone))
            return false
        } else if (TextUtils.isEmpty(etSuggestion.text.toString())) {
            showSnackBar(getString(R.string.invalid_question_comment))
            return false
        }
        return true
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun handleResponse(staticPageResponse: ContactUsResponse?) {
        pbContactUs.visibility = View.GONE
        cvContactUs.visibility = View.VISIBLE
        if (null != staticPageResponse) {

            wbMap.settings.javaScriptEnabled = true
            wbMap.loadData(staticPageResponse.contact_us[0].google_map, "text/html", "utf-8")

            tvName.text = staticPageResponse.contact_us[0].name
            tvAddress.text = staticPageResponse.contact_us[0].address
            tvPhone.text = staticPageResponse.contact_us[0].phone
            tvEmail.text = staticPageResponse.contact_us[0].email
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