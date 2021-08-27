package com.app.colorsofgujarat.ui.activity

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import app.app.patidarsaurabh.apputils.AppConstants
import com.app.colorsofgujarat.R
import com.app.colorsofgujarat.databinding.ActivityDisplayPictureBinding
import com.bumptech.glide.Glide

class DisplayPictureActivity : AppCompatActivity(), ViewPager.OnPageChangeListener {

    private var mAlbumList: ArrayList<String>? = null
    private var mSize: Int = 0
    private lateinit var binding: ActivityDisplayPictureBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDisplayPictureBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        init()
    }

    private fun init() {
        mAlbumList =
            intent.getSerializableExtra(AppConstants.IMAGE_LIST) as ArrayList<String>
        val pos = intent.getIntExtra(AppConstants.IMAGE_POSITION, 0)

        binding.viewPager.addOnPageChangeListener(this)
        binding.viewPager.adapter = PictureAdapter(this)
        mSize = if (mAlbumList != null) mAlbumList!!.size else 0
        if (pos < mSize) {
            binding.viewPager.currentItem = pos
        }
    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}

    override fun onPageSelected(position: Int) {

    }

    override fun onPageScrollStateChanged(state: Int) {

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }


    private inner class PictureAdapter(context: Context) :
        PagerAdapter() {
        private val mInflater = LayoutInflater.from(context)

        override fun getCount(): Int {
            return if (mAlbumList != null) mAlbumList!!.size else 0
        }

        override fun isViewFromObject(view: View, `object`: Any): Boolean {
            return view === `object`
        }

        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            val itemView = mInflater.inflate(R.layout.item_zoom_image, container, false)

            val tivNewsImage =
                itemView.findViewById<com.github.chrisbanes.photoview.PhotoView>(R.id.tivNewsImage)

//            val circularProgressDrawable = CircularProgressDrawable(context)
//            circularProgressDrawable.strokeWidth = 5f
//            circularProgressDrawable.centerRadius = 30f
//            circularProgressDrawable.start()

//            val circularProgressDrawable = CircularProgressDrawable(this@DisplayPictureActivity)
//            circularProgressDrawable.strokeWidth = 5f
//            circularProgressDrawable.centerRadius = 30f
//
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
//                circularProgressDrawable.colorFilter = BlendModeColorFilter(
//                    ContextCompat.getColor(
//                        this@DisplayPictureActivity,
//                        R.color.white
//                    ), BlendMode.SRC_ATOP
//                )
//            } else {
//                circularProgressDrawable.setColorFilter(
//                    ContextCompat.getColor(
//                        this@DisplayPictureActivity,
//                        R.color.white
//                    ), PorterDuff.Mode.SRC_ATOP
//                )
//            }

            Glide.with(this@DisplayPictureActivity)
                .load(mAlbumList!![position])
//                .apply(
//                    RequestOptions()
//                        .placeholder(circularProgressDrawable)
//                    //.error(R.drawable.default_image_placeholder)
//                )
                .into(tivNewsImage)

            container.addView(itemView)
            return itemView
        }

        override fun destroyItem(container: ViewGroup, position: Int, anyType: Any) {
            container.removeView(anyType as LinearLayout)
        }
    }
}