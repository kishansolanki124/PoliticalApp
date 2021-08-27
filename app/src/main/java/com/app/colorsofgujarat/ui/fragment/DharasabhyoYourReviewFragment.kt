package com.app.colorsofgujarat.ui.fragment

import android.graphics.Color
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.app.colorsofgujarat.R
import com.app.colorsofgujarat.apputils.*
import com.app.colorsofgujarat.databinding.FragmentDharasabhyoYourReviewBinding
import com.app.colorsofgujarat.pojo.GiveMLARatingResponse
import com.app.colorsofgujarat.pojo.MLADetailResponse
import com.app.colorsofgujarat.ui.activity.DharasabhyoDetailActivity
import com.app.colorsofgujarat.viewmodel.MLAViewModel
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

class DharasabhyoYourReviewFragment : Fragment(), OnChartValueSelectedListener {

    private lateinit var mlaDetailResponse: MLADetailResponse
    private lateinit var mlaViewModel: MLAViewModel
    private var checkedRadioId = ""
    private var _binding: FragmentDharasabhyoYourReviewBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        //return inflater.inflate(R.layout.fragment_dharasabhyo_your_review, container, false)
        _binding = FragmentDharasabhyoYourReviewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mlaDetailResponse = (activity as DharasabhyoDetailActivity).getMLADetailResponse()

        setupViews()

        mlaViewModel = ViewModelProvider(this).get(MLAViewModel::class.java)

        mlaViewModel.mLAVoteResponse().observe(requireActivity(), {
            handleResponse(it)
        })

