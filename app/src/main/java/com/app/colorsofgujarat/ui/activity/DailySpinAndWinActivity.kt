package com.app.colorsofgujarat.ui.activity

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import com.app.colorsofgujarat.R
import com.app.colorsofgujarat.apputils.*
import com.app.colorsofgujarat.pojo.PopupBannerResponse
import com.app.colorsofgujarat.pojo.ScratchCardResponse
import com.app.colorsofgujarat.viewmodel.SettingsViewModel
import com.bluehomestudio.luckywheel.WheelItem
import kotlinx.android.synthetic.main.activity_daily_spin_and_win.*
import nl.dionsegijn.konfetti.models.Shape
import nl.dionsegijn.konfetti.models.Size
import java.util.*
import kotlin.collections.ArrayList

class DailySpinAndWinActivity : ExtendedToolbarActivity() {

    private lateinit var settingsViewModel: SettingsViewModel
    private var numberList: ArrayList<Int> = ArrayList()
    //private var isRevealed = false
    private var targetValue = 0
    private var points = 0
    private var handler: Handler? = null
    private var runnableCode: Runnable? = null
    override val layoutId: Int
        get() = R.layout.activity_daily_spin_and_win

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setToolbarTitle(getString(R.string.daily_spin_and_win))

        settingsViewModel = ViewModelProvider(this).get(SettingsViewModel::class.java)

        settingsViewModel.scratchCardResponse().observe(this, {
            handleResponse(it)
        })

        settingsViewModel.addScratchCardResponse().observe(this, {
            if (null != it) {
                showWinnerAnimation()

                setUserPoints(it.user_points)

                Handler(Looper.getMainLooper()).postDelayed({
                    lwv.visibility = View.GONE
                    tvSpinAndWin.visibility = View.GONE
                    tvSpinAlreadyDone.visibility = View.VISIBLE
                    tvTNCHeader.visibility = View.VISIBLE
                    tvTNC.visibility = View.VISIBLE
                    tvSpinAlreadyDone.text = it.message
                    //tvYourPoints.visibility = View.VISIBLE
//                    tvYourPoints.text =
//                        "Your Points Are: " + SPreferenceManager.getInstance(this).settings.user_points
                    showAlertDialog(it.message)
                }, 3500L)

            } else {
                showSnackBar(getString(R.string.something_went_wrong))
            }
        })

//        idScratchCardIv.setRevealListener(object : ScratchImageView.IRevealListener {
//            override fun onRevealed(iv: ScratchImageView) {
//                // this method is called after revealing the image.
//                Toast.makeText(
//                    this@DailySpinAndWinActivity,
//                    "Congratulations",
//                    Toast.LENGTH_SHORT
//                ).show()
//            }
//
//            override fun onRevealPercentChangedListener(siv: ScratchImageView, percent: Float) {
//                // we can check how much percentage of
//                // image is revealed using percent variable
//                println("progress:" + percent * 100)
//                if (percent * 100 > 70 && !isRevealed) {
//                    idScratchCardIv.reveal()
//                    isRevealed = true
//                }
//            }
//        })

        settingsViewModel.popupBanners().observe(this, {
            handleResponse(it)
        })

        fetchScratchCard()

