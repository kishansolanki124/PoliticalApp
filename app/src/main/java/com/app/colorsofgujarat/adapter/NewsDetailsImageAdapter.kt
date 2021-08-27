package com.app.colorsofgujarat.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.colorsofgujarat.databinding.NewsDetailsImageItemBinding
import com.bumptech.glide.Glide

class NewsDetailsImageAdapter(
    private val mListener: (String?, Int) -> Unit
) :
    RecyclerView.Adapter<NewsDetailsImageAdapter.HomeOffersViewHolder>() {

    private var list: List<String> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeOffersViewHolder {
        val binding =
            NewsDetailsImageItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )

        return HomeOffersViewHolder(
            binding
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

    class HomeOffersViewHolder(private val binding: NewsDetailsImageItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(viewPagerImageModel: String) {
            Glide.with(binding.ivHomeViewPager.context)
                .load(viewPagerImageModel)
                .into(binding.ivHomeViewPager)
        }
    }
}