package com.app.colorsofgujarat.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.colorsofgujarat.databinding.CommentItemBinding
import com.app.colorsofgujarat.pojo.GovtWorkDetailResponse

class NewsCommentAdapter(
    private val itemClickCall: (GovtWorkDetailResponse.UserComment) -> Unit,
    private val itemClickWeb: (GovtWorkDetailResponse.UserComment) -> Unit
) :
    RecyclerView.Adapter<NewsCommentAdapter.HomeOffersViewHolder>() {

    private var list: ArrayList<GovtWorkDetailResponse.UserComment> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeOffersViewHolder {
//        val view =
//            LayoutInflater.from(parent.context)
//                .inflate(R.layout.comment_item, parent, false)
        val binding =
            CommentItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )

        return HomeOffersViewHolder(
            binding, itemClickCall, itemClickWeb
        )
    }

    override fun onBindViewHolder(holder: HomeOffersViewHolder, position: Int) {
        holder.bindForecast(list[position])
    }

    fun setItem(list: ArrayList<GovtWorkDetailResponse.UserComment>) {
        this.list.addAll(list)
        notifyDataSetChanged()
    }

    fun reset() {
        this.list.clear()
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = list.size

    class HomeOffersViewHolder(
        private val binding: CommentItemBinding,
        private val itemClickCall: (GovtWorkDetailResponse.UserComment) -> Unit,
        private val itemClickWeb: (GovtWorkDetailResponse.UserComment) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

//        constructor(parent: ViewGroup) : this(
//            LayoutInflater.from(parent.context).inflate(
//                R.layout.item_vatan_nu_gham,
//                parent, false
//            )
//        )

        fun bindForecast(
            newsPortal: GovtWorkDetailResponse.UserComment
        ) {
            with(newsPortal) {
                binding.tvCommentBy.text = newsPortal.user_name
                binding.tvComment.text = newsPortal.comment

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
//                    itemClickCall(this)
//                }
//
//                itemView.ivWeb.setOnClickListener {
//                    itemClickWeb(this)
//                }
            }
        }
    }
}