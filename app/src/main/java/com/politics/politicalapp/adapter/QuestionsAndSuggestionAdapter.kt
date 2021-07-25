package com.politics.politicalapp.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.politics.politicalapp.R
import com.politics.politicalapp.pojo.UserAdviseResponse
import kotlinx.android.synthetic.main.question_suggestion_item.view.*

class QuestionsAndSuggestionAdapter(
    private val itemClickCall: (UserAdviseResponse.UserAdvice) -> Unit,
    private val itemShareCall: (UserAdviseResponse.UserAdvice) -> Unit
) :
    RecyclerView.Adapter<QuestionsAndSuggestionAdapter.HomeOffersViewHolder>() {

    private var list: ArrayList<UserAdviseResponse.UserAdvice> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeOffersViewHolder {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.question_suggestion_item, parent, false)
        return HomeOffersViewHolder(
            view, itemClickCall, itemShareCall
        )
    }

    override fun onBindViewHolder(holder: HomeOffersViewHolder, position: Int) {
        holder.bindForecast(list[position])
    }

    fun setItem(list: ArrayList<UserAdviseResponse.UserAdvice>) {
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
        private val itemClickCall: (UserAdviseResponse.UserAdvice) -> Unit,
        private val itemShareCall: (UserAdviseResponse.UserAdvice) -> Unit
    ) : RecyclerView.ViewHolder(view) {

//        constructor(parent: ViewGroup) : this(
//            LayoutInflater.from(parent.context).inflate(
//                R.layout.item_vatan_nu_gham,
//                parent, false
//            )
//        )

        @SuppressLint("SetTextI18n")
        fun bindForecast(
            newsPortal: UserAdviseResponse.UserAdvice
        ) {
            with(newsPortal) {

                Glide.with(itemView.ivQuestionSuggestion.context)
                    .load(newsPortal.up_pro_img)
                    .into(itemView.ivQuestionSuggestion)

//                Glide.with(itemView.ivQuestionSuggestionUser.context)
//                    .load(newsPortal.)
//                    .into(itemView.ivQuestionSuggestionUser)

                itemView.tvQuestionSuggestionUserName.text = newsPortal.user_name
                itemView.tvUserCityDistrict.text = newsPortal.city + ", " + newsPortal.district
                itemView.tvQuestionSuggestion.text = newsPortal.title
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

                itemView.ivShareQuestionSuggestion.setOnClickListener {
                    itemShareCall(this)
                }
//
//                itemView.ivWeb.setOnClickListener {
//                    itemClickWeb(this)
//                }
            }
        }
    }
}