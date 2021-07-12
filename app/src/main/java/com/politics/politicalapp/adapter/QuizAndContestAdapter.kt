package com.politics.politicalapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.politics.politicalapp.R
import kotlinx.android.synthetic.main.quiz_and_contest_item.view.*

class QuizAndContestAdapter(
    private val itemClickCall: (String) -> Unit,
    private val itemClickWeb: (String) -> Unit
) :
    RecyclerView.Adapter<QuizAndContestAdapter.HomeOffersViewHolder>() {

    private var list: ArrayList<String> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeOffersViewHolder {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.quiz_and_contest_item, parent, false)
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
        private val itemClickCall: (String) -> Unit,
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
                    itemView.tvGive_rate_get_10_point.text = ""
                    itemView.btSubmitRegister.text = "check winner"
                    itemView.btSubmitRegister.setBackgroundColor(
                        ContextCompat.getColor(
                            itemView.btSubmitRegister.context, R.color.gray_BEBEBE
                        )
                    )
                } else {
                    itemView.tvGive_rate_get_10_point.text =
                        "(Participate &amp; Get 10 points / Win Prize)"
                    itemView.btSubmitRegister.text = "running"
                    itemView.btSubmitRegister.setBackgroundColor(
                        ContextCompat.getColor(
                            itemView.btSubmitRegister.context, R.color.red_CC252C
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
                itemView.cvRootGovtWorkNewsItem.setOnClickListener {
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