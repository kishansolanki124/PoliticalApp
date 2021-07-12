package com.politics.politicalapp.ui.activity

import android.os.Bundle
import com.politics.politicalapp.R
import com.politics.politicalapp.adapter.QuizAndContestAdapter
import kotlinx.android.synthetic.main.activity_quiz_and_contest.*

class QuizAndContestRunningActivity : ExtendedToolbarActivity() {

    override val layoutId: Int
        get() = R.layout.activity_quiz_and_contest_running

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setToolbarTitle(getString(R.string.quiz_and_contest))
    }
}