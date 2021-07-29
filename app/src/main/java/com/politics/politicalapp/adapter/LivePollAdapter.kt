package com.politics.politicalapp.adapter

import android.annotation.SuppressLint
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.politics.politicalapp.R
import com.politics.politicalapp.apputils.getPollPoints
import com.politics.politicalapp.pojo.LivePollListResponse
import kotlinx.android.synthetic.main.live_poll_item.view.*

class LivePollAdapter(
    private val itemClickCall: (LivePollListResponse.LivePoll, Boolean) -> Unit,
    private val itemClickShare: (LivePollListResponse.LivePoll) -> Unit,
    private val itemClickWeb: (LivePollListResponse.LivePoll) -> Unit
) :
    RecyclerView.Adapter<LivePollAdapter.HomeOffersViewHolder>() {

    private var list: ArrayList<LivePollListResponse.LivePoll> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeOffersViewHolder {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.live_poll_item, parent, false)
        return HomeOffersViewHolder(
            view, itemClickCall, itemClickShare, itemClickWeb
        )
    }

    override fun onBindViewHolder(holder: HomeOffersViewHolder, position: Int) {
        holder.bindForecast(list[position])
    }

    fun setItem(list: ArrayList<LivePollListResponse.LivePoll>) {
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
        private val itemClickCall: (LivePollListResponse.LivePoll, Boolean) -> Unit,
        private val itemClickShare: (LivePollListResponse.LivePoll) -> Unit,
        private val itemClickWeb: (LivePollListResponse.LivePoll) -> Unit
    ) : RecyclerView.ViewHolder(view) {

        @SuppressLint("SetTextI18n")
        fun bindForecast(
            newsPortal: LivePollListResponse.LivePoll
        ) {
            with(newsPortal) {

                itemView.tvQuestionSuggestion.text = newsPortal.name

                if (newsPortal.poll_status == "Result") {
                    itemView.tvGive_rate_get_10_point.text = ""
                    itemView.btViewWinner.text = "result"
                    itemView.btViewWinner.setBackgroundColor(
                        ContextCompat.getColor(
                            itemView.btViewWinner.context, R.color.gray_BEBEBE
                        )
                    )
                } else {
                    itemView.tvGive_rate_get_10_point.text =
                        "(Participate &amp; Get 10 points / Win Prize)"

                    val greenText =
                        SpannableString(itemView.tvGive_rate_get_10_point.context.getString(R.string.participate_and))
                    greenText.setSpan(
                        ForegroundColorSpan(
                            ContextCompat.getColor(
                                itemView.tvGive_rate_get_10_point.context,
                                R.color.black
                            )
                        ),
                        0, greenText.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                    itemView.tvGive_rate_get_10_point.text = greenText

//                    val yellowText =
//                        SpannableString(itemView.tvGive_rate_get_10_point.context.getString(R.string.give_rate_get_10_point_2))

                    val yellowText = SpannableString(itemView.tvGive_rate_get_10_point.context.getPollPoints())

                    yellowText.setSpan(
                        ForegroundColorSpan(
                            ContextCompat.getColor(
                                itemView.tvGive_rate_get_10_point.context,
                                R.color.red_CC252C
                            )
                        ),
                        0, yellowText.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                    )

                    itemView.tvGive_rate_get_10_point.append(yellowText)

                    val thirdText =
                        SpannableString(itemView.tvGive_rate_get_10_point.context.getString(R.string.give_rate_get_10_point_3))
                    thirdText.setSpan(
                        ForegroundColorSpan(
                            ContextCompat.getColor(
                                itemView.tvGive_rate_get_10_point.context,
                                R.color.black
                            )
                        ),
                        0, thirdText.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                    itemView.tvGive_rate_get_10_point.append(thirdText)


                    itemView.btViewWinner.text = "running"
                    itemView.btViewWinner.setBackgroundColor(
                        ContextCompat.getColor(
                            itemView.btViewWinner.context, R.color.red_CC252C
                        )
                    )
                }

//                Glide.with(itemView.ivNewsPortal.context)
//                    .load(newsPortal.up_pro_img)
//                    .into(itemView.ivNewsPortal)
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
                    if (itemView.btViewWinner.text.equals("result")) {
                        itemClickCall(this, false)
                    } else {
                        itemClickCall(this, true)
                    }

                }

                itemView.btViewWinner.setOnClickListener {
                    if (itemView.btViewWinner.text.equals("result")) {
                        itemClickWeb(this)
                    }
                }

                itemView.ivLivePollShare.setOnClickListener {
                    itemClickShare(this)
                }
//
//                itemView.ivWeb.setOnClickListener {
//                    itemClickWeb(this)
//                }
            }
        }
    }
}