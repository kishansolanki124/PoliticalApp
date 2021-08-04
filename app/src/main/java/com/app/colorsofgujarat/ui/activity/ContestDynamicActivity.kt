package com.app.colorsofgujarat.ui.activity

import android.app.Activity
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import com.app.colorsofgujarat.R
import com.app.colorsofgujarat.apputils.*
import com.app.colorsofgujarat.pojo.CommonResponse
import com.app.colorsofgujarat.pojo.PopupBannerResponse
import com.app.colorsofgujarat.pojo.QuizAndContestDynamicResponse
import com.app.colorsofgujarat.viewmodel.QuizAndContestViewModel
import com.app.colorsofgujarat.viewmodel.SettingsViewModel
import com.bumptech.glide.Glide
import com.github.drjacky.imagepicker.ImagePicker
import kotlinx.android.synthetic.main.activity_contest_dynamic.*
import java.io.File

class ContestDynamicActivity : ExtendedToolbarActivity() {

    private lateinit var selectedFile: File
    private lateinit var quizAndContestViewModel: QuizAndContestViewModel
    private lateinit var settingsViewModel: SettingsViewModel
    private var handler: Handler? = null
    private var runnableCode: Runnable? = null
    private var rules = ""
    private var cid = ""
    private var prizeDetail = ""
    private var winners = ""

    override val layoutId: Int
        get() = R.layout.activity_contest_dynamic

    private val launcher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                val uri = it.data?.data!!
                // Use the uri to load the image
                val filePath = FetchPath.getPath(this, uri)
                selectedFile = File(filePath)
                Glide.with(this).load(selectedFile).into(ivSelectedImage)
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setToolbarTitle(SPreferenceManager.getInstance(this).settings.contest[0].menu_name)

        quizAndContestViewModel = ViewModelProvider(this).get(QuizAndContestViewModel::class.java)

        settingsViewModel = ViewModelProvider(this).get(SettingsViewModel::class.java)

        settingsViewModel.popupBanners().observe(this, {
            handleResponse(it)
        })

        quizAndContestViewModel.quizAndContestDynamic().observe(this, {
            handleResponse(it)
        })

        quizAndContestViewModel.quizAndContestAnswer().observe(this, {
            handleCommonResponse(it)
        })

        if (isConnected(this)) {
            llContestDynamicMain.visibility = View.GONE
            pbContestDynamic.visibility = View.VISIBLE
            quizAndContestViewModel.getQuizAndContestDynamic(SPreferenceManager.getInstance(this).session)
            settingsViewModel.getPopupBanner("photo_contest")
        } else {
            showSnackBar(getString(R.string.no_internet))
        }

        btSelectImage.setOnClickListener {
            //GligarPicker().requestCode(100).limit(1).withActivity(this).show()
            launcher.launch(
                ImagePicker.with(this)
                    .galleryOnly()
                    .createIntent()
            )
        }

        btPrizeDetailsAndRules.setOnClickListener {
            showAlertDialog()
        }

        btWinners.setOnClickListener {
            showWinnerAlertDialog()
        }

