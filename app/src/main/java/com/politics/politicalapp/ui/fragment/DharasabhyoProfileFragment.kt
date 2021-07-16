package com.politics.politicalapp.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.text.HtmlCompat
import androidx.fragment.app.Fragment
import com.politics.politicalapp.R
import com.politics.politicalapp.pojo.MLADetailResponse
import com.politics.politicalapp.ui.activity.DharasabhyoDetailActivity
import kotlinx.android.synthetic.main.fragment_dharasabhyo_profile.*

class DharasabhyoProfileFragment : Fragment() {

    private lateinit var mlaDetailResponse: MLADetailResponse

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_dharasabhyo_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mlaDetailResponse = (activity as DharasabhyoDetailActivity).getMLADetailResponse()

        tvMLAProfile.text = HtmlCompat.fromHtml(
            mlaDetailResponse.gov_mla_detail[0].mla_profile,
            HtmlCompat.FROM_HTML_MODE_LEGACY
        )
    }
}