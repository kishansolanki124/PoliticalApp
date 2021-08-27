package com.app.colorsofgujarat.ui.activity

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import app.app.patidarsaurabh.apputils.AppConstants
import com.app.colorsofgujarat.R
import com.app.colorsofgujarat.apputils.*
import com.app.colorsofgujarat.databinding.ActivityQuizAndContestRunningBinding
import com.app.colorsofgujarat.pojo.CommonResponse
import com.app.colorsofgujarat.pojo.PopupBannerResponse
import com.app.colorsofgujarat.pojo.QuizAndContestRunningResponse
import com.app.colorsofgujarat.viewmodel.QuizAndContestViewModel
import com.bumptech.glide.Glide

class QuizAndContestRunningActivity : AppCompatActivity() {

    private var qid = ""
    private var answerId = ""
    private var browserURL = ""
    private var rules = ""
    private var prizeDetail = ""
    private lateinit var settingsViewModel: QuizAndContestViewModel
    private var handler: Handler? = null
    private var runnableCode: Runnable? = null
    private lateinit var binding: ActivityQuizAndContestRunningBinding

//    override val layoutId: Int
//        get() = R.layout.activity_quiz_and_contest_running

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQuizAndContestRunningBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.commonToolbar.tvTitle.visibility = View.VISIBLE
        binding.commonToolbar.tvTitle.text = getString(R.string.quiz_and_contest)
        title = ""
        binding.commonToolbar.ibBack.setOnClickListener {
            hideKeyboard(this)
            onBackPressed()
        }
        //setToolbarTitle(getString(R.string.quiz_and_contest))

        qid = intent.getStringExtra(AppConstants.ID)!!

        binding.ivSponsor.setOnClickListener {
            openBrowser(this, browserURL)
        }

        binding.btPrizeDetailsAndRules.setOnClickListener {
            showAlertDialog()
        }

        binding.btSubmitQuizContestAnswer.setOnClickListener {
            val index: Int =
                binding.rgQuestionSuggestionRunning.indexOfChild(findViewById(binding.rgQuestionSuggestionRunning.checkedRadioButtonId))

            if (index != -1) {
                if (isConnected(this)) {
                    binding.btSubmitQuizContestAnswer.visibility = View.INVISIBLE
                    binding.pbSubmitQuizContestAnswer.visibility = View.VISIBLE
                    settingsViewModel.addQuiZAnswer(
                        qid,
                        SPreferenceManager.getInstance(this).session,
                        answerId
                    )
                } else {
                    showSnackBar(getString(R.string.no_internet))
                }
            } else {
                showSnackBar(getString(R.string.invalid_option))
            }
        }

