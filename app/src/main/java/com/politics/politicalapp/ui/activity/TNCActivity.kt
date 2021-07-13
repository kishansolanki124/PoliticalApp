package com.politics.politicalapp.ui.activity

import android.os.Bundle
import com.politics.politicalapp.R

class TNCActivity : ExtendedToolbarActivity() {

    override val layoutId: Int
        get() = R.layout.activity_tnc

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setToolbarTitle(getString(R.string.Terms_Of_Use))
    }
}