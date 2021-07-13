package com.politics.politicalapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.politics.politicalapp.R
import kotlinx.android.synthetic.main.live_poll_item.view.*

class WinnerAdapter(
    private val itemClickCall: (String, Boolean) -> Unit,
    private val itemClickWeb: (String) -> Unit
) :
    RecyclerView.Adapter<WinnerAdapter.HomeOffersViewHolder>() {

    private var list: ArrayList<String> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeOffersViewHolder {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.winner_item, parent, false)
        return HomeOffersViewHolder(
            view, itemClickCall, itemClickWeb
        )
    }

    override fun onBindViewHolder(holder: HomeOffersViewHolder, position: Int) {
        holder.bindForecast(list[position], position)
    }

    fun setItem(list: ArrayList<String>) {
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
        private val itemClickCall: (String, Boolean) -> Unit,
        private val itemClickWeb: (String) -> Unit
    ) : RecyclerView.ViewHolder(view) {

//        constructor(parent: ViewGroup) : this(
//            LayoutInflater.from(parent.context).inflate(
//                R.layout.item_vatan_nu_gham,
//                parent, false
//            )
//        )

        fun bindForecast(
            newsPortal: String,
            position: Int
        ) {
            with(newsPortal) {

                if (position == 3 || position == 4) {
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