package com.politics.politicalapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.politics.politicalapp.R
import kotlinx.android.synthetic.main.poll_and_survey_item.view.*

class PollAndSurveyAdapter(
    private val itemClickCall: (String) -> Unit,
    private val itemClickWeb: (String) -> Unit
) :
    RecyclerView.Adapter<PollAndSurveyAdapter.HomeOffersViewHolder>() {

    private var list: ArrayList<String> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeOffersViewHolder {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.poll_and_survey_item, parent, false)
        return HomeOffersViewHolder(
            view, itemClickCall, itemClickWeb
        )
    }

    override fun onBindViewHolder(holder: HomeOffersViewHolder, position: Int) {
        holder.bindForecast(list[position])
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
            newsPortal: String
        ) {
            with(newsPortal) {
                itemView.rbBad.setOnClickListener {
                    resetRadioButtons()
                    itemView.rbBad.isChecked = !itemView.rbBad.isChecked
                }

                itemView.rbExcellent.setOnClickListener {
                    resetRadioButtons()
                    itemView.rbExcellent.isChecked = !itemView.rbBad.isChecked
                }

                itemView.rbGood.setOnClickListener {
                    resetRadioButtons()
                    itemView.rbGood.isChecked = !itemView.rbBad.isChecked
                }

                itemView.rbVeryBad.setOnClickListener {
                    resetRadioButtons()
                    itemView.rbVeryBad.isChecked = !itemView.rbBad.isChecked
                }

                itemView.rbcantAnswer.setOnClickListener {
                    resetRadioButtons()
                    itemView.rbcantAnswer.isChecked = !itemView.rbBad.isChecked
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
//                itemView.rlDharasabhyaRoot.setOnClickListener {
//                    itemClickCall(this)
//                }
//
//                itemView.ivWeb.setOnClickListener {
//                    itemClickWeb(this)
//                }
            }
        }

        private fun resetRadioButtons() {
            itemView.rbBad.isChecked = false
            itemView.rbVeryBad.isChecked = false
            itemView.rbGood.isChecked = false
            itemView.rbExcellent.isChecked = false
            itemView.rbcantAnswer.isChecked = false
        }
    }
}