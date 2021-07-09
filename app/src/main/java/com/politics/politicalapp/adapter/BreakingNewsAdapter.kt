package com.politics.politicalapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.politics.politicalapp.R

class BreakingNewsAdapter(
    private val itemClick: (String) -> Unit
) : RecyclerView.Adapter<BreakingNewsAdapter.HomeOffersViewHolder>() {

    private var list: List<String> = listOf()

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

    fun setItem(list: List<String>) {
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

        fun bind(introImageModel: String) {
            println(introImageModel)

//            Glide.with(itemView.ivBreakingNews.context)
//                .load(introImageModel.up_pro_img)
//                .into(itemView.ivBreakingNews)

            //itemView.tvNewsTitle.text = introImageModel.name
        }
    }
}