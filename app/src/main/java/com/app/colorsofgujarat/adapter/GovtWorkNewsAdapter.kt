package com.app.colorsofgujarat.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.app.colorsofgujarat.R
import com.app.colorsofgujarat.pojo.GovtWorkListResponse
import kotlinx.android.synthetic.main.govt_work_news_item.view.*

class GovtWorkNewsAdapter(
    private val itemClickCall: (GovtWorkListResponse.GovWork) -> Unit,
    private val itemClickShare: (GovtWorkListResponse.GovWork) -> Unit
) :
    RecyclerView.Adapter<GovtWorkNewsAdapter.HomeOffersViewHolder>() {

    private var list: ArrayList<GovtWorkListResponse.GovWork> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeOffersViewHolder {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.govt_work_news_item, parent, false)
        return HomeOffersViewHolder(
            view, itemClickCall, itemClickShare
        )
    }

    override fun onBindViewHolder(holder: HomeOffersViewHolder, position: Int) {
        holder.bindForecast(list[position])
    }

    fun setItem(list: ArrayList<GovtWorkListResponse.GovWork>) {
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
        private val itemClickCall: (GovtWorkListResponse.GovWork) -> Unit,
        private val itemClickShare: (GovtWorkListResponse.GovWork) -> Unit
    ) : RecyclerView.ViewHolder(view) {

        fun bindForecast(
            newsPortal: GovtWorkListResponse.GovWork
        ) {
            with(newsPortal) {

                itemView.tvNewsTitle.text = newsPortal.name
                itemView.tvRateReceived.text = newsPortal.average_rating

                Glide.with(itemView.ivNews.context)
                    .load(newsPortal.up_pro_img)
                    .into(itemView.ivNews)

                itemView.cvRootGovtWorkNewsItem.setOnClickListener {
                    itemClickCall(this)
                }

                itemView.ivNewsShare.setOnClickListener {
                    itemClickShare(this)
                }
//
//                itemView.ivWeb.setOnClickListener {
//                    itemClickShare(this)
//                }
            }
        }
    }
}