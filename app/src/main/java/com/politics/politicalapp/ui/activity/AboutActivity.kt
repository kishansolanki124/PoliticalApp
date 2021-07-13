package com.politics.politicalapp.ui.activity

import android.os.Bundle
import com.politics.politicalapp.R

class AboutActivity : ExtendedToolbarActivity() {

    override val layoutId: Int
        get() = R.layout.activity_about

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setToolbarTitle(getString(R.string.About_Us))
    }
}