        btSubmitQuestion.setOnClickListener {
            if (fieldsAreValid()) {
                if (isConnected(this)) {
                    pbSubmitQuizContestAnswer.visibility = View.VISIBLE
                    btSubmitQuestion.visibility = View.INVISIBLE
                    quizAndContestViewModel.addPhotoContestUser(
                        SPreferenceManager.getInstance(this).session,
                        cid,
                        selectedFile
                    )
                } else {
                    showSnackBar(getString(R.string.no_internet))
                }
            }
        }

    }

    private fun handleCommonResponse(commonResponse: CommonResponse?) {
        if (null != commonResponse) {
            pbSubmitQuizContestAnswer.visibility = View.GONE
            btSubmitQuestion.visibility = View.GONE
            btSelectImage.visibility = View.GONE

            tvAlreadySubmittedResponse.text = commonResponse.message
            tvAlreadySubmittedResponse.visibility = View.VISIBLE

            showAlertDialog(commonResponse.message)
        }
    }

    private fun handleResponse(quizAndContestResponse: QuizAndContestDynamicResponse) {
        llContestDynamicMain.visibility = View.VISIBLE
        pbContestDynamic.visibility = View.GONE

        tvTitleQuiz.text = quizAndContestResponse.photo_contest[0].contest_name

        rules = quizAndContestResponse.photo_contest[0].contest_rules
        winners = quizAndContestResponse.photo_contest[0].contest_winner
        cid = quizAndContestResponse.photo_contest[0].id
        prizeDetail = quizAndContestResponse.photo_contest[0].contest_detail

        if (null != quizAndContestResponse.photo_contest[0].user_participate) {
            tvAlreadySubmittedResponse.visibility = View.VISIBLE
            btSubmitQuestion.visibility = View.GONE
            btSelectImage.visibility = View.GONE
        } else {
            tvAlreadySubmittedResponse.visibility = View.GONE
            btSubmitQuestion.visibility = View.VISIBLE
            btSelectImage.visibility = View.VISIBLE
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
//                    selectedFile = File(imagesList[0])
//                    Glide.with(this).load(selectedFile).into(ivSelectedImage)
//                }
//            }
//        }
//    }


    private fun fieldsAreValid(): Boolean {
        return when {
            (!::selectedFile.isInitialized) -> {
                showSnackBar(getString(R.string.Select_Photo))
                false
            }
            else -> true
        }
    }

    private fun showAlertDialog() {
        val builder = AlertDialog.Builder(this)

        val viewGroup = this.findViewById<ViewGroup>(R.id.content)
        val layout: View = LayoutInflater.from(this)
            .inflate(R.layout.dialog_quize_rules, viewGroup, false)

        builder.setView(layout)
        val alertDialog = builder.create()
        alertDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val ivClose = layout.findViewById(R.id.ivClose) as ImageView
        val tvRules = layout.findViewById(R.id.tvRules) as TextView
        val tvPrizeDetailTitle = layout.findViewById(R.id.tvPrizeDetailTitle) as TextView
        val tvPrizeDetail = layout.findViewById(R.id.tvPrizeDetail) as TextView

        tvPrizeDetailTitle.text = getString(R.string.contest_detail_)

        tvRules.text = HtmlCompat.fromHtml(
            rules,
            HtmlCompat.FROM_HTML_MODE_LEGACY
        )
        tvPrizeDetail.text = HtmlCompat.fromHtml(
            prizeDetail,
            HtmlCompat.FROM_HTML_MODE_LEGACY
        )

        ivClose.setOnClickListener {
            alertDialog.dismiss()
        }

        alertDialog.show()
    }

    private fun showWinnerAlertDialog() {
        val builder = AlertDialog.Builder(this)

        val viewGroup = this.findViewById<ViewGroup>(R.id.content)
        val layout: View = LayoutInflater.from(this)
            .inflate(R.layout.dialog_winners, viewGroup, false)

        builder.setView(layout)
        val alertDialog = builder.create()
        alertDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val ivClose = layout.findViewById(R.id.ivClose) as ImageView
        val tvWinnerList = layout.findViewById(R.id.tvWinnerList) as TextView

        tvWinnerList.text = HtmlCompat.fromHtml(
            winners,
            HtmlCompat.FROM_HTML_MODE_LEGACY
        )

        ivClose.setOnClickListener {
            alertDialog.dismiss()
        }

        alertDialog.show()
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

    private fun handleResponse(popupBannerResponse: PopupBannerResponse) {
        if (!popupBannerResponse.popup_banner.isNullOrEmpty()) {
            setupRepeatableBannerAd(
                popupBannerResponse.delay_time, popupBannerResponse.initial_time,
                popupBannerResponse.popup_banner
            )
        }
    }

    private fun setupRepeatableBannerAd(
        delayTime: String,
        initialTime: String,
        popupBanner: List<PopupBannerResponse.PopupBanner>
    ) {
        handler = Handler(Looper.getMainLooper())
        runnableCode = object : Runnable {
            override fun run() {
                if (!isDestroyed && !this@ContestDynamicActivity.isFinishing) {
                    if (this@ContestDynamicActivity.lifecycle.currentState
                            .isAtLeast(Lifecycle.State.RESUMED)
                    ) {
                        showProgressDialog(this@ContestDynamicActivity, popupBanner)
                    }
                    handler?.postDelayed(this, delayTime.toLong() * 1000)
                }
            }
        }

        if (!isDestroyed && !this.isFinishing) {
            runnableCode?.let {
                handler?.postDelayed(it, initialTime.toLong() * 1000)
            }
        }
    }
}