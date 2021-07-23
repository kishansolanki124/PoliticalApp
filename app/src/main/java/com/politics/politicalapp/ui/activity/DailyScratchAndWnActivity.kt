package com.politics.politicalapp.ui.activity

import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.bluehomestudio.luckywheel.WheelItem
import com.cooltechworks.views.ScratchImageView
import com.politics.politicalapp.R
import com.politics.politicalapp.apputils.isConnected
import com.politics.politicalapp.apputils.showSnackBar
import com.politics.politicalapp.pojo.ScratchCardResponse
import com.politics.politicalapp.viewmodel.SettingsViewModel
import kotlinx.android.synthetic.main.activity_daily_scratch_and_win.*
import java.util.*
import kotlin.collections.ArrayList

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

        settingsViewModel.addScratchCardResponse().observe(this, {
            if (null != it) {
                showAlertDialog(it.message)
            } else {
                showSnackBar(getString(R.string.something_went_wrong))
            }
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
        if (null != scratchCardResponse && scratchCardResponse.status == "1") {
            setupSpinWheel(scratchCardResponse)
        } else {
            showSnackBar(getString(R.string.something_went_wrong))
        }
    }

    private fun setupSpinWheel(scratchCardResponse: ScratchCardResponse) {

        val list = getRandomNonRepeatingIntegers(12, 1, 15)

//        val centerValue =
//            ((scratchCardResponse.scratch_card[0].max.toInt() + scratchCardResponse.scratch_card[0].min.toInt()) / 2)
//        val centerValue1 =
//            ((scratchCardResponse.scratch_card[0].max.toInt() + centerValue) / 2)
        val wheelItems: MutableList<WheelItem> = ArrayList()
//        val centerValue2 =
//            ((scratchCardResponse.scratch_card[0].min.toInt() + centerValue) / 2)
//        val centerValue3 =
//            ((scratchCardResponse.scratch_card[0].min.toInt() + centerValue2) / 2)
//        val centerValue4 =
//            ((scratchCardResponse.scratch_card[0].max.toInt() + centerValue1) / 2)

        wheelItems.add(
            WheelItem(
                ContextCompat.getColor(this, R.color.red_light),
                BitmapFactory.decodeResource(resources, R.drawable.ic_action_name),
                scratchCardResponse.scratch_card[0].min
            )
        )


        wheelItems.add(
            WheelItem(
                ContextCompat.getColor(this, R.color.red_light_2),
                BitmapFactory.decodeResource(resources, R.drawable.ic_action_name),
                list!![0].toString()
            )
        )
        wheelItems.add(
            WheelItem(
                ContextCompat.getColor(this, R.color.red_light_3),
                BitmapFactory.decodeResource(resources, R.drawable.ic_action_name),
                list[0].toString()
            )
        )
        wheelItems.add(
            WheelItem(
                ContextCompat.getColor(this, R.color.red_light),
                BitmapFactory.decodeResource(resources, R.drawable.ic_action_name),
                list[1].toString()
            )
        )
        wheelItems.add(
            WheelItem(
                ContextCompat.getColor(this, R.color.red_light_2),
                BitmapFactory.decodeResource(resources, R.drawable.ic_action_name),
                scratchCardResponse.scratch_card[0].max
            )
        )
        wheelItems.add(
            WheelItem(
                ContextCompat.getColor(this, R.color.red_light_3),
                BitmapFactory.decodeResource(resources, R.drawable.ic_action_name),
                list[2].toString()
            )
        )

        wheelItems.add(
            WheelItem(
                ContextCompat.getColor(this, R.color.red_light),
                BitmapFactory.decodeResource(resources, R.drawable.ic_action_name),
                list[3].toString()
            )
        )

        wheelItems.add(
            WheelItem(
                ContextCompat.getColor(this, R.color.red_light_2),
                BitmapFactory.decodeResource(resources, R.drawable.ic_action_name),
                list[4].toString()
            )
        )

        wheelItems.add(
            WheelItem(
                ContextCompat.getColor(this, R.color.red_light_3),
                BitmapFactory.decodeResource(resources, R.drawable.ic_action_name),
                list[5].toString()
            )
        )

        wheelItems.add(
            WheelItem(
                ContextCompat.getColor(this, R.color.red_light),
                BitmapFactory.decodeResource(resources, R.drawable.ic_action_name),
                list[6].toString()
            )
        )

        wheelItems.add(
            WheelItem(
                ContextCompat.getColor(this, R.color.red_light_2),
                BitmapFactory.decodeResource(resources, R.drawable.ic_action_name),
                list[7].toString()
            )
        )

        wheelItems.add(
            WheelItem(
                ContextCompat.getColor(this, R.color.red_light_3),
                BitmapFactory.decodeResource(resources, R.drawable.ic_action_name),
                list[8].toString()
            )
        )

        lwv.setTarget(randomNumber())

        lwv.addWheelItems(wheelItems)
        lwv.setLuckyWheelReachTheTarget {
            if (isConnected(this)) {
                //todo work here
//                settingsViewModel.addScratchCard(
//                    SPreferenceManager.getInstance(this).session,
//                    "10"
//                )
            } else {
                showSnackBar(getString(R.string.no_internet), this)
            }
        }
    }

    private fun fetchScratchCard() {
        if (isConnected(this)) {
            pbScratchCard.visibility = View.VISIBLE
            lwv.visibility = View.GONE
            //settingsViewModel.getScratchCard(SPreferenceManager.getInstance(this).session)
            settingsViewModel.getScratchCard("9825086150")
        } else {
            showSnackBar(getString(R.string.no_internet), this)
        }
    }

    private fun showAlertDialog(msg: String) {
        val alertDialogBuilder: AlertDialog.Builder = AlertDialog.Builder(this)
        alertDialogBuilder.setMessage(msg)
        alertDialogBuilder.setCancelable(true)

        alertDialogBuilder.setPositiveButton(
            getString(android.R.string.ok)
        ) { dialog, _ ->
            dialog.cancel()
        }

        val alertDialog: AlertDialog = alertDialogBuilder.create()
        alertDialog.show()
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE)
            .setTextColor(ContextCompat.getColor(this, R.color.red_CC252C))
    }

    private fun randomNumber(): Int {
        val rand = Random()
        return rand.nextInt(4 - 0 + 1) + 0
    }

    private fun getRandomNonRepeatingIntegers(
        size: Int, min: Int, max: Int
    ): ArrayList<Int>? {
        val numbers = ArrayList<Int>()
        while (numbers.size < size) {
            val random: Int = getRandomInt(min, max)
            if (!numbers.contains(random)) {
                numbers.add(random)
            }
        }
        return numbers
    }

    fun getRandomInt(min: Int, max: Int): Int {
        val random = Random()
        return random.nextInt(max - min + 1) + min
    }

}