package com.politics.politicalapp.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.politics.politicalapp.R
import com.politics.politicalapp.pojo.MLAListResponse
import kotlinx.android.synthetic.main.dharasabhyo_item.view.*

class DharasabhyoAdapter(
    private val itemClickCall: (MLAListResponse.GovMla) -> Unit,
    private val itemClickWeb: (MLAListResponse.GovMla) -> Unit
) :
    RecyclerView.Adapter<DharasabhyoAdapter.HomeOffersViewHolder>() {

    private var list: ArrayList<MLAListResponse.GovMla> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeOffersViewHolder {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.dharasabhyo_item, parent, false)
        return HomeOffersViewHolder(
            view, itemClickCall, itemClickWeb
        )
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
        view: View,
        private val itemClickCall: (MLAListResponse.GovMla) -> Unit,
        private val itemClickWeb: (MLAListResponse.GovMla) -> Unit
    ) : RecyclerView.ViewHolder(view) {

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

                itemView.tvMLAName.text = newsPortal.mla_name
                itemView.tvPartyName.text = newsPortal.political_party
                itemView.tvCityName.text = newsPortal.city
                itemView.pbMLA.progress = newsPortal.percenrage.toFloat().toInt()
                itemView.tvPercentage.text = itemView.tvPercentage.context.getString(
                    R.string.percentage_,
                    newsPortal.percenrage
                )
                itemView.tvVotesTotal.text = "Votes: " + newsPortal.votes

                Glide.with(itemView.ivMLA.context)
                    .load(newsPortal.up_pro_img)
                    .into(itemView.ivMLA)
//
//                if (newsPortal.name.isNullOrEmpty()) {
//                    itemView.tvNewsPortalTitle.visibility = View.GONE
//                }
//                if (newsPortal.address.isNullOrEmpty()) {
//                    itemView.llNewsPortalAddress.visibility = View.GONE
//                }
//                if (newsPortal.contact_no.isNullOrEmpty()) {
//                    itemView.llNewsPortalPhone.visibility = View.GONE
//                }
//                if (newsPortal.email.isNullOrEmpty()) {
//                    itemView.llNewsPortalEmail.visibility = View.GONE
//                }
//                if (newsPortal.website.isNullOrEmpty()) {
//                    itemView.llNewsPortalWebsite.visibility = View.GONE
//                }
//
//                itemView.tvNewsPortalTitle.text = newsPortal.name
//                itemView.tvNewsPortalAddress.text = newsPortal.address
//                itemView.tvNewsPortalPhone.text = newsPortal.contact_no
//                itemView.tvNewsPortalEmail.text = newsPortal.email
//                itemView.tvNewsPortalWebsite.text = newsPortal.website
//
                itemView.rlDharasabhyaRoot.setOnClickListener {
                    itemClickCall(this)
                }
//
//                itemView.ivWeb.setOnClickListener {
//                    itemClickWeb(this)
//                }
            }
        }
    }
}