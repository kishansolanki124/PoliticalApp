package com.politics.politicalapp.ui.activity

import android.graphics.Color
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import app.app.patidarsaurabh.apputils.AppConstants
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.github.mikephil.charting.utils.ColorTemplate
import com.github.mikephil.charting.utils.MPPointF
import com.politics.politicalapp.R
import com.politics.politicalapp.apputils.MyValueFormatter
import com.politics.politicalapp.apputils.SPreferenceManager
import com.politics.politicalapp.apputils.isConnected
import com.politics.politicalapp.apputils.showSnackBar
import com.politics.politicalapp.pojo.CommonResponse
import com.politics.politicalapp.pojo.LivePollDetailResponse
import com.politics.politicalapp.viewmodel.LivePollViewModel
import kotlinx.android.synthetic.main.activity_live_poll_running.*
import java.util.*

class LivePollRunningActivity : ExtendedToolbarActivity(), OnChartValueSelectedListener {

    private var qid = ""
    private var answerId = ""
    private lateinit var settingsViewModel: LivePollViewModel
    private var livePollDetailResponse: LivePollDetailResponse? = null

    private val showButton: Boolean by lazy {
        // runs on first access of messageView
        intent.getBooleanExtra(AppConstants.SHOW_SUBMIT, false)
    }

    override val layoutId: Int
        get() = R.layout.activity_live_poll_running

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        qid = intent.getStringExtra(AppConstants.ID)!!

        if (showButton) {
            btSubmitLivePOllAnswer.visibility = View.VISIBLE
            tvGive_rate_get_10_point.visibility = View.VISIBLE
        } else {
            btSubmitLivePOllAnswer.visibility = View.INVISIBLE
            tvGive_rate_get_10_point.visibility = View.INVISIBLE
        }

        setToolbarTitle(getString(R.string.live_poll))
        setupPointText()

