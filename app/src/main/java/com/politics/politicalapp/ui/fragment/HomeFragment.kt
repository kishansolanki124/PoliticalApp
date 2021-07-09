package com.politics.politicalapp.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.politics.politicalapp.R
import com.politics.politicalapp.adapter.BreakingNewsAdapter
import com.politics.politicalapp.apputils.setShowSideItems
import com.politics.politicalapp.ui.activity.GovtWorkActivity
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupListeners()
    }

    private fun setupListeners() {
        llGovtWork.setOnClickListener {
            startActivity(Intent(requireContext(), GovtWorkActivity::class.java))
        }
    }

    private fun setupRecyclerView() {

        val stringList: ArrayList<String> = ArrayList()
        stringList.add("S")
        stringList.add("S")
        stringList.add("S")
        stringList.add("S")
        stringList.add("S")

        val adapter = BreakingNewsAdapter {
//            startActivity(
//                Intent(
//                    requireActivity(), NewsDetailsActivity::class.java
//                ).putExtra(AppConstants.NEWS_ID, it.id)
//            )
        }

        adapter.setItem(stringList)

//        rvNewsHome.adapter = adapter
//
//        rvNewsHome.addOnScrollListener(object : RecyclerView.OnScrollListener() {
//            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
//                super.onScrolled(recyclerView, dx, dy)
//                val offset: Int = rvNewsHome.computeHorizontalScrollOffset()
//                if (offset % rvNewsHome.width == 0) {
//                    val position: Int = rvNewsHome
//                    Log.e("Current position is", position.toString())
//                    requireActivity().showToast(position.toString())
//                }
//            }
//        })

        //showing next page's partial visibility
        newsHomeViewPager.setShowSideItems(50, 0)

        newsHomeViewPager.adapter = adapter

        newsHomeViewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageScrollStateChanged(state: Int) {
                println(state)
            }

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels)
                println(position)
            }


            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                tvNewsIndex.text = (position + 1).toString() + "/" + stringList.size
            }
        })
    }
}