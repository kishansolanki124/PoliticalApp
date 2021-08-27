package com.app.colorsofgujarat.ui.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.MotionEvent
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import androidx.lifecycle.ViewModelProvider
import app.app.patidarsaurabh.apputils.AppConstants
import com.app.colorsofgujarat.R
import com.app.colorsofgujarat.apputils.*
import com.app.colorsofgujarat.databinding.ActivityAddQuestionSuggestionBinding
import com.app.colorsofgujarat.pojo.CommonResponse
import com.app.colorsofgujarat.pojo.SettingsResponse
import com.app.colorsofgujarat.viewmodel.UserAdviseViewModel
import com.bumptech.glide.Glide
import com.github.drjacky.imagepicker.ImagePicker
import java.io.File

class AddQuestionSuggestionActivity : AppCompatActivity() {

    private lateinit var govtWorkViewModel: UserAdviseViewModel
    private var districtId = ""
    private var districtPosition = 0
    private lateinit var selectedFile: File
    private var districtList: ArrayList<SettingsResponse.District> = ArrayList()
    private lateinit var binding: ActivityAddQuestionSuggestionBinding

//    override val layoutId: Int
//        get() = R.layout.activity_add_question_suggestion

    private val launcher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                val uri = it.data?.data!!
                // Use the uri to load the image
                val filePath = FetchPath.getPath(this, uri)
                selectedFile = File(filePath)
                Glide.with(this).load(selectedFile).into(binding.ivSelectedImage)
            }
        }

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAddQuestionSuggestionBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.commonToolbar.tvTitle.visibility = View.VISIBLE
        binding.commonToolbar.tvTitle.text = getString(R.string.que_suggestion)
        title = ""
        binding.commonToolbar.ibBack.setOnClickListener {
            hideKeyboard(this)
            onBackPressed()
        }

        districtPosition = intent.getIntExtra(AppConstants.DISTRICTID, 0)

        //setToolbarTitle(getString(R.string.que_suggestion))
        setupDistrictSpinner()

        binding.tvTNC.text = HtmlCompat.fromHtml(
            getTermsByName("User Question"),
            HtmlCompat.FROM_HTML_MODE_LEGACY
        )

        govtWorkViewModel = ViewModelProvider(this).get(UserAdviseViewModel::class.java)

        govtWorkViewModel.addQuestionResponse().observe(this, {
            handleResponse(it)
        })

        binding.btSubmitQuestion.setOnClickListener {
            if (fieldsAreValid()) {
                if (isConnected(this)) {
                    if (!::selectedFile.isInitialized) {
                        showUploadStoryWithoutImageAlertDialog()
                    } else {
                        binding.pbSubmitQuizContestAnswer.visibility = View.VISIBLE
                        binding.btSubmitQuestion.visibility = View.INVISIBLE
                        govtWorkViewModel.addUserAdvice(
                            districtId,
                            SPreferenceManager.getInstance(this).session,
                            binding.etCity.text.toString(),
                            binding.etTitle.text.toString(),
                            binding.etDescription.text.toString(),
                            selectedFile
                        )
                    }
                } else {
                    showSnackBar(getString(R.string.no_internet))
                }
            }
        }

        binding.rlImage.setOnClickListener {
            //GligarPicker().requestCode(100).limit(1).withActivity(this).show()
            launcher.launch(
                ImagePicker.with(this)
                    .galleryOnly()
                    .createIntent()
            )
        }

        binding.ivCamera.setOnClickListener {
            binding.rlImage.callOnClick()
        }

        binding.ivSelectedImage.setOnClickListener {
            binding.rlImage.callOnClick()
        }

        binding.etDescription.setOnTouchListener { view, event ->
            view.parent.requestDisallowInterceptTouchEvent(true)
            if ((event.action and MotionEvent.ACTION_MASK) == MotionEvent.ACTION_UP) {
                view.parent.requestDisallowInterceptTouchEvent(false)
            }
            return@setOnTouchListener false
        }

    }

    private fun handleResponse(commonResponse: CommonResponse?) {
        if (null != commonResponse) {
            binding.btSubmitQuestion.visibility = View.VISIBLE
            binding.pbSubmitQuizContestAnswer.visibility = View.GONE
            showAlertDialog(commonResponse.message)
        }
    }

    private fun fieldsAreValid(): Boolean {
        return when {
            TextUtils.isEmpty(binding.etTitle.text.toString()) -> {
                showSnackBar(getString(R.string.Enter_Title))
                false
            }
            TextUtils.isEmpty(binding.etDescription.text.toString()) -> {
                showSnackBar(getString(R.string.Enter_Desc))
                false
            }
            TextUtils.isEmpty(binding.etCity.text.toString()) -> {
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
        binding.spDistrictQuestionSuggestion.adapter = adapter

        binding.spDistrictQuestionSuggestion.setSelection(districtPosition)

        binding.spDistrictQuestionSuggestion.onItemSelectedListener =
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

//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if (resultCode != Activity.RESULT_OK) {
//            return
//        }
//
//        when (requestCode) {
//            100 -> {
//                val imagesList =
//                    data?.extras?.getStringArray(GligarPicker.IMAGES_RESULT)// return list of selected images paths.
//                if (!imagesList.isNullOrEmpty()) {
//                    //.load(File(imagePath))
//                    selectedFile = File(imagesList[0])
//                    Glide.with(this).load(selectedFile).into(ivSelectedImage)
//
//
////                    val requestFile: RequestBody = selectedFile
////                        .asRequestBody(contentResolver.getType(Uri.fromFile(selectedFile))!!.toMediaTypeOrNull())
////
////                    val body: Part =
////                        createFormData.createFormData("picture", selectedFile.name, requestFile)
//                }
//            }
//        }
//    }

    private fun showAlertDialog(msg: String) {
        val alertDialogBuilder: AlertDialog.Builder = AlertDialog.Builder(this)
        alertDialogBuilder.setMessage(msg)
        alertDialogBuilder.setCancelable(false)

        alertDialogBuilder.setPositiveButton(
            getString(android.R.string.ok)
        ) { dialog, _ ->
            dialog.dismiss()
            val intent = Intent()
            intent.putExtra(AppConstants.REFRESH, true)
            setResult(Activity.RESULT_OK, intent)
            finish()
        }

        val alertDialog: AlertDialog = alertDialogBuilder.create()
        alertDialog.show()
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE)
            .setTextColor(ContextCompat.getColor(this, R.color.red_CC252C))
    }

    private fun showUploadStoryWithoutImageAlertDialog() {
        val alertDialogBuilder: AlertDialog.Builder = AlertDialog.Builder(this)
        alertDialogBuilder.setMessage("Are you sure to continue without photograph ?")
        alertDialogBuilder.setCancelable(false)

        alertDialogBuilder.setPositiveButton(
            getString(android.R.string.ok)
        ) { dialog, _ ->
            dialog.dismiss()
            binding.pbSubmitQuizContestAnswer.visibility = View.VISIBLE
            binding.btSubmitQuestion.visibility = View.INVISIBLE
            govtWorkViewModel.addUserAdvice(
                districtId,
                SPreferenceManager.getInstance(this).session,
                binding.etCity.text.toString(),
                binding.etTitle.text.toString(),
                binding.etDescription.text.toString(),
                null
            )
        }

        alertDialogBuilder.setNegativeButton(
            getString(android.R.string.cancel)
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