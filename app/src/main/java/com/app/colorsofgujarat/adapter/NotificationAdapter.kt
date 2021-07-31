package com.app.colorsofgujarat.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.colorsofgujarat.R
import com.app.colorsofgujarat.pojo.NotificationResponse
import kotlinx.android.synthetic.main.notification_item.view.*

class NotificationAdapter :
    RecyclerView.Adapter<NotificationAdapter.HomeOffersViewHolder>() {

    private var list: ArrayList<NotificationResponse.Notification> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeOffersViewHolder {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.notification_item, parent, false)
        return HomeOffersViewHolder(
            view
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
        view: View
    ) : RecyclerView.ViewHolder(view) {

        fun bindForecast(
            newsPortal: NotificationResponse.Notification
        ) {
            itemView.tvNotificationTime.text =
                itemView.tvNotificationTitle.context.getString(R.string.Date_, newsPortal.pdate)
            itemView.tvNotificationTitle.text = newsPortal.name
        }
    }
}