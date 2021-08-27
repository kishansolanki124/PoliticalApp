package com.app.colorsofgujarat.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.colorsofgujarat.databinding.HomeNewsItemBinding
import com.app.colorsofgujarat.pojo.SettingsResponse
import com.bumptech.glide.Glide

class BreakingNewsAdapter(
    private val itemClick: (SettingsResponse.News) -> Unit
) : RecyclerView.Adapter<BreakingNewsAdapter.HomeOffersViewHolder>() {

    private var list: List<SettingsResponse.News> = listOf()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): HomeOffersViewHolder {
        val binding =
            HomeNewsItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return HomeOffersViewHolder(binding, itemClick)
    }

//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeOffersViewHolder {
//        val itemBinding: HomeNewsItemBinding = HomeNewsItemBinding.inflate(
//            LayoutInflater.from(parent.context),
//            parent,
//            false
//        )
//        return HomeOffersViewHolder(itemBinding,parent)
////        return HomeOffersViewHolder(
////            parent
////        )
//    }

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

//    class HomeOffersViewHolder constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
//
//        constructor(parent: ViewGroup) : this(
//            LayoutInflater.from(parent.context).inflate(
//                R.layout.home_news_item,
//                parent, false
//            )
//        )
//
//        fun bind(introImageModel: SettingsResponse.News) {
//            Glide.with(itemView.ivBreakingNews.context)
//                .load(introImageModel.up_pro_img)
//                .into(itemView.ivBreakingNews)
//
//            itemView.tvNewsTitle.text = introImageModel.name
//        }
//    }

    class HomeOffersViewHolder(
        private val binding: HomeNewsItemBinding,
        private val itemClick: (SettingsResponse.News) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(introImageModel: SettingsResponse.News) {
            Glide.with(binding.ivBreakingNews.context)
                .load(introImageModel.up_pro_img)
                .into(binding.ivBreakingNews)

            binding.tvNewsTitle.text = introImageModel.name
        }
    }
}