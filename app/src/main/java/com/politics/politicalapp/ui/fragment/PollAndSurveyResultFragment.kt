package com.politics.politicalapp.ui.fragment

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
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
import com.politics.politicalapp.apputils.isConnected
import com.politics.politicalapp.apputils.setChartColors
import com.politics.politicalapp.apputils.showSnackBar
import com.politics.politicalapp.pojo.DistrictPollListResponse
import com.politics.politicalapp.ui.activity.PollAndSurveyActivity
import com.politics.politicalapp.viewmodel.PollAndSurveyViewModel
import kotlinx.android.synthetic.main.fragment_poll_and_survey_result.*

class PollAndSurveyResultFragment : Fragment(), OnChartValueSelectedListener {

    private lateinit var settingsViewModel: PollAndSurveyViewModel
    private var districtPollListResponse: DistrictPollListResponse? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_poll_and_survey_result, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        settingsViewModel = ViewModelProvider(this).get(PollAndSurveyViewModel::class.java)

        settingsViewModel.getDistrictPoll().observe(requireActivity(), {
            handleResponse(it)
        })
    }

    private fun setupChart() {
        chartPollAndSurveyResult.description.isEnabled = false
        //chartPollAndSurveyResult.setExtraOffsets(5f, 10f, 5f, 5f)
        chartPollAndSurveyResult.setExtraOffsets(0f, 10f, 15f, 5f)

        chartPollAndSurveyResult.dragDecelerationFrictionCoef = 0.95f

        //chartPollAndSurveyResult.setCenterTextTypeface(tfLight);
        //chartPollAndSurveyResult.setCenterText(generateCenterSpannableText());

        //chartPollAndSurveyResult.setDrawHoleEnabled(true);
        //chartPollAndSurveyResult.setHoleColor(Color.WHITE);


        //chartPollAndSurveyResult.setCenterTextTypeface(tfLight);
        //chartPollAndSurveyResult.setCenterText(generateCenterSpannableText());

        //chartPollAndSurveyResult.setDrawHoleEnabled(true);
        //chartPollAndSurveyResult.setHoleColor(Color.WHITE);
        chartPollAndSurveyResult.setTransparentCircleColor(Color.WHITE)
        chartPollAndSurveyResult.setTransparentCircleAlpha(110)

        //chartPollAndSurveyResult.setHoleRadius(58f);

        //chartPollAndSurveyResult.setHoleRadius(58f);
        chartPollAndSurveyResult.transparentCircleRadius = 61f

        //chartPollAndSurveyResult.setDrawCenterText(true);


        //chartPollAndSurveyResult.setDrawCenterText(true);
        chartPollAndSurveyResult.rotationAngle = 0f
        // enable rotation of the chart by touch
        // enable rotation of the chart by touch
        chartPollAndSurveyResult.isRotationEnabled = true
        chartPollAndSurveyResult.isHighlightPerTapEnabled = true

        // chartPollAndSurveyResult.setUnit(" €");
        // chartPollAndSurveyResult.setDrawUnitsInChart(true);

        // add a selection listener

        // chartPollAndSurveyResult.setUnit(" €");
        // chartPollAndSurveyResult.setDrawUnitsInChart(true);

        // add a selection listener
        chartPollAndSurveyResult.setOnChartValueSelectedListener(this)

        //seekBarX.setProgress(4);
        //seekBarY.setProgress(10);


        //seekBarX.setProgress(4);
        //seekBarY.setProgress(10);
        chartPollAndSurveyResult.animateY(1400, Easing.EaseInOutQuad)
        // chartPollAndSurveyResult.spin(2000, 0, 360);

        // chartPollAndSurveyResult.spin(2000, 0, 360);
        val l = chartPollAndSurveyResult.legend
        l.verticalAlignment = Legend.LegendVerticalAlignment.TOP
        l.horizontalAlignment = Legend.LegendHorizontalAlignment.RIGHT
        l.orientation = Legend.LegendOrientation.VERTICAL
        l.setDrawInside(false)
        l.xEntrySpace = 7f
        l.yEntrySpace = 0f
        l.yOffset = 0f

        // entry label styling
        chartPollAndSurveyResult.setEntryLabelColor(Color.WHITE)
        //chartPollAndSurveyResult.setEntryLabelTypeface(tfRegular);
        //chartPollAndSurveyResult.setEntryLabelTypeface(tfRegular);
        chartPollAndSurveyResult.setEntryLabelTextSize(12f)

        chartPollAndSurveyResult.isDrawHoleEnabled = false //remove center area
        chartPollAndSurveyResult.setDrawEntryLabels(false)//hide text in chart (label text)
        setData()
    }

    private fun setData() {
        val entries = ArrayList<PieEntry>()

        // NOTE: The order of the entries when being added to the entries array determines their position around the center of
        // the chartPollAndSurveyResult.
//        for (i in 0 until count) {
//            //entries.add(PieEntry(25f, "label", null))
//            entries.add(
//                PieEntry(
//                    ((Math.random() * range) + range / 5).toFloat(),
//                    "label",
//                    ContextCompat.getDrawable(requireContext(), R.drawable.aboutus)
//                )
//            )
//        }

        districtPollListResponse?.let {
            for (item in it.poll_result) {
                entries.add(PieEntry(item.percenrage.toFloat(), item.option_name, null))
            }
        }

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
        requireContext().setChartColors(dataSet)
        //dataSet.setSelectionShift(0f);
        val data = PieData(dataSet)
        //data.setValueFormatter(PercentFormatter())

//        //showing percentage in chart
//        data.setValueFormatter(PercentFormatter(chart))
        data.setValueFormatter(MyValueFormatter())
//        chartPollAndSurveyResult.setUsePercentValues(true)

        data.setValueTextSize(11f)
        data.setValueTextColor(Color.WHITE)
        //data.setValueTypeface(tfLight);
        chartPollAndSurveyResult.data = data

        // undo all highlights
        chartPollAndSurveyResult.highlightValues(null)
        chartPollAndSurveyResult.invalidate()
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

    private fun handleResponse(districtPollListResponse: DistrictPollListResponse?) {
        chartPollAndSurveyResult.visibility = View.VISIBLE
        pbPollAndSurveyResult.visibility = View.GONE

        if (null != districtPollListResponse) {
            this.districtPollListResponse = districtPollListResponse
            setupChart()
        } else {
            showSnackBar(getString(R.string.something_went_wrong), requireActivity())
        }
    }

    fun refreshPollAndSurveyResult() {
        if (isConnected(requireContext())) {
            chartPollAndSurveyResult.visibility = View.GONE
            pbPollAndSurveyResult.visibility = View.VISIBLE
            settingsViewModel.getGovtWorkList(((activity as PollAndSurveyActivity).getDistrictId()))
        } else {
            showSnackBar(getString(R.string.no_internet), requireActivity())
        }
    }
}
//todo scroll whole screen issue