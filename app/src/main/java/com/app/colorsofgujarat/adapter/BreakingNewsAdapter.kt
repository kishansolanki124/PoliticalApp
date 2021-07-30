package com.app.colorsofgujarat.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.app.colorsofgujarat.R
import com.app.colorsofgujarat.pojo.SettingsResponse
import kotlinx.android.synthetic.main.home_news_item.view.*

class BreakingNewsAdapter(
    private val itemClick: (SettingsResponse.News) -> Unit
) : RecyclerView.Adapter<BreakingNewsAdapter.HomeOffersViewHolder>() {

    private var list: List<SettingsResponse.News> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeOffersViewHolder {
        return HomeOffersViewHolder(
            parent
        )
    }

    override fun onBindViewHolder(holder: HomeOffersViewHolder, position: Int) {

        holder.itemView.setOnClickListener {
            list[position].let { it1 -> itemClick.invoke(it1) }
        }

        holder.bind(list[position])
    }

    fun setItem(list: List<SettingsResponse.News>) {
        this.list = list
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = list.size

    class HomeOffersViewHolder constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {

        constructor(parent: ViewGroup) : this(
            LayoutInflater.from(parent.context).inflate(
                R.layout.home_news_item,
                parent, false
            )
        )

        fun bind(introImageModel: SettingsResponse.News) {
            Glide.with(itemView.ivBreakingNews.context)
                .load(introImageModel.up_pro_img)
                .into(itemView.ivBreakingNews)

            itemView.tvNewsTitle.text = introImageModel.name
        }
    }
}