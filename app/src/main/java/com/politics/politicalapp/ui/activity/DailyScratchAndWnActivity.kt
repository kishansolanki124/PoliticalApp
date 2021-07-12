package com.politics.politicalapp.ui.activity

import android.os.Bundle
import android.widget.Toast
import com.cooltechworks.views.ScratchImageView
import com.politics.politicalapp.R
import kotlinx.android.synthetic.main.activity_daily_scratch_and_win.*

class DailyScratchAndWnActivity : ExtendedToolbarActivity() {

    private var isRevealed = false
    override val layoutId: Int
        get() = R.layout.activity_daily_scratch_and_win

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setToolbarTitle(getString(R.string.daily_scratch_card))

        idScratchCardIv.setRevealListener(object : ScratchImageView.IRevealListener {
            override fun onRevealed(iv: ScratchImageView) {
                // this method is called after revealing the image.
                Toast.makeText(
                    this@DailyScratchAndWnActivity,
                    "Congratulations",
                    Toast.LENGTH_SHORT
                ).show()
            }

            override fun onRevealPercentChangedListener(siv: ScratchImageView, percent: Float) {
                // we can check how much percentage of
                // image is revealed using percent variable
                println("progress:" + percent * 100)
                if (percent * 100 > 70 && !isRevealed) {
                    idScratchCardIv.reveal()
                    isRevealed = true
                }
            }
        })
    }
}