        val greenText =
            SpannableString(getString(R.string.participate_and))
        greenText.setSpan(
            ForegroundColorSpan(
                ContextCompat.getColor(
                    this,
                    R.color.black
                )
            ),
            0, greenText.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        binding.tvGiveRateGet10Point.text = greenText

//        val yellowText =
//            SpannableString(getString(R.string.give_rate_get_10_point_2))

        val yellowText = SpannableString(getPollPoints())

        yellowText.setSpan(
            ForegroundColorSpan(
                ContextCompat.getColor(
                    this,
                    R.color.red_CC252C
                )
            ),
            0, yellowText.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        binding.tvGiveRateGet10Point.append(yellowText)

        val thirdText =
            SpannableString(getString(R.string.points_and_win_prizes))
        thirdText.setSpan(
            ForegroundColorSpan(
                ContextCompat.getColor(
                    this,
                    R.color.black
                )
            ),
            0, thirdText.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        binding.tvGiveRateGet10Point.append(thirdText)

        val forthText =
            SpannableString(getString(R.string.points_and_win_prizes_2))
        forthText.setSpan(
            ForegroundColorSpan(
                ContextCompat.getColor(
                    this,
                    R.color.red_CC252C
                )
            ),
            0, forthText.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        binding.tvGiveRateGet10Point.append(forthText)

        settingsViewModel = ViewModelProvider(this).get(QuizAndContestViewModel::class.java)

        settingsViewModel.quizAndContestDetail().observe(this, {
            handleResponse(it)
        })

        settingsViewModel.quizAndContestAnswer().observe(this, {
            handleAnswerResponse(it)
        })

        if (isConnected(this)) {
            binding.cvQuizAndContestRunning.visibility = View.GONE
            binding.pbQuizAndContestRunning.visibility = View.VISIBLE
            settingsViewModel.getQuizAndContestDetail(
                qid,
                SPreferenceManager.getInstance(this).session
            )
        } else {
            showSnackBar(getString(R.string.no_internet))
        }

        if (!SPreferenceManager.getInstance(this).banners.popup_banner.isNullOrEmpty()) {
            setupRepeatableBannerAd(
                SPreferenceManager.getInstance(this).banners.delay_time,
                SPreferenceManager.getInstance(this).banners.initial_time,
                SPreferenceManager.getInstance(this).banners.popup_banner
            )
        }

    }

    private fun handleAnswerResponse(commonResponse: CommonResponse?) {
        if (null != commonResponse) {
            binding.btSubmitQuizContestAnswer.visibility = View.VISIBLE
            binding.pbSubmitQuizContestAnswer.visibility = View.GONE
            setUserPoints(commonResponse.user_points)

            //answer submitted
            binding.llQuizAndContestAnswer.visibility = View.GONE
            binding.tvAnswerSubmitted.visibility = View.VISIBLE
            showAlertDialog(commonResponse.message)
            disableRadioButtons()
        } else {
            showSnackBar(getString(R.string.something_went_wrong))
        }
    }

    private fun disableRadioButtons() {
        binding.rbExcellent.isEnabled = false
        binding.rbGood.isEnabled = false
        binding.rbcantAnswer.isEnabled = false
        binding.rbBad.isEnabled = false
    }

    private fun handleResponse(quizAndContestRunningResponse: QuizAndContestRunningResponse?) {
        binding.pbQuizAndContestRunning.visibility = View.GONE
        binding.cvQuizAndContestRunning.visibility = View.VISIBLE

        if (null != quizAndContestRunningResponse) {
            setupViews(quizAndContestRunningResponse)
        }
    }

    private fun setupViews(quizAndContestRunningResponse: QuizAndContestRunningResponse) {

        if (!quizAndContestRunningResponse.quiz_detail[0].sponser_img.isNullOrEmpty()) {
            binding.tvSponsor.visibility = View.VISIBLE
            Glide.with(this)
                .load(quizAndContestRunningResponse.quiz_detail[0].sponser_img)
                .into(binding.ivSponsor)
        } else {
            binding.tvSponsor.visibility = View.GONE
        }

        binding.tvQuestionSuggestion.text = quizAndContestRunningResponse.quiz_question[0].question

        binding.rbExcellent.text =
            quizAndContestRunningResponse.quiz_question[0].quiz_options[0].option_name
        if (quizAndContestRunningResponse.quiz_question[0].quiz_options[0].option_id == quizAndContestRunningResponse.quiz_detail[0].user_answer
        ) {
            binding.rbExcellent.isChecked = true
        }

        binding.rbGood.text =
            quizAndContestRunningResponse.quiz_question[0].quiz_options[1].option_name
        if (quizAndContestRunningResponse.quiz_question[0].quiz_options[1].option_id == quizAndContestRunningResponse.quiz_detail[0].user_answer
        ) {
            binding.rbGood.isChecked = true
        }

        binding.rbcantAnswer.text =
            quizAndContestRunningResponse.quiz_question[0].quiz_options[2].option_name
        if (quizAndContestRunningResponse.quiz_question[0].quiz_options[2].option_id == quizAndContestRunningResponse.quiz_detail[0].user_answer
        ) {
            binding.rbcantAnswer.isChecked = true
        }

        binding.rbBad.text =
            quizAndContestRunningResponse.quiz_question[0].quiz_options[3].option_name
        if (quizAndContestRunningResponse.quiz_question[0].quiz_options[3].option_id == quizAndContestRunningResponse.quiz_detail[0].user_answer
        ) {
            binding.rbBad.isChecked = true
        }

        if (quizAndContestRunningResponse.quiz_detail[0].user_answer.isNotEmpty()) {
            //answer already submitted
            binding.llQuizAndContestAnswer.visibility = View.GONE
            binding.tvAnswerSubmitted.visibility = View.VISIBLE
            disableRadioButtons()
        }

        binding.rbExcellent.setOnCheckedChangeListener { _, b ->
            if (b) {
                answerId = quizAndContestRunningResponse.quiz_question[0].quiz_options[0].option_id
            }
        }

        binding.rbGood.setOnCheckedChangeListener { _, b ->
            if (b) {
                answerId = quizAndContestRunningResponse.quiz_question[0].quiz_options[1].option_id
            }
        }

        binding.rbcantAnswer.setOnCheckedChangeListener { _, b ->
            if (b) {
                answerId = quizAndContestRunningResponse.quiz_question[0].quiz_options[2].option_id
            }
        }

        binding.rbBad.setOnCheckedChangeListener { _, b ->
            if (b) {
                answerId = quizAndContestRunningResponse.quiz_question[0].quiz_options[3].option_id
            }
        }

        binding.tvQuizStartDate.text = quizAndContestRunningResponse.quiz_detail[0].start_date
        binding.tvQuizEndDate.text = quizAndContestRunningResponse.quiz_detail[0].end_date
        binding.tvQuizWinnerDate.text = quizAndContestRunningResponse.quiz_detail[0].result_date

        browserURL = quizAndContestRunningResponse.quiz_detail[0].sponser_url
        rules = quizAndContestRunningResponse.quiz_detail[0].quiz_rules
        prizeDetail = quizAndContestRunningResponse.quiz_detail[0].quiz_detail

        binding.ivShareQuizAndContestDetail.setOnClickListener {
            shareIntent(
                "Participate in Quiz and Contest and Win Prizes:\n\n" +
                        quizAndContestRunningResponse.quiz_question[0].question,
                this
            )
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
        val tvPrizeDetail = layout.findViewById(R.id.tvPrizeDetail) as TextView

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

    private fun setupRepeatableBannerAd(
        delayTime: String,
        initialTime: String,
        popupBanner: List<PopupBannerResponse.PopupBanner>
    ) {
        handler = Handler(Looper.getMainLooper())
        runnableCode = object : Runnable {
            override fun run() {
                if (!isDestroyed && !this@QuizAndContestRunningActivity.isFinishing) {
                    if (this@QuizAndContestRunningActivity.lifecycle.currentState
                            .isAtLeast(Lifecycle.State.RESUMED)
                    ) {
                        showProgressDialog(this@QuizAndContestRunningActivity, popupBanner)
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

    override fun onDestroy() {
        super.onDestroy()
        if (null != handler) {
            runnableCode?.let {
                handler!!.removeCallbacks(it)
            }
        }
    }
}