        btSubmitLivePOllAnswer.setOnClickListener {
            val index: Int =
                rgLivePollRunning.indexOfChild(findViewById(rgLivePollRunning.checkedRadioButtonId))

            if (index != -1) {
                if (isConnected(this)) {
                    btSubmitLivePOllAnswer.visibility = View.INVISIBLE
                    pbSubmitLivePOllAnswer.visibility = View.VISIBLE
                    settingsViewModel.addLivePollAnswer(
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

        settingsViewModel = ViewModelProvider(this).get(LivePollViewModel::class.java)

        settingsViewModel.quizAndContestDetail().observe(this, {
            handleResponse(it)
        })

        settingsViewModel.quizAndContestAnswer().observe(this, {
            handleAnswerResponse(it)
        })

        if (isConnected(this)) {
            cvQuizAndContestRunning.visibility = View.GONE
            pbQuizAndContestRunning.visibility = View.VISIBLE
            settingsViewModel.getLivePollDetail(
                qid,
                SPreferenceManager.getInstance(this).session
            )
        } else {
            showSnackBar(getString(R.string.no_internet))
        }
    }

    private fun setupPointText() {
        val greenText = SpannableString(getString(R.string.participate_and))
        greenText.setSpan(
            ForegroundColorSpan(ContextCompat.getColor(this, R.color.black)),
            0, greenText.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        tvGive_rate_get_10_point.text = greenText

        val yellowText = SpannableString(getString(R.string.give_rate_get_10_point_2))
        yellowText.setSpan(
            ForegroundColorSpan(ContextCompat.getColor(this, R.color.red_CC252C)),
            0, yellowText.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        tvGive_rate_get_10_point.append(yellowText)

        val thirdText = SpannableString(getString(R.string.give_rate_get_10_point_3))
        thirdText.setSpan(
            ForegroundColorSpan(ContextCompat.getColor(this, R.color.black)),
            0, thirdText.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        tvGive_rate_get_10_point.append(thirdText)
    }

    private fun setupChart() {
        chart.description.isEnabled = false
        chart.setExtraOffsets(5f, 10f, 5f, 5f)

        chart.dragDecelerationFrictionCoef = 0.95f

        //chart.setCenterTextTypeface(tfLight);
        //chart.setCenterText(generateCenterSpannableText());

        //chart.setDrawHoleEnabled(true);
        //chart.setHoleColor(Color.WHITE);


        //chart.setCenterTextTypeface(tfLight);
        //chart.setCenterText(generateCenterSpannableText());

        //chart.setDrawHoleEnabled(true);
        //chart.setHoleColor(Color.WHITE);
        chart.setTransparentCircleColor(Color.WHITE)
        chart.setTransparentCircleAlpha(110)

        //chart.setHoleRadius(58f);

        //chart.setHoleRadius(58f);
        chart.transparentCircleRadius = 61f

        //chart.setDrawCenterText(true);


        //chart.setDrawCenterText(true);
        chart.rotationAngle = 0f
        // enable rotation of the chart by touch
        // enable rotation of the chart by touch
        chart.isRotationEnabled = true
        chart.isHighlightPerTapEnabled = true

        // chart.setUnit(" €");
        // chart.setDrawUnitsInChart(true);

        // add a selection listener

        // chart.setUnit(" €");
        // chart.setDrawUnitsInChart(true);

        // add a selection listener
        chart.setOnChartValueSelectedListener(this)

        //seekBarX.setProgress(4);
        //seekBarY.setProgress(10);


        //seekBarX.setProgress(4);
        //seekBarY.setProgress(10);
        chart.animateY(1400, Easing.EaseInOutQuad)
        // chart.spin(2000, 0, 360);

        // chart.spin(2000, 0, 360);
        val l = chart.legend
        l.verticalAlignment = Legend.LegendVerticalAlignment.TOP
        l.horizontalAlignment = Legend.LegendHorizontalAlignment.RIGHT
        l.orientation = Legend.LegendOrientation.VERTICAL
        l.setDrawInside(false)
        l.xEntrySpace = 7f
        l.yEntrySpace = 0f
        l.yOffset = 0f

        // entry label styling
        chart.setEntryLabelColor(Color.WHITE)
        //chart.setEntryLabelTypeface(tfRegular);
        //chart.setEntryLabelTypeface(tfRegular);
        chart.setEntryLabelTextSize(12f)

        chart.isDrawHoleEnabled = false //remove center area
        chart.setDrawEntryLabels(false)//hide text in chart (label text)
        setData()
    }

    private fun setData() {
        val entries = ArrayList<PieEntry>()

        // NOTE: The order of the entries when being added to the entries array determines their position around the center of
        // the chart.
//        for (i in 0 until count) {
//            //entries.add(PieEntry(25f, "label", null))
//            entries.add(
//                PieEntry(
//                    ((Math.random() * range) + range / 5).toFloat(),
//                    "label",
//                    ContextCompat.getDrawable(this, R.drawable.aboutus)
//                )
//            )
//        }

        livePollDetailResponse?.let {
            for (item in it.poll_result) {
                entries.add(PieEntry(item.percenrage.toFloat(), item.option_name, null))
            }
        }

//        entries.add(PieEntry(45f, "નરેન્દ્ર મોદી", null))
//        entries.add(PieEntry(25f, "રાહુલ ગાંધી", null))
//        entries.add(PieEntry(10f, "મમતા બેનર્જી", null))
//        entries.add(PieEntry(5f, "અખિલેશ યાદવ", null))

        val dataSet = PieDataSet(entries, "")
        dataSet.setDrawIcons(false)
        dataSet.sliceSpace = 3f
        dataSet.iconsOffset = MPPointF(0f, 40f)
        dataSet.selectionShift = 5f

        // add a lot of colors
        val colors = ArrayList<Int>()
        for (c in ColorTemplate.VORDIPLOM_COLORS) colors.add(c)
        for (c in ColorTemplate.JOYFUL_COLORS) colors.add(c)
        for (c in ColorTemplate.COLORFUL_COLORS) colors.add(c)
        for (c in ColorTemplate.LIBERTY_COLORS) colors.add(c)
        for (c in ColorTemplate.PASTEL_COLORS) colors.add(c)
        colors.add(ColorTemplate.getHoloBlue())
        dataSet.colors = colors
        //dataSet.setSelectionShift(0f);
        val data = PieData(dataSet)
        //data.setValueFormatter(PercentFormatter())

//        //showing percentage in chart
//        data.setValueFormatter(PercentFormatter(chart))
        data.setValueFormatter(MyValueFormatter())
//        chart.setUsePercentValues(true)

        data.setValueTextSize(11f)
        data.setValueTextColor(Color.WHITE)
        //data.setValueTypeface(tfLight);
        chart.data = data

        // undo all highlights
        chart.highlightValues(null)
        chart.invalidate()
    }

    override fun onValueSelected(e: Entry?, h: Highlight?) {
        if (e == null) return
        Log.i(
            "VAL SELECTED",
            "Value: " + e.y + ", index: " + h!!.x
                    + ", DataSet index: " + h.dataSetIndex
        )
    }

    override fun onNothingSelected() {
        Log.i("PieChart", "nothing selected")
    }

    private fun handleAnswerResponse(commonResponse: CommonResponse?) {
        if (null != commonResponse) {
            btSubmitLivePOllAnswer.visibility = View.VISIBLE
            pbSubmitLivePOllAnswer.visibility = View.GONE
            showAlertDialog(commonResponse.message)
        } else {
            showSnackBar(getString(R.string.something_went_wrong))
        }
    }

    private fun handleResponse(quizAndContestRunningResponse: LivePollDetailResponse?) {
        pbQuizAndContestRunning.visibility = View.GONE
        cvQuizAndContestRunning.visibility = View.VISIBLE

        if (null != quizAndContestRunningResponse) {
            setupViews(quizAndContestRunningResponse)
        }
    }

    private fun setupViews(quizAndContestRunningResponse: LivePollDetailResponse) {
        livePollDetailResponse = quizAndContestRunningResponse
        setupChart()

        tvLivePollQuestion.text = quizAndContestRunningResponse.poll[0].poll_question

        rbExcellent.text =
            quizAndContestRunningResponse.poll[0].poll_options[0].option_name
        if (quizAndContestRunningResponse.poll[0].poll_options[0].option_id == quizAndContestRunningResponse.poll[0].user_rating
        ) {
            rbExcellent.isChecked = true
        }

        rbGood.text = quizAndContestRunningResponse.poll[0].poll_options[1].option_name
        if (quizAndContestRunningResponse.poll[0].poll_options[1].option_id == quizAndContestRunningResponse.poll[0].user_rating
        ) {
            rbGood.isChecked = true
        }

        rbcantAnswer.text =
            quizAndContestRunningResponse.poll[0].poll_options[2].option_name
        if (quizAndContestRunningResponse.poll[0].poll_options[2].option_id == quizAndContestRunningResponse.poll[0].user_rating
        ) {
            rbcantAnswer.isChecked = true
        }

        rbBad.text = quizAndContestRunningResponse.poll[0].poll_options[3].option_name
        if (quizAndContestRunningResponse.poll[0].poll_options[3].option_id == quizAndContestRunningResponse.poll[0].user_rating
        ) {
            rbBad.isChecked = true
        }

        if (quizAndContestRunningResponse.poll[0].user_rating.isNotEmpty()) {
            //answer already submitted
            btSubmitLivePOllAnswer.visibility = View.INVISIBLE
            tvGive_rate_get_10_point.visibility = View.INVISIBLE
            tvAnswerSubmitted.visibility = View.VISIBLE
        }

        //todo work here
        rbExcellent.setOnCheckedChangeListener { _, b ->
            if (b) {
                answerId = quizAndContestRunningResponse.poll[0].poll_options[0].option_id
            }
        }

        rbGood.setOnCheckedChangeListener { _, b ->
            if (b) {
                answerId = quizAndContestRunningResponse.poll[0].poll_options[1].option_id
            }
        }

        rbcantAnswer.setOnCheckedChangeListener { _, b ->
            if (b) {
                answerId = quizAndContestRunningResponse.poll[0].poll_options[2].option_id
            }
        }

        rbBad.setOnCheckedChangeListener { _, b ->
            if (b) {
                answerId = quizAndContestRunningResponse.poll[0].poll_options[3].option_id
            }
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

//todo scroll whole screen issue
//todo disable radio buttons, convert it to as per the designs