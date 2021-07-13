package com.politics.politicalapp.ui.activity

import android.os.Bundle
import com.politics.politicalapp.R

class QuizAndContestWinnerActivity : ExtendedToolbarActivity() {

    override val layoutId: Int
        get() = R.layout.activity_quiz_and_contest_winner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setToolbarTitle(getString(R.string.quiz_and_contest))
    }
}