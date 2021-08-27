package com.app.colorsofgujarat.ui.fragment

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.app.colorsofgujarat.R
import com.app.colorsofgujarat.apputils.*
import com.app.colorsofgujarat.databinding.FragmentPollAndSurveyResultBinding
import com.app.colorsofgujarat.pojo.DistrictPollListResponse
import com.app.colorsofgujarat.ui.activity.PollAndSurveyActivity
import com.app.colorsofgujarat.viewmodel.PollAndSurveyViewModel
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

class PollAndSurveyResultFragment : Fragment(), OnChartValueSelectedListener {

    private lateinit var settingsViewModel: PollAndSurveyViewModel
    private var districtPollListResponse: DistrictPollListResponse? = null
    private var _binding: FragmentPollAndSurveyResultBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        //return inflater.inflate(R.layout.fragment_poll_and_survey_result, container, false)
        _binding = FragmentPollAndSurveyResultBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        settingsViewModel = ViewModelProvider(this).get(PollAndSurveyViewModel::class.java)

        settingsViewModel.getDistrictPoll().observe(requireActivity(), {
            handleResponse(it)
        })
    }

    private fun setupChart() {
        binding.chartPollAndSurveyResult.description.isEnabled = false
        //chartPollAndSurveyResult.setExtraOffsets(5f, 10f, 5f, 5f)
        binding.chartPollAndSurveyResult.setExtraOffsets(0f, 10f, 15f, 5f)

        binding.chartPollAndSurveyResult.dragDecelerationFrictionCoef = 0.95f

        //chartPollAndSurveyResult.setCenterTextTypeface(tfLight);
        //chartPollAndSurveyResult.setCenterText(generateCenterSpannableText());

        //chartPollAndSurveyResult.setDrawHoleEnabled(true);
        //chartPollAndSurveyResult.setHoleColor(Color.WHITE);


        //chartPollAndSurveyResult.setCenterTextTypeface(tfLight);
        //chartPollAndSurveyResult.setCenterText(generateCenterSpannableText());

        //chartPollAndSurveyResult.setDrawHoleEnabled(true);
        //chartPollAndSurveyResult.setHoleColor(Color.WHITE);
        binding.chartPollAndSurveyResult.setTransparentCircleColor(Color.WHITE)
        binding.chartPollAndSurveyResult.setTransparentCircleAlpha(110)

        //chartPollAndSurveyResult.setHoleRadius(58f);

        //chartPollAndSurveyResult.setHoleRadius(58f);
        binding.chartPollAndSurveyResult.transparentCircleRadius = 61f

        //chartPollAndSurveyResult.setDrawCenterText(true);


        //chartPollAndSurveyResult.setDrawCenterText(true);
        binding.chartPollAndSurveyResult.rotationAngle = 0f
        // enable rotation of the chart by touch
        // enable rotation of the chart by touch
        binding.chartPollAndSurveyResult.isRotationEnabled = true
        binding.chartPollAndSurveyResult.isHighlightPerTapEnabled = true

        // binding.chartPollAndSurveyResult.setUnit(" €");
        // binding.chartPollAndSurveyResult.setDrawUnitsInChart(true);

        // add a selection listener

        // binding.chartPollAndSurveyResult.setUnit(" €");
        // binding.chartPollAndSurveyResult.setDrawUnitsInChart(true);

        // add a selection listener
        binding.chartPollAndSurveyResult.setOnChartValueSelectedListener(this)

        //seekBarX.setProgress(4);
        //seekBarY.setProgress(10);


        //seekBarX.setProgress(4);
        //seekBarY.setProgress(10);
        binding.chartPollAndSurveyResult.animateY(1400, Easing.EaseInOutQuad)
        // binding.chartPollAndSurveyResult.spin(2000, 0, 360);

        // binding.chartPollAndSurveyResult.spin(2000, 0, 360);
        val l = binding.chartPollAndSurveyResult.legend
        l.verticalAlignment = Legend.LegendVerticalAlignment.TOP
        l.horizontalAlignment = Legend.LegendHorizontalAlignment.RIGHT
        l.orientation = Legend.LegendOrientation.VERTICAL
        l.setDrawInside(false)
        l.xEntrySpace = 7f
        l.yEntrySpace = 0f
        l.yOffset = 0f

        // entry label styling
        binding.chartPollAndSurveyResult.setEntryLabelColor(Color.WHITE)
        //binding.chartPollAndSurveyResult.setEntryLabelTypeface(tfRegular);
        //binding.chartPollAndSurveyResult.setEntryLabelTypeface(tfRegular);
        binding.chartPollAndSurveyResult.setEntryLabelTextSize(12f)

        binding.chartPollAndSurveyResult.isDrawHoleEnabled = false //remove center area
        binding.chartPollAndSurveyResult.setDrawEntryLabels(false)//hide text in chart (label text)
        setData()
    }

    private fun setData() {
        val entries = ArrayList<PieEntry>()

        districtPollListResponse?.let {
            for (item in it.poll_result) {
                entries.add(PieEntry(item.percentage.toFloat(), item.option_name, null))
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
//        binding.chartPollAndSurveyResult.setUsePercentValues(true)

        data.setValueTextSize(11f)
        data.setValueTextColor(Color.WHITE)
        //data.setValueTypeface(tfLight);
        binding.chartPollAndSurveyResult.data = data

        // undo all highlights
        binding.chartPollAndSurveyResult.highlightValues(null)
        binding.chartPollAndSurveyResult.invalidate()
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
        binding.chartPollAndSurveyResult.visibility = View.VISIBLE
        binding.tvPollResultTitle.visibility = View.VISIBLE
        binding.pbPollAndSurveyResult.visibility = View.GONE

        if (null != districtPollListResponse) {
            this.districtPollListResponse = districtPollListResponse
            binding.tvPollResultTitle.text = districtPollListResponse.poll_result_text
            setupChart()
        } else {
            showSnackBar(getString(R.string.something_went_wrong), requireActivity())
        }
    }

    fun refreshPollAndSurveyResult() {
        if (isConnected(requireContext())) {
            binding.chartPollAndSurveyResult.visibility = View.GONE
            binding.tvPollResultTitle.visibility = View.GONE
            binding.pbPollAndSurveyResult.visibility = View.VISIBLE
            settingsViewModel.getGovtWorkList(
                ((activity as PollAndSurveyActivity).getDistrictId()),
                SPreferenceManager.getInstance(requireContext()).session
            )
        } else {
            showSnackBar(getString(R.string.no_internet), requireActivity())
        }
    }
}