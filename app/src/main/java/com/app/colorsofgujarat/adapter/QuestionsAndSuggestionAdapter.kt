package com.app.colorsofgujarat.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.colorsofgujarat.databinding.QuestionSuggestionItemBinding
import com.app.colorsofgujarat.pojo.UserAdviseResponse
import com.bumptech.glide.Glide

class QuestionsAndSuggestionAdapter(
    private val itemClickCall: (UserAdviseResponse.UserAdvice) -> Unit,
    private val itemShareCall: (UserAdviseResponse.UserAdvice) -> Unit
) :
    RecyclerView.Adapter<QuestionsAndSuggestionAdapter.HomeOffersViewHolder>() {

    private var list: ArrayList<UserAdviseResponse.UserAdvice> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeOffersViewHolder {
//        val view =
//            LayoutInflater.from(parent.context)
//                .inflate(R.layout.question_suggestion_item, parent, false)
        val binding =
            QuestionSuggestionItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return HomeOffersViewHolder(
            binding, itemClickCall, itemShareCall
        )
    }

    override fun onBindViewHolder(holder: HomeOffersViewHolder, position: Int) {
        holder.bindForecast(list[position])
    }

    fun setItem(list: ArrayList<UserAdviseResponse.UserAdvice>) {
        val size = this.list.size
        this.list.addAll(list)
        val sizeNew = this.list.size
        notifyItemRangeChanged(size, sizeNew)
    }

    fun reset() {
        val size = this.list.size
        this.list.clear()
        //notifyDataSetChanged()
        notifyItemRangeRemoved(0, size)
    }

    override fun getItemCount(): Int = list.size

    class HomeOffersViewHolder(
        private val binding: QuestionSuggestionItemBinding,
        private val itemClickCall: (UserAdviseResponse.UserAdvice) -> Unit,
        private val itemShareCall: (UserAdviseResponse.UserAdvice) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bindForecast(
            newsPortal: UserAdviseResponse.UserAdvice
        ) {
            with(newsPortal) {

                Glide.with(binding.ivQuestionSuggestion.context)
                    .load(newsPortal.up_pro_img)
                    .into(binding.ivQuestionSuggestion)

//                Glide.with(binding.ivQuestionSuggestionUser.context)
//                    .load(newsPortal.)
//                    .into(binding.ivQuestionSuggestionUser)

                binding.tvQuestionSuggestionUserName.text = newsPortal.user_name
                binding.tvUserCityDistrict.text = newsPortal.city + ", " + newsPortal.district
                binding.tvQuestionSuggestion.text = newsPortal.title
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
                binding.cvRootGovtWorkNewsItem.setOnClickListener {
                    itemClickCall(this)
                }

                binding.ivShareQuestionSuggestion.setOnClickListener {
                    itemShareCall(this)
                }
//
//                binding.ivWeb.setOnClickListener {
//                    itemClickWeb(this)
//                }
            }
        }
    }
}