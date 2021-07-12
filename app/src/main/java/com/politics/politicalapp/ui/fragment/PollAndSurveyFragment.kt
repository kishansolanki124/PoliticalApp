package com.politics.politicalapp.ui.fragment

import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.politics.politicalapp.R
import com.politics.politicalapp.adapter.PollAndSurveyAdapter
import kotlinx.android.synthetic.main.fragment_poll_and_survey.*

class PollAndSurveyFragment : Fragment() {

    private lateinit var govtWorkNewsAdapter: PollAndSurveyAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_poll_and_survey, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupPointsTextView()
        setupList()
    }

    private fun setupPointsTextView() {
        val greenText = SpannableString(getString(R.string.give_rate_get_10_point_1))
        greenText.setSpan(
            ForegroundColorSpan(ContextCompat.getColor(requireContext(), R.color.black)),
            0, greenText.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        tvGive_rate_get_10_point.text = greenText

        val yellowText = SpannableString(getString(R.string.give_rate_get_10_point_2))
        yellowText.setSpan(
            ForegroundColorSpan(ContextCompat.getColor(requireContext(), R.color.red_CC252C)),
            0, yellowText.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        tvGive_rate_get_10_point.append(yellowText)

        val thirdText = SpannableString(getString(R.string.give_rate_get_10_point_3))
        thirdText.setSpan(
            ForegroundColorSpan(ContextCompat.getColor(requireContext(), R.color.black)),
            0, thirdText.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        tvGive_rate_get_10_point.append(thirdText)
    }

    private fun setupList() {
        val stringList: ArrayList<String> = ArrayList()
        stringList.add("S")
        stringList.add("S")
        stringList.add("S")
        stringList.add("S")
        stringList.add("S")

        govtWorkNewsAdapter = PollAndSurveyAdapter(
            {
                //callIntent(this, it.contact_no!!)
                //startActivity(Intent(this, DharasabhyoDetailActivity::class.java))
            }, {
                //browserIntent(this, it.website!!)
            }
        )
        govtWorkNewsAdapter.setItem(stringList)
        rvPollAndSurvey.adapter = govtWorkNewsAdapter
    }
}