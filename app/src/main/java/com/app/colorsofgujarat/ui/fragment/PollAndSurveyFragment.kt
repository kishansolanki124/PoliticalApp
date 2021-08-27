package com.app.colorsofgujarat.ui.fragment

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
import com.app.colorsofgujarat.R
import com.app.colorsofgujarat.adapter.PollAndSurveyAdapter
import com.app.colorsofgujarat.apputils.*
import com.app.colorsofgujarat.databinding.FragmentPollAndSurveyBinding
import com.app.colorsofgujarat.pojo.CommonResponse
import com.app.colorsofgujarat.pojo.DistrictPollListResponse
import com.app.colorsofgujarat.ui.activity.PollAndSurveyActivity
import com.app.colorsofgujarat.viewmodel.PollAndSurveyViewModel

class PollAndSurveyFragment : Fragment() {

    private lateinit var govtWorkNewsAdapter: PollAndSurveyAdapter
    private lateinit var settingsViewModel: PollAndSurveyViewModel
    private var _binding: FragmentPollAndSurveyBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        //return inflater.inflate(R.layout.fragment_poll_and_survey, container, false)
        _binding = FragmentPollAndSurveyBinding.inflate(inflater, container, false)
        return binding.root
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

        binding.btSubmitPollAndSurvey.setOnClickListener {
            if (govtWorkNewsAdapter.getPollList().isNotEmpty() && isAllAnswerSubmitted()) {
                val pollListQuestions = ArrayList<String>()
                val pollListAnswers = ArrayList<String>()

                for (item in govtWorkNewsAdapter.getPollList()) {
                    pollListQuestions.add(item.id)
                    pollListAnswers.add(item.checkedRadioId)
                }

                if (isConnected(requireContext())) {
                    binding.btSubmitPollAndSurvey.visibility = View.INVISIBLE
                    binding.pbSubmitPollAndSurvey.visibility = View.VISIBLE

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
        binding.pbSubmitPollAndSurvey.visibility = View.GONE
        binding.btSubmitPollAndSurvey.visibility = View.VISIBLE

        if (null != commonResponse) {
            requireContext().setUserPoints(commonResponse.user_points)
            //rating is done
            binding.btSubmitPollAndSurvey.visibility = View.GONE
            binding.tvGiveRateGet10Point.visibility = View.GONE
            binding.tvRatingDone.visibility = View.VISIBLE
            showAlertDialog(commonResponse.message)
        } else {
            showSnackBar(getString(R.string.something_went_wrong), requireActivity())
        }
    }

    private fun handleResponse(districtPollListResponse: DistrictPollListResponse?) {
        binding.rvPollAndSurvey.visibility = View.VISIBLE
        binding.pbPollAndSurvey.visibility = View.GONE

        if (null != districtPollListResponse) {
            if (districtPollListResponse.rating_status.isNotEmpty()) {
                //already rating is done
                binding.btSubmitPollAndSurvey.visibility = View.GONE
                binding.tvGiveRateGet10Point.visibility = View.GONE
                binding.tvRatingDone.visibility = View.VISIBLE
            } else {
                binding.btSubmitPollAndSurvey.visibility = View.VISIBLE
                binding.tvGiveRateGet10Point.visibility = View.VISIBLE
                binding.tvRatingDone.visibility = View.GONE
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

    private fun setupList() {
        govtWorkNewsAdapter = PollAndSurveyAdapter(
            {
                //callIntent(this, it.contact_no!!)
                //startActivity(Intent(this, DharasabhyoDetailActivity::class.java))
            }, {
                //browserIntent(this, it.website!!)
            }
        )
        binding.rvPollAndSurvey.adapter = govtWorkNewsAdapter
    }

    fun refreshPollAndSurvey() {
        if (isConnected(requireContext())) {
            binding.pbPollAndSurvey.visibility = View.VISIBLE
            binding.rvPollAndSurvey.visibility = View.GONE
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