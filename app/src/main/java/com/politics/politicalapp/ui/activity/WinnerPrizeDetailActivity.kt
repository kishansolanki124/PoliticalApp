package com.politics.politicalapp.ui.activity

import android.os.Bundle
import com.politics.politicalapp.R

class WinnerPrizeDetailActivity : ExtendedToolbarActivity() {

    override val layoutId: Int
        get() = R.layout.activity_winner_prize_detail

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setToolbarTitle(getString(R.string.winner))
    }
}