package com.app.colorsofgujarat.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.text.HtmlCompat
import androidx.fragment.app.Fragment
import com.app.colorsofgujarat.databinding.FragmentDharasabhyoSpecialWorkBinding
import com.app.colorsofgujarat.pojo.MLADetailResponse
import com.app.colorsofgujarat.ui.activity.DharasabhyoDetailActivity

class DharasabhyoSpecialWorkFragment : Fragment() {

    private lateinit var mlaDetailResponse: MLADetailResponse
    private lateinit var binding: FragmentDharasabhyoSpecialWorkBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        //return inflater.inflate(R.layout.fragment_dharasabhyo_special_work, container, false)
        binding = FragmentDharasabhyoSpecialWorkBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mlaDetailResponse = (activity as DharasabhyoDetailActivity).getMLADetailResponse()

        binding.tvMLASpecialWork.text = HtmlCompat.fromHtml(
            mlaDetailResponse.gov_mla_detail[0].mla_work,
            HtmlCompat.FROM_HTML_MODE_LEGACY
        )
    }
}