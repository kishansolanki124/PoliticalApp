package com.app.colorsofgujarat.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.colorsofgujarat.R
import com.app.colorsofgujarat.databinding.NotificationItemBinding
import com.app.colorsofgujarat.pojo.NotificationResponse

class NotificationAdapter :
    RecyclerView.Adapter<NotificationAdapter.HomeOffersViewHolder>() {

    private var list: ArrayList<NotificationResponse.Notification> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeOffersViewHolder {
//        val view =
//            LayoutInflater.from(parent.context)
//                .inflate(R.layout.notification_item, parent, false)
        val binding =
            NotificationItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return HomeOffersViewHolder(
            binding
        )
    }

    override fun onBindViewHolder(holder: HomeOffersViewHolder, position: Int) {
        holder.bindForecast(list[position])
    }

    fun setItem(list: ArrayList<NotificationResponse.Notification>) {
        this.list = list
        notifyDataSetChanged()
    }

    fun reset() {
        this.list.clear()
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = list.size

    class HomeOffersViewHolder(
        private val binding: NotificationItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bindForecast(
            newsPortal: NotificationResponse.Notification
        ) {
            binding.tvNotificationTime.text =
                binding.tvNotificationTitle.context.getString(R.string.Date_, newsPortal.pdate)
            binding.tvNotificationTitle.text = newsPortal.name
        }
    }
}