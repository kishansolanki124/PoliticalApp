package com.app.colorsofgujarat.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.colorsofgujarat.R
import com.app.colorsofgujarat.databinding.DharasabhyoItemBinding
import com.app.colorsofgujarat.pojo.MLAListResponse
import com.bumptech.glide.Glide

class DharasabhyoAdapter(
    private val itemClickCall: (MLAListResponse.GovMla) -> Unit,
    private val itemClickWeb: (MLAListResponse.GovMla) -> Unit
) :
    RecyclerView.Adapter<DharasabhyoAdapter.HomeOffersViewHolder>() {

    private var list: ArrayList<MLAListResponse.GovMla> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeOffersViewHolder {
//        val view =
//            LayoutInflater.from(parent.context)
//                .inflate(R.layout.dharasabhyo_item, parent, false)
//        return HomeOffersViewHolder(
//            view, itemClickCall, itemClickWeb
//        )

        val binding =
            DharasabhyoItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return HomeOffersViewHolder(binding, itemClickCall, itemClickWeb)
    }

    override fun onBindViewHolder(holder: HomeOffersViewHolder, position: Int) {
        holder.bindForecast(list[position])
    }

    fun setItem(list: ArrayList<MLAListResponse.GovMla>) {
        this.list.addAll(list)
        notifyDataSetChanged()
    }

    fun reset() {
        this.list.clear()
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = list.size

    class HomeOffersViewHolder(
        private val binding: DharasabhyoItemBinding,
        private val itemClickCall: (MLAListResponse.GovMla) -> Unit,
        private val itemClickWeb: (MLAListResponse.GovMla) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

//        constructor(parent: ViewGroup) : this(
//            LayoutInflater.from(parent.context).inflate(
//                R.layout.item_vatan_nu_gham,
//                parent, false
//            )
//        )

        @SuppressLint("SetTextI18n")
        fun bindForecast(
            newsPortal: MLAListResponse.GovMla
        ) {
            with(newsPortal) {

                binding.tvMLAName.text = newsPortal.mla_name
                binding.tvPartyName.text = newsPortal.political_party
                binding.tvCityName.text = newsPortal.city
                binding.pbMLA.progress = newsPortal.percentage.toFloat().toInt()
                binding.tvPercentage.text = binding.tvPercentage.context.getString(
                    R.string.percentage_,
                    newsPortal.percentage
                )
                binding.tvVotesTotal.text = "Votes: " + newsPortal.votes

                Glide.with(binding.ivMLA.context)
                    .load(newsPortal.up_pro_img)
                    .into(binding.ivMLA)
//
//                if (newsPortal.name.isNullOrEmpty()) {
//                    binding.tvNewsPortalTitle.visibility = View.GONE
//                }
//                if (newsPortal.address.isNullOrEmpty()) {
//                    binding.llNewsPortalAddress.visibility = View.GONE
//                }
//                if (newsPortal.contact_no.isNullOrEmpty()) {
//                    binding.llNewsPortalPhone.visibility = View.GONE
//                }
//                if (newsPortal.email.isNullOrEmpty()) {
//                    binding.llNewsPortalEmail.visibility = View.GONE
//                }
//                if (newsPortal.website.isNullOrEmpty()) {
//                    binding.llNewsPortalWebsite.visibility = View.GONE
//                }
//
//                binding.tvNewsPortalTitle.text = newsPortal.name
//                binding.tvNewsPortalAddress.text = newsPortal.address
//                binding.tvNewsPortalPhone.text = newsPortal.contact_no
//                binding.tvNewsPortalEmail.text = newsPortal.email
//                binding.tvNewsPortalWebsite.text = newsPortal.website
//
                binding.rlDharasabhyaRoot.setOnClickListener {
                    itemClickCall(this)
                }
//
//                binding.ivWeb.setOnClickListener {
//                    itemClickWeb(this)
//                }
            }
        }
    }
}