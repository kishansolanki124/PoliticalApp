package com.politics.politicalapp.ui.activity

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import androidx.lifecycle.ViewModelProvider
import app.app.patidarsaurabh.apputils.AppConstants
import com.bumptech.glide.Glide
import com.politics.politicalapp.R
import com.politics.politicalapp.apputils.*
import com.politics.politicalapp.pojo.CommonResponse
import com.politics.politicalapp.pojo.QuizAndContestRunningResponse
import com.politics.politicalapp.viewmodel.QuizAndContestViewModel
import kotlinx.android.synthetic.main.activity_quiz_and_contest_running.*

class QuizAndContestRunningActivity : ExtendedToolbarActivity() {

    private var qid = ""
    private var answerId = ""
    private var browserURL = ""
    private var rules = ""
    private var prizeDetail = ""
    private lateinit var settingsViewModel: QuizAndContestViewModel

    override val layoutId: Int
        get() = R.layout.activity_quiz_and_contest_running

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setToolbarTitle(getString(R.string.quiz_and_contest))

        qid = intent.getStringExtra(AppConstants.ID)!!

        ivSponsor.setOnClickListener {
            openBrowser(this, browserURL)
        }

        btPrizeDetailsAndRules.setOnClickListener {
            showAlertDialog()
        }

        btSubmitQuizContestAnswer.setOnClickListener {
            val index: Int =
                rgQuestionSuggestionRunning.indexOfChild(findViewById(rgQuestionSuggestionRunning.checkedRadioButtonId))

            if (index != -1) {
                if (isConnected(this)) {
                    btSubmitQuizContestAnswer.visibility = View.INVISIBLE
                    pbSubmitQuizContestAnswer.visibility = View.VISIBLE
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
        tvGive_rate_get_10_point.text = greenText

        val yellowText =
            SpannableString(getString(R.string.give_rate_get_10_point_2))
        yellowText.setSpan(
            ForegroundColorSpan(
                ContextCompat.getColor(
                    this,
                    R.color.red_CC252C
                )
            ),
            0, yellowText.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        tvGive_rate_get_10_point.append(yellowText)

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
        tvGive_rate_get_10_point.append(thirdText)

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
        tvGive_rate_get_10_point.append(forthText)

        settingsViewModel = ViewModelProvider(this).get(QuizAndContestViewModel::class.java)

        settingsViewModel.quizAndContestDetail().observe(this, {
            handleResponse(it)
        })

        settingsViewModel.quizAndContestAnswer().observe(this, {
            handleAnswerResponse(it)
        })

        if (isConnected(this)) {
            cvQuizAndContestRunning.visibility = View.GONE
            pbQuizAndContestRunning.visibility = View.VISIBLE
            settingsViewModel.getQuizAndContestDetail(
                qid,
                SPreferenceManager.getInstance(this).session
            )
        } else {
            showSnackBar(getString(R.string.no_internet))
        }
    }

    private fun handleAnswerResponse(commonResponse: CommonResponse?) {
        if (null != commonResponse) {
            btSubmitQuizContestAnswer.visibility = View.VISIBLE
            pbSubmitQuizContestAnswer.visibility = View.GONE
            setUserPoints(commonResponse.user_points)

            //answer submitted
            llQuizAndContestAnswer.visibility = View.GONE
            tvAnswerSubmitted.visibility = View.VISIBLE
            showAlertDialog(commonResponse.message)
            disableRadioButtons()
        } else {
            showSnackBar(getString(R.string.something_went_wrong))
        }
    }

    private fun disableRadioButtons() {
        rbExcellent.isEnabled = false
        rbGood.isEnabled = false
        rbcantAnswer.isEnabled = false
        rbBad.isEnabled = false
    }

    private fun handleResponse(quizAndContestRunningResponse: QuizAndContestRunningResponse?) {
        pbQuizAndContestRunning.visibility = View.GONE
        cvQuizAndContestRunning.visibility = View.VISIBLE

        if (null != quizAndContestRunningResponse) {
            setupViews(quizAndContestRunningResponse)
        }
    }

    private fun setupViews(quizAndContestRunningResponse: QuizAndContestRunningResponse) {
        Glide.with(this)
            .load(quizAndContestRunningResponse.quiz_detail[0].sponser_img)
            .into(ivSponsor)

        tvQuestionSuggestion.text = quizAndContestRunningResponse.quiz_question[0].question

        rbExcellent.text =
            quizAndContestRunningResponse.quiz_question[0].quiz_options[0].option_name
        if (quizAndContestRunningResponse.quiz_question[0].quiz_options[0].option_id == quizAndContestRunningResponse.quiz_detail[0].user_answer
        ) {
            rbExcellent.isChecked = true
        }

        rbGood.text = quizAndContestRunningResponse.quiz_question[0].quiz_options[1].option_name
        if (quizAndContestRunningResponse.quiz_question[0].quiz_options[1].option_id == quizAndContestRunningResponse.quiz_detail[0].user_answer
        ) {
            rbGood.isChecked = true
        }

        rbcantAnswer.text =
            quizAndContestRunningResponse.quiz_question[0].quiz_options[2].option_name
        if (quizAndContestRunningResponse.quiz_question[0].quiz_options[2].option_id == quizAndContestRunningResponse.quiz_detail[0].user_answer
        ) {
            rbcantAnswer.isChecked = true
        }

        rbBad.text = quizAndContestRunningResponse.quiz_question[0].quiz_options[3].option_name
        if (quizAndContestRunningResponse.quiz_question[0].quiz_options[3].option_id == quizAndContestRunningResponse.quiz_detail[0].user_answer
        ) {
            rbBad.isChecked = true
        }

        if (quizAndContestRunningResponse.quiz_detail[0].user_answer.isNotEmpty()) {
            //answer already submitted
            llQuizAndContestAnswer.visibility = View.GONE
            tvAnswerSubmitted.visibility = View.VISIBLE
            disableRadioButtons()
        }

        rbExcellent.setOnCheckedChangeListener { _, b ->
            if (b) {
                answerId = quizAndContestRunningResponse.quiz_question[0].quiz_options[0].option_id
            }
        }

        rbGood.setOnCheckedChangeListener { _, b ->
            if (b) {
                answerId = quizAndContestRunningResponse.quiz_question[0].quiz_options[1].option_id
            }
        }

        rbcantAnswer.setOnCheckedChangeListener { _, b ->
            if (b) {
                answerId = quizAndContestRunningResponse.quiz_question[0].quiz_options[2].option_id
            }
        }

        rbBad.setOnCheckedChangeListener { _, b ->
            if (b) {
                answerId = quizAndContestRunningResponse.quiz_question[0].quiz_options[3].option_id
            }
        }

        tvQuizStartDate.text = quizAndContestRunningResponse.quiz_detail[0].start_date
        tvQuizEndDate.text = quizAndContestRunningResponse.quiz_detail[0].end_date
        tvQuizWinnerDate.text = quizAndContestRunningResponse.quiz_detail[0].result_date

        browserURL = quizAndContestRunningResponse.quiz_detail[0].sponser_url
        rules = quizAndContestRunningResponse.quiz_detail[0].quiz_rules
        prizeDetail = quizAndContestRunningResponse.quiz_detail[0].quiz_detail

        ivShareQuizAndContestDetail.setOnClickListener {
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
}