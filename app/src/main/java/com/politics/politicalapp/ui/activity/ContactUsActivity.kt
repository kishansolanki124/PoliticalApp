package com.politics.politicalapp.ui.activity

import android.os.Bundle
import com.politics.politicalapp.R

class ContactUsActivity : ExtendedToolbarActivity() {

    override val layoutId: Int
        get() = R.layout.activity_contact_us

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setToolbarTitle(getString(R.string.Contact_Us))
    }
}