package com.app.colorsofgujarat.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.text.HtmlCompat
import androidx.fragment.app.Fragment
import com.app.colorsofgujarat.R
import com.app.colorsofgujarat.pojo.MLADetailResponse
import com.app.colorsofgujarat.ui.activity.DharasabhyoDetailActivity
import kotlinx.android.synthetic.main.fragment_dharasabhyo_special_work.*

class DharasabhyoSpecialWorkFragment : Fragment() {

    private lateinit var mlaDetailResponse: MLADetailResponse

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_dharasabhyo_special_work, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mlaDetailResponse = (activity as DharasabhyoDetailActivity).getMLADetailResponse()

        tvMLASpecialWork.text = HtmlCompat.fromHtml(
            mlaDetailResponse.gov_mla_detail[0].mla_work,
            HtmlCompat.FROM_HTML_MODE_LEGACY
        )
    }

}