package com.app.colorsofgujarat.ui.activity

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import app.app.patidarsaurabh.apputils.AppConstants
import com.app.colorsofgujarat.R
import com.app.colorsofgujarat.apputils.*
import com.app.colorsofgujarat.databinding.ActivityLivePollRunningBinding
import com.app.colorsofgujarat.pojo.GiveMLARatingResponse
import com.app.colorsofgujarat.pojo.LivePollDetailResponse
import com.app.colorsofgujarat.pojo.PopupBannerResponse
import com.app.colorsofgujarat.viewmodel.LivePollViewModel
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
import java.util.*

class LivePollRunningActivity : AppCompatActivity(), OnChartValueSelectedListener {

    private var qid = ""
    private var answerId = ""
    private lateinit var settingsViewModel: LivePollViewModel
    private var livePollDetailResponse: LivePollDetailResponse? = null
    private var handler: Handler? = null
    private var runnableCode: Runnable? = null
    private lateinit var binding: ActivityLivePollRunningBinding

    private val showButton: Boolean by lazy {
        // runs on first access of messageView
        intent.getBooleanExtra(AppConstants.SHOW_SUBMIT, false)
    }

//    override val layoutId: Int
//        get() = R.layout.activity_live_poll_running

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLivePollRunningBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.commonToolbar.tvTitle.visibility = View.VISIBLE
        binding.commonToolbar.tvTitle.text = getString(R.string.live_poll)
        title = ""
        binding.commonToolbar.ibBack.setOnClickListener {
            hideKeyboard(this)
            onBackPressed()
        }

        qid = intent.getStringExtra(AppConstants.ID)!!

        if (showButton) {
            binding.btSubmitLivePOllAnswer.visibility = View.VISIBLE
            binding.tvGiveRateGet10Point.visibility = View.VISIBLE
        } else {
            binding.btSubmitLivePOllAnswer.visibility = View.INVISIBLE
            binding.tvGiveRateGet10Point.visibility = View.INVISIBLE
        }

        //setToolbarTitle(getString(R.string.live_poll))
        setupPointText()

