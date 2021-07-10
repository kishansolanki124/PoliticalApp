package com.politics.politicalapp.ui.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.ViewPager
import com.politics.politicalapp.R
import com.politics.politicalapp.ui.fragment.DharasabhyoProfileFragment
import com.politics.politicalapp.ui.fragment.DharasabhyoSpecialWorkFragment
import com.politics.politicalapp.ui.fragment.DharasabhyoYourReviewFragment

class DharasabhyoDetailActivity : ExtendedToolbarActivity() {

    private lateinit var viewPagerShraddhanjaliAdapter: ViewPagerShraddhanjaliAdapter
    private lateinit var viewPager: ViewPager

    override val layoutId: Int
        get() = R.layout.activity_dharasabhyo_detail

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setToolbarTitle("અમારો સંપર્ક")

        viewPagerShraddhanjaliAdapter =
            ViewPagerShraddhanjaliAdapter(supportFragmentManager)
        viewPager = findViewById(R.id.pager)
        viewPager.adapter = viewPagerShraddhanjaliAdapter
    }

    // Since this is an object collection, use a FragmentStatePagerAdapter,
    // and NOT a FragmentPagerAdapter.
    class ViewPagerShraddhanjaliAdapter(fm: FragmentManager) :
        FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

        override fun getCount(): Int = 3

        override fun getItem(i: Int): Fragment {

            return when (i) {
                0 -> {
                    DharasabhyoProfileFragment()
                }
                1 -> {
                    DharasabhyoSpecialWorkFragment()
                }
                else -> {
                    DharasabhyoYourReviewFragment()
                    //                fragment.arguments = Bundle().apply {
                    //                }
                    //                 Our object is just an integer :-P
                    //                putInt(ARG_OBJECT, i + 1)
                }
            }
        }

        override fun getPageTitle(position: Int): CharSequence {
            //return "OBJECT ${(position + 1)}"
            return if (position == 0) {
                "પ્રોફાઇલ"
            } else if (position == 1) {
                "વિશેષ કામગીરી"
            } else {
                "તમારો રીવ્યુ"
            }
        }
    }
}