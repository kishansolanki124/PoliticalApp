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
import com.politics.politicalapp.pojo.QuizAndContestResponse
import kotlinx.android.synthetic.main.quiz_and_contest_item.view.*

class QuizAndContestAdapter(
    private val itemClickCall: (QuizAndContestResponse.Quiz) -> Unit,
    private val itemClickWeb: (QuizAndContestResponse.Quiz) -> Unit
) :
    RecyclerView.Adapter<QuizAndContestAdapter.HomeOffersViewHolder>() {

    private var list: ArrayList<QuizAndContestResponse.Quiz> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeOffersViewHolder {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.quiz_and_contest_item, parent, false)
        return HomeOffersViewHolder(
            view, itemClickCall, itemClickWeb
        )
    }

    override fun onBindViewHolder(holder: HomeOffersViewHolder, position: Int) {
        holder.bindForecast(list[position])
    }

    fun setItem(list: ArrayList<QuizAndContestResponse.Quiz>) {
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
        private val itemClickCall: (QuizAndContestResponse.Quiz) -> Unit,
        private val itemClickWeb: (QuizAndContestResponse.Quiz) -> Unit
    ) : RecyclerView.ViewHolder(view) {

//        constructor(parent: ViewGroup) : this(
//            LayoutInflater.from(parent.context).inflate(
//                R.layout.item_vatan_nu_gham,
//                parent, false
//            )
//        )

        @SuppressLint("SetTextI18n")
        fun bindForecast(
            newsPortal: QuizAndContestResponse.Quiz
        ) {
            with(newsPortal) {

                itemView.tvQuizAndContestTitle.text = newsPortal.name

                if (newsPortal.quiz_mode == "Quiz Winner") {
                    itemView.tvGive_rate_get_10_point.text = ""
                    itemView.btViewWinner.text = "check winner"
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

                    val yellowText =
                        SpannableString(itemView.tvGive_rate_get_10_point.context.getString(R.string.give_rate_get_10_point_2))
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
                        SpannableString(itemView.tvGive_rate_get_10_point.context.getString(R.string.points_and_win_prizes))
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

                    val forthText =
                        SpannableString(itemView.tvGive_rate_get_10_point.context.getString(R.string.points_and_win_prizes_2))
                    forthText.setSpan(
                        ForegroundColorSpan(
                            ContextCompat.getColor(
                                itemView.tvGive_rate_get_10_point.context,
                                R.color.red_CC252C
                            )
                        ),
                        0, forthText.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                    itemView.tvGive_rate_get_10_point.append(forthText)

                    ///(Participate &amp; Get 10 points / Win Prize)
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
                    if (itemView.btViewWinner.text.equals("running")) {
                        itemClickCall(this)
                    } else {
                        itemClickWeb(this)
                    }
                }

                itemView.btViewWinner.setOnClickListener {
                    if (itemView.btViewWinner.text.equals("check winner")) {
                        itemClickWeb(this)
                    }
                }
//
//                itemView.ivWeb.setOnClickListener {
//                    itemClickWeb(this)
//                }
            }
        }
    }
}