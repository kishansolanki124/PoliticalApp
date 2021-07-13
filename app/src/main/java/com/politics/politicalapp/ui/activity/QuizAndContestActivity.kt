package com.politics.politicalapp.ui.activity

import android.content.Intent
import android.os.Bundle
import com.politics.politicalapp.R
import com.politics.politicalapp.adapter.QuizAndContestAdapter
import kotlinx.android.synthetic.main.activity_quiz_and_contest.*

class QuizAndContestActivity : ExtendedToolbarActivity() {

    private lateinit var govtWorkNewsAdapter: QuizAndContestAdapter

    override val layoutId: Int
        get() = R.layout.activity_quiz_and_contest

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setToolbarTitle(getString(R.string.quiz_and_contest))
        setupList()
    }

    private fun setupList() {
        val stringList: ArrayList<String> = ArrayList()
        stringList.add("S")
        stringList.add("S")
        stringList.add("S")
        stringList.add("S")
        stringList.add("S")

        govtWorkNewsAdapter = QuizAndContestAdapter(
            {
                //callIntent(this, it.contact_no!!)
                startActivity(Intent(this, QuizAndContestRunningActivity::class.java))

            }, {
                //browserIntent(this, it.website!!)
                startActivity(Intent(this, QuizAndContestWinnerActivity::class.java))
            }
        )
        govtWorkNewsAdapter.setItem(stringList)
        rvPollAndSurvey.adapter = govtWorkNewsAdapter
    }
}