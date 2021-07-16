package com.politics.politicalapp.ui.activity

import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.bluehomestudio.luckywheel.WheelItem
import com.cooltechworks.views.ScratchImageView
import com.politics.politicalapp.R
import com.politics.politicalapp.apputils.SPreferenceManager
import com.politics.politicalapp.apputils.isConnected
import com.politics.politicalapp.apputils.showSnackBar
import com.politics.politicalapp.pojo.ScratchCardResponse
import com.politics.politicalapp.viewmodel.SettingsViewModel
import kotlinx.android.synthetic.main.activity_daily_scratch_and_win.*

class DailyScratchAndWnActivity : ExtendedToolbarActivity() {

    private lateinit var settingsViewModel: SettingsViewModel

    private var isRevealed = false
    override val layoutId: Int
        get() = R.layout.activity_daily_scratch_and_win

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setToolbarTitle(getString(R.string.daily_scratch_card))

        settingsViewModel = ViewModelProvider(this).get(SettingsViewModel::class.java)

        settingsViewModel.scratchCardResponse().observe(this, {
            handleResponse(it)
        })

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

        fetchScratchCard()
    }

    private fun handleResponse(scratchCardResponse: ScratchCardResponse?) {
        lwv.visibility = View.VISIBLE
        pbScratchCard.visibility = View.GONE
        if (null != scratchCardResponse) {
            setupSpinWheel(scratchCardResponse)
        } else {
            showSnackBar(getString(R.string.something_went_wrong), this)
        }
    }

    private fun setupSpinWheel(scratchCardResponse: ScratchCardResponse) {
        val centerValue =
            ((scratchCardResponse.scratch_card[0].max.toInt() + scratchCardResponse.scratch_card[0].min.toInt()) / 2)
        val centerValue1 =
            ((scratchCardResponse.scratch_card[0].max.toInt() + centerValue) / 2)
        val wheelItems: MutableList<WheelItem> = ArrayList()
        val centerValue2 =
            ((scratchCardResponse.scratch_card[0].min.toInt() + centerValue) / 2)
        wheelItems.add(
            WheelItem(
                Color.LTGRAY,
                BitmapFactory.decodeResource(resources, R.drawable.ic_action_name),
                scratchCardResponse.scratch_card[0].min
            )
        )
        wheelItems.add(
            WheelItem(
                Color.BLUE,
                BitmapFactory.decodeResource(resources, R.drawable.ic_action_name),
                centerValue.toString()
            )
        )
        wheelItems.add(
            WheelItem(
                Color.BLACK,
                BitmapFactory.decodeResource(resources, R.drawable.ic_action_name),
                centerValue1.toString()
            )
        )
        wheelItems.add(
            WheelItem(
                Color.GRAY,
                BitmapFactory.decodeResource(resources, R.drawable.ic_action_name),
                centerValue2.toString()
            )
        )
        wheelItems.add(
            WheelItem(
                Color.RED,
                BitmapFactory.decodeResource(resources, R.drawable.ic_action_name),
                scratchCardResponse.scratch_card[0].max
            )
        )

        lwv.setTarget(3)

        lwv.addWheelItems(wheelItems)
        lwv.setLuckyWheelReachTheTarget {

        }
    }

    private fun fetchScratchCard() {
        if (isConnected(this)) {
            pbScratchCard.visibility = View.VISIBLE
            lwv.visibility = View.GONE
            settingsViewModel.getScratchCard(SPreferenceManager.getInstance(this).session)
        } else {
            showSnackBar(getString(R.string.no_internet), this)
        }
    }

}