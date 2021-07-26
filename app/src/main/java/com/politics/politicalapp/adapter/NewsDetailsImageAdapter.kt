package com.politics.politicalapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.politics.politicalapp.R
import com.politics.politicalapp.pojo.ViewPagerImageModel
import kotlinx.android.synthetic.main.news_details_image_item.view.*

class NewsDetailsImageAdapter(
    private val mListener: (String?, Int) -> Unit
) :
    RecyclerView.Adapter<NewsDetailsImageAdapter.HomeOffersViewHolder>() {

    private var list: List<String> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeOffersViewHolder {
        return HomeOffersViewHolder(
            parent
        )
    }


    override fun onBindViewHolder(holder: HomeOffersViewHolder, position: Int) {
        holder.bind(list[position])

        holder.itemView.setOnClickListener {
            list[position].let { it1 -> mListener.invoke(it1, position) }
        }
    }

    fun setItem(list: List<String>) {
        this.list = list
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = list.size

    class HomeOffersViewHolder constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {

        constructor(parent: ViewGroup) : this(
            LayoutInflater.from(parent.context).inflate(
                R.layout.news_details_image_item,
                parent, false
            )
        )

        fun bind(viewPagerImageModel: String) {
            Glide.with(itemView.ivHomeViewPager.context)
                .load(viewPagerImageModel)
                .into(itemView.ivHomeViewPager)
        }
    }
}