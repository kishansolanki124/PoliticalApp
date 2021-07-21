package com.politics.politicalapp.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.politics.politicalapp.R
import com.politics.politicalapp.pojo.WinnerListResponse
import kotlinx.android.synthetic.main.winner_item.view.*

class WinnerAdapter(
    private val itemClickCall: (WinnerListResponse.PointsPrize, Boolean) -> Unit,
    private val itemClickWeb: (WinnerListResponse.PointsPrize) -> Unit
) :
    RecyclerView.Adapter<WinnerAdapter.HomeOffersViewHolder>() {

    private var list: ArrayList<WinnerListResponse.PointsPrize> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeOffersViewHolder {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.winner_item, parent, false)
        return HomeOffersViewHolder(
            view, itemClickCall, itemClickWeb
        )
    }

    override fun onBindViewHolder(holder: HomeOffersViewHolder, position: Int) {
        holder.bindForecast(list[position])
    }

    fun setItem(list: ArrayList<WinnerListResponse.PointsPrize>) {
        this.list = list
        notifyDataSetChanged()
    }

    fun reset() {
        this.list.clear()
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = list.size

    class HomeOffersViewHolder(
        view: View,
        private val itemClickCall: (WinnerListResponse.PointsPrize, Boolean) -> Unit,
        private val itemClickWeb: (WinnerListResponse.PointsPrize) -> Unit
    ) : RecyclerView.ViewHolder(view) {

        @SuppressLint("SetTextI18n")
        fun bindForecast(
            newsPortal: WinnerListResponse.PointsPrize
        ) {
            with(newsPortal) {

                itemView.tvWinnerTitle.text = newsPortal.name1 + "\n" + newsPortal.name2
                if (newsPortal.prize_mode == "Points Winner") {
                    itemView.btViewWinner.text =
                        itemView.btViewWinner.context.getString(R.string.Check_Winner)
                    itemView.btViewWinner.setBackgroundColor(
                        ContextCompat.getColor(
                            itemView.btViewWinner.context, R.color.gray_BEBEBE
                        )
                    )
                } else {
                    itemView.btViewWinner.text =
                        itemView.btViewWinner.context.getString(R.string.Prize_Details)
                    itemView.btViewWinner.setBackgroundColor(
                        ContextCompat.getColor(
                            itemView.btViewWinner.context, R.color.red_CC252C
                        )
                    )
                }

//                Glide.with(itemView.ivNewsPortal.context)
//                    .load(newsPortal.up_pro_img)
//                    .into(itemView.ivNewsPortal)
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
//                itemView.cvRootGovtWorkNewsItem.setOnClickListener {
//                    if (itemView.btViewWinner.text.equals("result")) {
//                        itemClickCall(this, false)
//                    } else {
//                        itemClickCall(this, true)
//                    }
//
//                }

                itemView.btViewWinner.setOnClickListener {
                    if (itemView.btViewWinner.text.equals(
                            itemView.btViewWinner.context.getString(R.string.Check_Winner)
                        )
                    ) {
                        itemClickWeb(this)
                    } else {
                        itemClickCall(this, false)
                    }
                }
//
//                itemView.ivWeb.setOnClickListener {
//                    itemClickWeb(this)
//                }
            }
        }
    }
}