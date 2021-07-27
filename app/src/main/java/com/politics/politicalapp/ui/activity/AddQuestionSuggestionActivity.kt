package com.politics.politicalapp.ui.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import androidx.lifecycle.ViewModelProvider
import app.app.patidarsaurabh.apputils.AppConstants
import com.bumptech.glide.Glide
import com.opensooq.supernova.gligar.GligarPicker
import com.politics.politicalapp.R
import com.politics.politicalapp.apputils.SPreferenceManager
import com.politics.politicalapp.apputils.getTermsByName
import com.politics.politicalapp.apputils.isConnected
import com.politics.politicalapp.apputils.showSnackBar
import com.politics.politicalapp.pojo.CommonResponse
import com.politics.politicalapp.pojo.SettingsResponse
import com.politics.politicalapp.viewmodel.UserAdviseViewModel
import kotlinx.android.synthetic.main.activity_add_question_suggestion.*
import java.io.File

class AddQuestionSuggestionActivity : ExtendedToolbarActivity() {

    private lateinit var govtWorkViewModel: UserAdviseViewModel
    private var districtId = ""
    private var districtPosition = 0
    private lateinit var selectedFile: File
    private var districtList: ArrayList<SettingsResponse.District> = ArrayList()

    override val layoutId: Int
        get() = R.layout.activity_add_question_suggestion

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        districtPosition = intent.getIntExtra(AppConstants.DISTRICTID, 0)

        setToolbarTitle(getString(R.string.que_suggestion))
        setupDistrictSpinner()

        tvTNC.text = HtmlCompat.fromHtml(
            getTermsByName("User Question"),
            HtmlCompat.FROM_HTML_MODE_LEGACY
        )

        govtWorkViewModel = ViewModelProvider(this).get(UserAdviseViewModel::class.java)

        govtWorkViewModel.addQuestionResponse().observe(this, {
            handleResponse(it)
        })

        btSubmitQuestion.setOnClickListener {
            if (fieldsAreValid()) {
                if (isConnected(this)) {
                    pbSubmitQuizContestAnswer.visibility = View.VISIBLE
                    btSubmitQuestion.visibility = View.INVISIBLE
                    govtWorkViewModel.addUserAdvice(
                        districtId,
                        SPreferenceManager.getInstance(this).session,
                        etCity.text.toString(),
                        etTitle.text.toString(),
                        etDescription.text.toString(),
                        selectedFile
                    )
                } else {
                    showSnackBar(getString(R.string.no_internet))
                }
            }
        }

        rlImage.setOnClickListener {
            GligarPicker().requestCode(100).limit(1).withActivity(this).show()
        }

        ivCamera.setOnClickListener {
            rlImage.callOnClick()
        }

        ivSelectedImage.setOnClickListener {
            rlImage.callOnClick()
        }
    }

    private fun handleResponse(commonResponse: CommonResponse?) {
        if (null != commonResponse) {
            btSubmitQuestion.visibility = View.VISIBLE
            pbSubmitQuizContestAnswer.visibility = View.GONE
            showAlertDialog(commonResponse.message)
        }
    }

    private fun fieldsAreValid(): Boolean {
        return when {

            (!::selectedFile.isInitialized) -> {
                showSnackBar(getString(R.string.add_image))
                false
            }

            TextUtils.isEmpty(etTitle.text.toString()) -> {
                showSnackBar(getString(R.string.Enter_Title))
                false
            }
            TextUtils.isEmpty(etDescription.text.toString()) -> {
                showSnackBar(getString(R.string.Enter_Desc))
                false
            }
            TextUtils.isEmpty(etCity.text.toString()) -> {
                showSnackBar(getString(R.string.Enter_City))
                false
            }
            else -> true
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
        spDistrictQuestionSuggestion.adapter = adapter

        spDistrictQuestionSuggestion.setSelection(districtPosition)

        spDistrictQuestionSuggestion.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != Activity.RESULT_OK) {
            return
        }

        when (requestCode) {
            100 -> {
                val imagesList =
                    data?.extras?.getStringArray(GligarPicker.IMAGES_RESULT)// return list of selected images paths.
                if (!imagesList.isNullOrEmpty()) {
                    //.load(File(imagePath))
                    selectedFile = File(imagesList[0])
                    Glide.with(this).load(selectedFile).into(ivSelectedImage)


//                    val requestFile: RequestBody = selectedFile
//                        .asRequestBody(contentResolver.getType(Uri.fromFile(selectedFile))!!.toMediaTypeOrNull())
//
//                    val body: Part =
//                        createFormData.createFormData("picture", selectedFile.name, requestFile)
                }
            }
        }
    }

    private fun showAlertDialog(msg: String) {
        val alertDialogBuilder: AlertDialog.Builder = AlertDialog.Builder(this)
        alertDialogBuilder.setMessage(msg)
        alertDialogBuilder.setCancelable(false)

        alertDialogBuilder.setPositiveButton(
            getString(android.R.string.ok)
        ) { dialog, _ ->
            dialog.cancel()
            finish()
        }

        val alertDialog: AlertDialog = alertDialogBuilder.create()
        alertDialog.show()
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE)
            .setTextColor(ContextCompat.getColor(this, R.color.red_CC252C))
    }
}