package com.politics.politicalapp.ui.activity

import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import androidx.core.content.ContextCompat
import com.politics.politicalapp.R
import kotlinx.android.synthetic.main.activity_quiz_and_contest_running.*

class QuizAndContestRunningActivity : ExtendedToolbarActivity() {

    override val layoutId: Int
        get() = R.layout.activity_quiz_and_contest_running

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setToolbarTitle(getString(R.string.quiz_and_contest))

        val greenText =
            SpannableString(getString(R.string.participate_and))
        greenText.setSpan(
            ForegroundColorSpan(
                ContextCompat.getColor(
                    this,
                    R.color.black
                )
            ),
            0, greenText.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        tvGive_rate_get_10_point.text = greenText

        val yellowText =
            SpannableString(getString(R.string.give_rate_get_10_point_2))
        yellowText.setSpan(
            ForegroundColorSpan(
                ContextCompat.getColor(
                    this,
                    R.color.red_CC252C
                )
            ),
            0, yellowText.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        tvGive_rate_get_10_point.append(yellowText)

        val thirdText =
            SpannableString(getString(R.string.points_and_win_prizes))
        thirdText.setSpan(
            ForegroundColorSpan(
                ContextCompat.getColor(
                    this,
                    R.color.black
                )
            ),
            0, thirdText.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        tvGive_rate_get_10_point.append(thirdText)

        val forthText =
            SpannableString(getString(R.string.points_and_win_prizes_2))
        forthText.setSpan(
            ForegroundColorSpan(
                ContextCompat.getColor(
                    this,
                    R.color.red_CC252C
                )
            ),
            0, forthText.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        tvGive_rate_get_10_point.append(forthText)
    }
}