        fetchPopupBanner()
    }

    private fun showWinnerAnimation() {
        viewKonfetti.build()
            .addColors(
                ContextCompat.getColor(this, R.color.red_CC252C),
                ContextCompat.getColor(this, R.color.red_light_2),
                ContextCompat.getColor(this, R.color.yellow),
                ContextCompat.getColor(this, R.color.blue_5058AB)
            )
            .setDirection(0.0, 359.0)
            .setSpeed(1f, 5f)
            .setFadeOutEnabled(true)
            .setTimeToLive(2000L)
            .addShapes(Shape.Square, Shape.Circle)
            .addSizes(Size(12))
            .setPosition(-50f, viewKonfetti.width + 50f, -50f, -50f)
            .streamFor(300, 3000L)
    }

    @SuppressLint("SetTextI18n")
    private fun handleResponse(scratchCardResponse: ScratchCardResponse?) {
        lwv.visibility = View.VISIBLE
        tvSpinAndWin.visibility = View.VISIBLE
        tvTNCHeader.visibility = View.VISIBLE
        tvTNC.visibility = View.VISIBLE
        pbScratchCard.visibility = View.GONE

        tvTNC.text = HtmlCompat.fromHtml(
            getTermsByName("Scratch Card"),
            HtmlCompat.FROM_HTML_MODE_LEGACY
        )

        if (null != scratchCardResponse && scratchCardResponse.status == "1") {
            setupSpinWheel(scratchCardResponse)
        } else {
            lwv.visibility = View.GONE
            tvSpinAndWin.visibility = View.GONE
            tvTNCHeader.visibility = View.VISIBLE
            tvTNC.visibility = View.VISIBLE
            tvSpinAlreadyDone.visibility = View.VISIBLE
            tvSpinAlreadyDone.text = scratchCardResponse?.message
//            tvYourPoints.visibility = View.VISIBLE
//            tvYourPoints.text =
//                "Your Points are: " + SPreferenceManager.getInstance(this).settings.user_points
            setupEmptyWheel()
        }
    }

    private fun setupSpinWheel(scratchCardResponse: ScratchCardResponse) {
        numberList = getRandomNonRepeatingIntegers(
            12, scratchCardResponse.scratch_card[0].min.toInt(),
            scratchCardResponse.scratch_card[0].max.toInt()
        )

        val wheelItems: MutableList<WheelItem> = ArrayList()

//        wheelItems.add(
//            WheelItem(
//                ContextCompat.getColor(this, R.color.red_light),
//                getBitmapFromColor(ContextCompat.getColor(this, R.color.red_light)),
//                scratchCardResponse.scratch_card[0].min
//            )
////            WheelItem(
////                ContextCompat.getColor(this, R.color.red_light),
////                BitmapFactory.decodeResource(resources, R.drawable.ic_action_name),
////                scratchCardResponse.scratch_card[0].min
////            )
//        )

        wheelItems.add(
            WheelItem(
                ContextCompat.getColor(this, R.color.red_light_2),
                getBitmapFromColor(ContextCompat.getColor(this, R.color.red_light_2)),
                numberList[0].toString()
            )
        )
        wheelItems.add(
            WheelItem(
                ContextCompat.getColor(this, R.color.red_light_3),
                getBitmapFromColor(ContextCompat.getColor(this, R.color.red_light_3)),
                numberList[1].toString()
            )
        )

        wheelItems.add(
            WheelItem(
                ContextCompat.getColor(this, R.color.red_light),
                getBitmapFromColor(ContextCompat.getColor(this, R.color.red_light)),
                numberList[2].toString()
            )
        )

        wheelItems.add(
            WheelItem(
                ContextCompat.getColor(this, R.color.red_light_2),
                getBitmapFromColor(ContextCompat.getColor(this, R.color.red_light_2)),
                numberList[3].toString()
            )
        )

        wheelItems.add(
            WheelItem(
                ContextCompat.getColor(this, R.color.red_light_3),
                getBitmapFromColor(ContextCompat.getColor(this, R.color.red_light_3)),
                numberList[4].toString()
            )
        )

//        wheelItems.add(
//            WheelItem(
//                ContextCompat.getColor(this, R.color.red_light_2),
//                getBitmapFromColor(ContextCompat.getColor(this, R.color.red_light_2)),
//                scratchCardResponse.scratch_card[0].max
//            )
//        )
        wheelItems.add(
            WheelItem(
                ContextCompat.getColor(this, R.color.red_light),
                getBitmapFromColor(ContextCompat.getColor(this, R.color.red_light)),
                numberList[5].toString()
            )
        )

        wheelItems.add(
            WheelItem(
                ContextCompat.getColor(this, R.color.red_light_2),
                getBitmapFromColor(ContextCompat.getColor(this, R.color.red_light_2)),
                numberList[6].toString()
            )
        )

        wheelItems.add(
            WheelItem(
                ContextCompat.getColor(this, R.color.red_light_3),
                getBitmapFromColor(ContextCompat.getColor(this, R.color.red_light_3)),
                numberList[7].toString()
            )
        )

        wheelItems.add(
            WheelItem(
                ContextCompat.getColor(this, R.color.red_light),
                getBitmapFromColor(ContextCompat.getColor(this, R.color.red_light)),
                numberList[8].toString()
            )
        )

        wheelItems.add(
            WheelItem(
                ContextCompat.getColor(this, R.color.red_light_2),
                getBitmapFromColor(ContextCompat.getColor(this, R.color.red_light_2)),
                numberList[9].toString()
            )
        )

        wheelItems.add(
            WheelItem(
                ContextCompat.getColor(this, R.color.red_light_3),
                getBitmapFromColor(ContextCompat.getColor(this, R.color.red_light_3)),
                numberList[10].toString()
            )
        )

        wheelItems.add(
            WheelItem(
                ContextCompat.getColor(this, R.color.red_light),
                getBitmapFromColor(ContextCompat.getColor(this, R.color.red_light)),
                numberList[11].toString()
            )
        )

        lwv.setTarget(randomNumber())

        lwv.addWheelItems(wheelItems)

        lwv.setLuckyWheelReachTheTarget {
            if (isConnected(this)) {
                settingsViewModel.addScratchCard(
                    SPreferenceManager.getInstance(this).session,
                    points.toString()
                )
            } else {
                showSnackBar(getString(R.string.no_internet), this)
            }
        }
    }

    private fun setupEmptyWheel() {
        numberList = getRandomNonRepeatingIntegers(2, 1, 15)

        val wheelItems: MutableList<WheelItem> = ArrayList()

        wheelItems.add(
            WheelItem(
                ContextCompat.getColor(this, R.color.red_light_2),
                getBitmapFromColor(ContextCompat.getColor(this, R.color.red_light_2)),
                numberList[0].toString()
            )
        )

        wheelItems.add(
            WheelItem(
                ContextCompat.getColor(this, R.color.red_light_3),
                getBitmapFromColor(ContextCompat.getColor(this, R.color.red_light_3)),
                numberList[1].toString()
            )
        )

        lwv.setTarget(1)

        lwv.addWheelItems(wheelItems)

        lwv.setLuckyWheelReachTheTarget {

        }

        lwv.visibility = View.GONE
    }

    private fun fetchScratchCard() {
        if (isConnected(this)) {
            pbScratchCard.visibility = View.VISIBLE
            lwv.visibility = View.GONE
            tvSpinAndWin.visibility = View.GONE
            tvTNCHeader.visibility = View.GONE
            tvTNC.visibility = View.GONE
            settingsViewModel.getScratchCard(SPreferenceManager.getInstance(this).session)
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
        targetValue = (Math.random() * numberList.size).toInt()
//        val rand = Random()
//        targetValue = rand.nextInt(4 - 0 + 1) + 0
        if (targetValue <= 0) {
            targetValue = 1
        }
        points = numberList[targetValue - 1]
        return targetValue
    }

    private fun getRandomNonRepeatingIntegers(
        size: Int, min: Int, max: Int
    ): ArrayList<Int> {
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

    private fun getBitmapFromColor(color: Int): Bitmap {
        val bmp = Bitmap.createBitmap(10, 10, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bmp)
        canvas.drawColor(color)
        return bmp
    }

    private fun fetchPopupBanner() {
        if (isConnected(this)) {
            settingsViewModel.getPopupBanner("spin_win")
        } else {
            showSnackBar(getString(R.string.no_internet), this)
        }
    }

    private fun handleResponse(popupBannerResponse: PopupBannerResponse) {
        if (!popupBannerResponse.popup_banner.isNullOrEmpty()) {
            setupRepeatableBannerAd(
                popupBannerResponse.delay_time, popupBannerResponse.initial_time,
                popupBannerResponse.popup_banner
            )
        }
    }

    private fun setupRepeatableBannerAd(
        delayTime: String,
        initialTime: String,
        popupBanner: List<PopupBannerResponse.PopupBanner>
    ) {
        handler = Handler(Looper.getMainLooper())
        runnableCode = object : Runnable {
            override fun run() {
                if (!isDestroyed && !this@DailySpinAndWinActivity.isFinishing) {
                    if (this@DailySpinAndWinActivity.lifecycle.currentState
                            .isAtLeast(Lifecycle.State.RESUMED)
                    ) {
                        showProgressDialog(this@DailySpinAndWinActivity, popupBanner)
                    }
                    handler?.postDelayed(this, delayTime.toLong() * 1000)
                }
            }
        }

        if (!isDestroyed && !this.isFinishing) {
            runnableCode?.let {
                handler?.postDelayed(it, initialTime.toLong() * 1000)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (null != handler) {
            runnableCode?.let {
                handler!!.removeCallbacks(it)
            }
        }
    }
}