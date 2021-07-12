package com.politics.politicalapp.ui.activity

import android.os.Bundle
import com.politics.politicalapp.R

class QuestionSuggestionDetailActivity : ExtendedToolbarActivity() {

    override val layoutId: Int
        get() = R.layout.activity_question_suggestion_detail

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setToolbarTitle(getString(R.string.que_suggestion))
    }
}