        val greenText = SpannableString(getString(R.string.give_rate_get_10_point_1))
        greenText.setSpan(
            ForegroundColorSpan(ContextCompat.getColor(requireContext(), R.color.black)),
            0, greenText.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        binding.tvGiveRateGet10Point.text = greenText

        //val yellowText = SpannableString(getString(R.string.give_rate_get_10_point_2))
        val yellowText = SpannableString(requireActivity().getPollPoints())
        yellowText.setSpan(
            ForegroundColorSpan(ContextCompat.getColor(requireContext(), R.color.red_CC252C)),
            0, yellowText.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        binding.tvGiveRateGet10Point.append(yellowText)

        val thirdText = SpannableString(getString(R.string.give_rate_get_10_point_3))
        thirdText.setSpan(
            ForegroundColorSpan(ContextCompat.getColor(requireContext(), R.color.black)),
            0, thirdText.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        binding.tvGiveRateGet10Point.append(thirdText)
    }

    private fun handleResponse(commonResponse: GiveMLARatingResponse?) {
        if (null != commonResponse) {
            binding.pbMLAReviewSubmit.visibility = View.GONE
            binding.btMLAReviewSubmit.visibility = View.VISIBLE
            binding.llGiveRatingToMLA.visibility = View.GONE
            //rgDharasabhyaReview.visibility = View.GONE
            binding.tvAnswerSubmitted.visibility = View.VISIBLE

            binding.rbExcellent.isEnabled = false
            binding.rbGood.isEnabled = false
            binding.rbCantAnswer.isEnabled = false
            binding.rbBad.isEnabled = false
            binding.rbVeryBad.isEnabled = false

            showAlertDialog(commonResponse.message)
            requireContext().setUserPoints(commonResponse.user_points)

            mlaDetailResponse.poll_result = commonResponse.poll_result
            setData()
        } else {
            showSnackBar(getString(R.string.something_went_wrong), requireActivity())
        }
    }

    private fun setupViews() {
        if (mlaDetailResponse.gov_mla_detail[0].user_rating.isNotEmpty()) {
            //rating is already done by this user
            binding.llGiveRatingToMLA.visibility = View.GONE
            //rgDharasabhyaReview.visibility = View.GONE
            binding.tvAnswerSubmitted.visibility = View.VISIBLE

            binding.rbExcellent.isEnabled = false
            binding.rbGood.isEnabled = false
            binding.rbCantAnswer.isEnabled = false
            binding.rbBad.isEnabled = false
            binding.rbVeryBad.isEnabled = false
        }

        if (mlaDetailResponse.poll.isNotEmpty()) {
            binding.tvDharasabhyaReviewQuestion.text = mlaDetailResponse.poll[0].poll_question

            binding.rbExcellent.text = mlaDetailResponse.poll[0].poll_options[0].option_name
            if (mlaDetailResponse.poll[0].poll_options[0].option_id == mlaDetailResponse.gov_mla_detail[0].user_rating) {
                binding.rbExcellent.isChecked = true
            }
            binding.rbGood.text = mlaDetailResponse.poll[0].poll_options[1].option_name
            if (mlaDetailResponse.poll[0].poll_options[1].option_id == mlaDetailResponse.gov_mla_detail[0].user_rating) {
                binding.rbGood.isChecked = true
            }
            binding.rbCantAnswer.text = mlaDetailResponse.poll[0].poll_options[2].option_name
            if (mlaDetailResponse.poll[0].poll_options[2].option_id == mlaDetailResponse.gov_mla_detail[0].user_rating) {
                binding.rbCantAnswer.isChecked = true
            }
            binding.rbBad.text = mlaDetailResponse.poll[0].poll_options[3].option_name
            if (mlaDetailResponse.poll[0].poll_options[3].option_id == mlaDetailResponse.gov_mla_detail[0].user_rating) {
                binding.rbBad.isChecked = true
            }
            binding.rbVeryBad.text = mlaDetailResponse.poll[0].poll_options[4].option_name
            if (mlaDetailResponse.poll[0].poll_options[4].option_id == mlaDetailResponse.gov_mla_detail[0].user_rating) {
                binding.rbVeryBad.isChecked = true
            }
            setupRadioButtons()
        }
    }

    private fun setupRadioButtons() {
        binding.btMLAReviewSubmit.setOnClickListener {
            if (checkedRadioId.isEmpty()) {
                showSnackBar(getString(R.string.invalid_option), requireActivity())
            } else {
                binding.pbMLAReviewSubmit.visibility = View.VISIBLE
                binding.btMLAReviewSubmit.visibility = View.INVISIBLE

                mlaDetailResponse.gov_mla_detail[0].user_rating = mlaDetailResponse.poll[0].poll_id

                mlaViewModel.giveMLARating(
                    mlaDetailResponse.gov_mla_detail[0].id,
                    SPreferenceManager.getInstance(requireContext()).session,
                    mlaDetailResponse.poll[0].poll_id,
                    checkedRadioId
                )
            }
        }

        binding.rbExcellent.setOnClickListener {
            resetRadioButtons()
            binding.rbExcellent.isChecked = !binding.rbExcellent.isChecked
            checkedRadioId = mlaDetailResponse.poll[0].poll_options[0].option_id
        }

        binding.rbGood.setOnClickListener {
            resetRadioButtons()
            binding.rbGood.isChecked = !binding.rbGood.isChecked
            checkedRadioId = mlaDetailResponse.poll[0].poll_options[1].option_id
        }

        binding.rbCantAnswer.setOnClickListener {
            resetRadioButtons()
            binding.rbCantAnswer.isChecked = !binding.rbCantAnswer.isChecked
            checkedRadioId = mlaDetailResponse.poll[0].poll_options[2].option_id
        }

        binding.rbBad.setOnClickListener {
            resetRadioButtons()
            binding.rbBad.isChecked = !binding.rbBad.isChecked
            checkedRadioId = mlaDetailResponse.poll[0].poll_options[3].option_id
        }

        binding.rbVeryBad.setOnClickListener {
            resetRadioButtons()
            binding.rbVeryBad.isChecked = !binding.rbVeryBad.isChecked
            checkedRadioId = mlaDetailResponse.poll[0].poll_options[4].option_id
        }

        setupChart()
    }

    private fun setupChart() {
        binding.chart.description.isEnabled = false
        binding.chart.setExtraOffsets(0f, 10f, 15f, 5f)
        //https://stackoverflow.com/questions/56276481/android-mp-chart-set-space-between-legend-and-axis

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

        for (item in mlaDetailResponse.poll_result) {
            entries.add(PieEntry(item.percentage.toFloat(), item.option_name, null))
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

//        val purple = context?.let { ContextCompat.getColor(it, R.color.purple) }
//        val colorThird = context?.let { ContextCompat.getColor(it, R.color.green_02CC9C) }
//        val colorSecond = context?.let { ContextCompat.getColor(it, R.color.blue_5058AB) }
//        val neon = context?.let { ContextCompat.getColor(it, R.color.neon) }
//        val blue = context?.let { ContextCompat.getColor(it, R.color.blue_5058AB) }
//
//        dataSet.colors = mutableListOf(purple, colorSecond, colorThird, neon, blue)

        requireContext().setChartColors(dataSet)
        //dataSet.colors = colors

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

    private fun resetRadioButtons() {
        binding.rbExcellent.isChecked = false
        binding.rbGood.isChecked = false
        binding.rbCantAnswer.isChecked = false
        binding.rbBad.isChecked = false
        binding.rbVeryBad.isChecked = false
    }

    private fun showAlertDialog(msg: String) {
        val alertDialogBuilder: AlertDialog.Builder = AlertDialog.Builder(requireContext())
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
            .setTextColor(ContextCompat.getColor(requireContext(), R.color.red_CC252C))
    }
}