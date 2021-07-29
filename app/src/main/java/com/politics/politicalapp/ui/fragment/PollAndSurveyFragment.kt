package com.politics.politicalapp.ui.fragment

import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.politics.politicalapp.R
import com.politics.politicalapp.adapter.PollAndSurveyAdapter
import com.politics.politicalapp.apputils.SPreferenceManager
import com.politics.politicalapp.apputils.isConnected
import com.politics.politicalapp.apputils.setUserPoints
import com.politics.politicalapp.apputils.showSnackBar
import com.politics.politicalapp.pojo.CommonResponse
import com.politics.politicalapp.pojo.DistrictPollListResponse
import com.politics.politicalapp.ui.activity.PollAndSurveyActivity
import com.politics.politicalapp.viewmodel.PollAndSurveyViewModel
import kotlinx.android.synthetic.main.fragment_poll_and_survey.*

class PollAndSurveyFragment : Fragment() {

    private lateinit var govtWorkNewsAdapter: PollAndSurveyAdapter
    private lateinit var settingsViewModel: PollAndSurveyViewModel

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

        settingsViewModel = ViewModelProvider(this).get(PollAndSurveyViewModel::class.java)

        settingsViewModel.getDistrictPoll().observe(requireActivity(), {
            handleResponse(it)
        })

        settingsViewModel.getDistrictPollRating().observe(requireActivity(), {
            handleResponseDistrictPollRating(it)
        })

        btSubmitPollAndSurvey.setOnClickListener {
            if (govtWorkNewsAdapter.getPollList().isNotEmpty() && isAllAnswerSubmitted()) {
                val pollListQuestions = ArrayList<String>()
                val pollListAnswers = ArrayList<String>()

                for (item in govtWorkNewsAdapter.getPollList()) {
                    pollListQuestions.add(item.id)
                    pollListAnswers.add(item.checkedRadioId)
                }

                if (isConnected(requireContext())) {
                    btSubmitPollAndSurvey.visibility = View.INVISIBLE
                    pbSubmitPollAndSurvey.visibility = View.VISIBLE

                    val commaSeparatedStringQuestions =
                        //pollListQuestions.joinToString { "\'${it}\'" }
                        pollListQuestions.joinToString { it }

                    val commaSeparatedStringAnswers =
                        pollListAnswers.joinToString { it }

                    settingsViewModel.addDistrictPollRating(
                        ((activity as PollAndSurveyActivity).getDistrictId()),
                        SPreferenceManager.getInstance(requireContext()).session,
                        commaSeparatedStringQuestions, commaSeparatedStringAnswers
                    )
                } else {
                    showSnackBar(getString(R.string.no_internet), requireActivity())
                }
            }
        }
    }

    private fun isAllAnswerSubmitted(): Boolean {
        for (item in govtWorkNewsAdapter.getPollList()) {
            if (item.checkedRadioId.isEmpty()) {
                showSnackBar(getString(R.string.answer_all_questions), requireActivity())
                return false
            }
        }
        return true
    }

    private fun handleResponseDistrictPollRating(commonResponse: CommonResponse?) {
        pbSubmitPollAndSurvey.visibility = View.GONE
        btSubmitPollAndSurvey.visibility = View.VISIBLE

        if (null != commonResponse) {
            requireContext().setUserPoints(commonResponse.user_points)
            //rating is done
            btSubmitPollAndSurvey.visibility = View.GONE
            tvGive_rate_get_10_point.visibility = View.GONE
            tvRatingDone.visibility = View.VISIBLE
            showAlertDialog(commonResponse.message)
        } else {
            showSnackBar(getString(R.string.something_went_wrong), requireActivity())
        }
    }

    private fun handleResponse(districtPollListResponse: DistrictPollListResponse?) {
        rvPollAndSurvey.visibility = View.VISIBLE
        pbPollAndSurvey.visibility = View.GONE

        if (null != districtPollListResponse) {
            if (districtPollListResponse.rating_status.isNotEmpty()) {
                //already rating is done
                btSubmitPollAndSurvey.visibility = View.GONE
                tvGive_rate_get_10_point.visibility = View.GONE
                tvRatingDone.visibility = View.VISIBLE
            } else {
                btSubmitPollAndSurvey.visibility = View.VISIBLE
                tvGive_rate_get_10_point.visibility = View.VISIBLE
                tvRatingDone.visibility = View.GONE
            }

            if (null != districtPollListResponse.poll_list && districtPollListResponse.poll_list.isNotEmpty()) {
                govtWorkNewsAdapter.reset()
                govtWorkNewsAdapter.setItem(
                    districtPollListResponse.poll_list,
                    districtPollListResponse.poll_option
                )
            } else {
                govtWorkNewsAdapter.reset()
            }
        } else {
            showSnackBar(getString(R.string.something_went_wrong), requireActivity())
        }
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
        govtWorkNewsAdapter = PollAndSurveyAdapter(
            {
                //callIntent(this, it.contact_no!!)
                //startActivity(Intent(this, DharasabhyoDetailActivity::class.java))
            }, {
                //browserIntent(this, it.website!!)
            }
        )
        rvPollAndSurvey.adapter = govtWorkNewsAdapter
    }

    fun refreshPollAndSurvey() {
        if (isConnected(requireContext())) {
            pbPollAndSurvey.visibility = View.VISIBLE
            rvPollAndSurvey.visibility = View.GONE
            settingsViewModel.getGovtWorkList(
                ((activity as PollAndSurveyActivity).getDistrictId()),
                SPreferenceManager.getInstance(requireContext()).session
            )
        } else {
            showSnackBar(getString(R.string.no_internet), requireActivity())
        }
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