        binding.btSubmitLivePOllAnswer.setOnClickListener {
            val index: Int =
                binding.rgLivePollRunning.indexOfChild(findViewById(binding.rgLivePollRunning.checkedRadioButtonId))

            if (index != -1) {
                if (isConnected(this)) {
                    binding.btSubmitLivePOllAnswer.visibility = View.INVISIBLE
                    binding.pbSubmitLivePOllAnswer.visibility = View.VISIBLE
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
            binding.cvQuizAndContestRunning.visibility = View.GONE
            binding.pbQuizAndContestRunning.visibility = View.VISIBLE
            settingsViewModel.getLivePollDetail(
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

    private fun setupPointText() {
        val greenText = SpannableString(getString(R.string.participate_and))
        greenText.setSpan(
            ForegroundColorSpan(ContextCompat.getColor(this, R.color.black)),
            0, greenText.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        binding.tvGiveRateGet10Point.text = greenText

//        val yellowText = SpannableString(getString(R.string.give_rate_get_10_point_2))
        val yellowText = SpannableString(getPollPoints())

        yellowText.setSpan(
            ForegroundColorSpan(ContextCompat.getColor(this, R.color.red_CC252C)),
            0, yellowText.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        binding.tvGiveRateGet10Point.append(yellowText)

        val thirdText = SpannableString(getString(R.string.give_rate_get_10_point_3))
        thirdText.setSpan(
            ForegroundColorSpan(ContextCompat.getColor(this, R.color.black)),
            0, thirdText.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        binding.tvGiveRateGet10Point.append(thirdText)
    }

    private fun setupChart() {
        binding.chart.description.isEnabled = false
        binding.chart.setExtraOffsets(0f, 10f, 15f, 5f)

        binding.chart.dragDecelerationFrictionCoef = 0.95f

        //chart.setCenterTextTypeface(tfLight);
        //chart.setCenterText(generateCenterSpannableText());

        //chart.setDrawHoleEnabled(true);
        //chart.setHoleColor(Color.WHITE);


        //chart.setCenterTextTypeface(tfLight);
        //chart.setCenterText(generateCenterSpannableText());

        //chart.setDrawHoleEnabled(true);
        //chart.setHoleColor(Color.WHITE);
        binding.chart.setTransparentCircleColor(Color.WHITE)
        binding.chart.setTransparentCircleAlpha(110)

        //chart.setHoleRadius(58f);

        //chart.setHoleRadius(58f);
        binding.chart.transparentCircleRadius = 61f

        //chart.setDrawCenterText(true);


        //chart.setDrawCenterText(true);
        binding.chart.rotationAngle = 0f
        // enable rotation of the chart by touch
        // enable rotation of the chart by touch
        binding.chart.isRotationEnabled = true
        binding.chart.isHighlightPerTapEnabled = true

        // chart.setUnit(" €");
        // chart.setDrawUnitsInChart(true);

        // add a selection listener

        // chart.setUnit(" €");
        // chart.setDrawUnitsInChart(true);

        // add a selection listener
        binding.chart.setOnChartValueSelectedListener(this)

        //seekBarX.setProgress(4);
        //seekBarY.setProgress(10);


        //seekBarX.setProgress(4);
        //seekBarY.setProgress(10);
        binding.chart.animateY(1400, Easing.EaseInOutQuad)
        // chart.spin(2000, 0, 360);

        // chart.spin(2000, 0, 360);
        val l = binding.chart.legend
        l.verticalAlignment = Legend.LegendVerticalAlignment.TOP
        l.horizontalAlignment = Legend.LegendHorizontalAlignment.RIGHT
        l.orientation = Legend.LegendOrientation.VERTICAL
        l.setDrawInside(false)
        l.xEntrySpace = 7f
        l.yEntrySpace = 0f
        l.yOffset = 0f

        // entry label styling
        binding.chart.setEntryLabelColor(Color.WHITE)
        //chart.setEntryLabelTypeface(tfRegular);
        //chart.setEntryLabelTypeface(tfRegular);
        binding.chart.setEntryLabelTextSize(12f)

        binding.chart.isDrawHoleEnabled = false //remove center area
        binding.chart.setDrawEntryLabels(false)//hide text in chart (label text)
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
                entries.add(PieEntry(item.percentage.toFloat(), item.option_name, null))
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
        //dataSet.colors = colors

        setChartColors(dataSet)
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
        binding.chart.data = data

        // undo all highlights
        binding.chart.highlightValues(null)
        binding.chart.invalidate()
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

    private fun handleAnswerResponse(commonResponse: GiveMLARatingResponse) {
        if (commonResponse.status == "1") {
            binding.btSubmitLivePOllAnswer.visibility = View.VISIBLE
            binding.pbSubmitLivePOllAnswer.visibility = View.GONE
            binding.btSubmitLivePOllAnswer.visibility = View.INVISIBLE
            binding.tvGiveRateGet10Point.visibility = View.INVISIBLE
            binding.tvAnswerSubmitted.visibility = View.VISIBLE
            setUserPoints(commonResponse.user_points)
            showAlertDialog(commonResponse.message)

            livePollDetailResponse?.poll_result = commonResponse.poll_result
            setData()
        } else {
            showAlertDialog(commonResponse.message)
        }
    }

    private fun handleResponse(quizAndContestRunningResponse: LivePollDetailResponse?) {
        binding.pbQuizAndContestRunning.visibility = View.GONE
        binding.cvQuizAndContestRunning.visibility = View.VISIBLE

        if (null != quizAndContestRunningResponse) {
            setupViews(quizAndContestRunningResponse)
        }
    }

    private fun setupViews(quizAndContestRunningResponse: LivePollDetailResponse) {
        livePollDetailResponse = quizAndContestRunningResponse
        setupChart()

        binding.tvLivePollQuestion.text = quizAndContestRunningResponse.poll[0].poll_question

//        rbExcellentLivePollRunning.text =
//            quizAndContestRunningResponse.poll[0].poll_options[0].option_name
//        if (quizAndContestRunningResponse.poll[0].poll_options[0].option_id == quizAndContestRunningResponse.poll[0].user_rating
//        ) {
//            rbExcellentLivePollRunning.isChecked = true
//        }
//
//        rbGood.text = quizAndContestRunningResponse.poll[0].poll_options[1].option_name
//        if (quizAndContestRunningResponse.poll[0].poll_options[1].option_id == quizAndContestRunningResponse.poll[0].user_rating
//        ) {
//            rbGood.isChecked = true
//        }
//
//        rbcantAnswer.text =
//            quizAndContestRunningResponse.poll[0].poll_options[2].option_name
//        if (quizAndContestRunningResponse.poll[0].poll_options[2].option_id == quizAndContestRunningResponse.poll[0].user_rating
//        ) {
//            rbcantAnswer.isChecked = true
//        }


        for (pollOption in quizAndContestRunningResponse.poll[0].poll_options) {
            val radioButton = RadioButton(this)
            radioButton.text = pollOption.option_name
            radioButton.id = View.generateViewId()

            val padding = resources.getDimensionPixelOffset(R.dimen.dp_5)
            radioButton.setPadding(padding, padding, padding, padding)
            radioButton.setTextColor(ContextCompat.getColor(this, R.color.black))

            val myColorStateList = ColorStateList(
                arrayOf(
                    intArrayOf(
                        ContextCompat.getColor(this, R.color.black)
                    )
                ), intArrayOf(ContextCompat.getColor(this, R.color.black))
            )

            radioButton.buttonTintList = myColorStateList
            radioButton.setTextSize(
                TypedValue.COMPLEX_UNIT_PX,
                resources.getDimension(R.dimen.sp_12)
            )
            val typeface = ResourcesCompat.getFont(this, R.font.regular)
            radioButton.typeface = typeface

            val rprms = RadioGroup.LayoutParams(
                RadioGroup.LayoutParams.WRAP_CONTENT,
                RadioGroup.LayoutParams.WRAP_CONTENT
            )
            binding.rgLivePollRunning.addView(radioButton, rprms)

            radioButton.setOnCheckedChangeListener { _, b ->
                if (b) {
                    answerId = pollOption.option_id
                }
            }

            if (quizAndContestRunningResponse.poll[0].user_rating.isNotEmpty()) {
                radioButton.isEnabled = false
                if (pollOption.option_id == quizAndContestRunningResponse.poll[0].user_rating) {
                    radioButton.isChecked = true
                }
            } else {
                radioButton.isEnabled = true
            }
        }

        if (quizAndContestRunningResponse.poll[0].user_rating.isNotEmpty()) {
            //answer already submitted
            binding.btSubmitLivePOllAnswer.visibility = View.INVISIBLE
            binding.tvGiveRateGet10Point.visibility = View.INVISIBLE
            binding.tvAnswerSubmitted.visibility = View.VISIBLE
        }

        binding.ivLivePollShare.setOnClickListener {
            shareIntent(
                "Participate in Live Poll and Win Prizes:\n\n" +
                        quizAndContestRunningResponse.poll[0].poll_question,
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

    private fun setupRepeatableBannerAd(
        delayTime: String,
        initialTime: String,
        popupBanner: List<PopupBannerResponse.PopupBanner>
    ) {
        handler = Handler(Looper.getMainLooper())
        runnableCode = object : Runnable {
            override fun run() {
                if (!isDestroyed && !this@LivePollRunningActivity.isFinishing) {
                    if (this@LivePollRunningActivity.lifecycle.currentState
                            .isAtLeast(Lifecycle.State.RESUMED)
                    ) {
                        showProgressDialog(this@LivePollRunningActivity, popupBanner)
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