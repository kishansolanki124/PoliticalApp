package com.politics.politicalapp.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import app.app.patidarsaurabh.apputils.AppConstants
import com.politics.politicalapp.R
import com.politics.politicalapp.adapter.LivePollAdapter
import com.politics.politicalapp.ui.activity.LivePollRunningActivity
import kotlinx.android.synthetic.main.activity_quiz_and_contest.*
import java.util.*


class LivePollFragment : Fragment() {

    private lateinit var govtWorkNewsAdapter: LivePollAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_live_poll, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //setToolbarTitle(getString(R.string.quiz_and_contest))
        setupList()
    }

    private fun setupList() {
        val stringList: ArrayList<String> = ArrayList()
        stringList.add("S")
        stringList.add("S")
        stringList.add("S")
        stringList.add("S")
        stringList.add("S")

        govtWorkNewsAdapter = LivePollAdapter(
             { item, showButton ->
                 //callIntent(this, it.contact_no!!)
                startActivity(Intent(requireContext(), LivePollRunningActivity::class.java)
                    .putExtra(AppConstants.SHOW_SUBMIT,showButton))

            }, {
                //browserIntent(this, it.website!!)
                //startActivity(Intent(this, QuizAndContestWinnerActivity::class.java))
            }
        )
        govtWorkNewsAdapter.setItem(stringList)
        rvPollAndSurvey.adapter = govtWorkNewsAdapter
    }
}