package com.app.colorsofgujarat.adapter

import android.annotation.SuppressLint
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.app.colorsofgujarat.R
import com.app.colorsofgujarat.apputils.getPollPoints
import com.app.colorsofgujarat.databinding.LivePollItemBinding
import com.app.colorsofgujarat.pojo.LivePollListResponse

class LivePollAdapter(
    private val itemClickCall: (LivePollListResponse.LivePoll, Boolean) -> Unit,
    private val itemClickShare: (LivePollListResponse.LivePoll) -> Unit,
    private val itemClickWeb: (LivePollListResponse.LivePoll) -> Unit
) :
    RecyclerView.Adapter<LivePollAdapter.HomeOffersViewHolder>() {

    private var list: ArrayList<LivePollListResponse.LivePoll> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeOffersViewHolder {
//        val view =
//            LayoutInflater.from(parent.context)
//                .inflate(R.layout.live_poll_item, parent, false)
        val binding =
            LivePollItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )

        return HomeOffersViewHolder(
            binding, itemClickCall, itemClickShare, itemClickWeb
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
        private val binding: LivePollItemBinding,
        private val itemClickCall: (LivePollListResponse.LivePoll, Boolean) -> Unit,
        private val itemClickShare: (LivePollListResponse.LivePoll) -> Unit,
        private val itemClickWeb: (LivePollListResponse.LivePoll) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bindForecast(
            newsPortal: LivePollListResponse.LivePoll
        ) {
            with(newsPortal) {

                binding.tvQuestionSuggestion.text = newsPortal.name

                if (newsPortal.poll_status == "Result") {
                    binding.tvGiveRateGet10Point.text = ""
                    binding.btViewWinner.text = "result"

                    binding.tvVotesTotal.text = "Votes: " + newsPortal.votes
                    binding.btViewWinner.setBackgroundColor(
                        ContextCompat.getColor(
                            binding.btViewWinner.context, R.color.gray_BEBEBE
                        )
                    )
                } else {
                    binding.tvGiveRateGet10Point.text =
                        "(Participate &amp; Get 10 points / Win Prize)"

                    val greenText =
                        SpannableString(binding.tvGiveRateGet10Point.context.getString(R.string.participate_and))
                    greenText.setSpan(
                        ForegroundColorSpan(
                            ContextCompat.getColor(
                                binding.tvGiveRateGet10Point.context,
                                R.color.black
                            )
                        ),
                        0, greenText.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                    binding.tvGiveRateGet10Point.text = greenText

//                    val yellowText =
//                        SpannableString(binding.tvGiveRateGet10Point.context.getString(R.string.give_rate_get_10_point_2))

                    val yellowText =
                        SpannableString(binding.tvGiveRateGet10Point.context.getPollPoints())

                    yellowText.setSpan(
                        ForegroundColorSpan(
                            ContextCompat.getColor(
                                binding.tvGiveRateGet10Point.context,
                                R.color.red_CC252C
                            )
                        ),
                        0, yellowText.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                    )

                    binding.tvGiveRateGet10Point.append(yellowText)

                    val thirdText =
                        SpannableString(binding.tvGiveRateGet10Point.context.getString(R.string.give_rate_get_10_point_3))
                    thirdText.setSpan(
                        ForegroundColorSpan(
                            ContextCompat.getColor(
                                binding.tvGiveRateGet10Point.context,
                                R.color.black
                            )
                        ),
                        0, thirdText.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                    binding.tvGiveRateGet10Point.append(thirdText)

                    binding.tvVotesTotal.text = ""

                    binding.btViewWinner.text = "running"
                    binding.btViewWinner.setBackgroundColor(
                        ContextCompat.getColor(
                            binding.btViewWinner.context, R.color.red_CC252C
                        )
                    )
                }

//                Glide.with(binding.ivNewsPortal.context)
//                    .load(newsPortal.up_pro_img)
//                    .into(binding.ivNewsPortal)
//
//                if (newsPortal.name.isNullOrEmpty()) {
//                    binding.tvNewsPortalTitle.visibility = View.GONE
//                }
//                if (newsPortal.address.isNullOrEmpty()) {
//                    binding.llNewsPortalAddress.visibility = View.GONE
//                }
//                if (newsPortal.contact_no.isNullOrEmpty()) {
//                    binding.llNewsPortalPhone.visibility = View.GONE
//                }
//                if (newsPortal.email.isNullOrEmpty()) {
//                    binding.llNewsPortalEmail.visibility = View.GONE
//                }
//                if (newsPortal.website.isNullOrEmpty()) {
//                    binding.llNewsPortalWebsite.visibility = View.GONE
//                }
//
//                binding.tvNewsPortalTitle.text = newsPortal.name
//                binding.tvNewsPortalAddress.text = newsPortal.address
//                binding.tvNewsPortalPhone.text = newsPortal.contact_no
//                binding.tvNewsPortalEmail.text = newsPortal.email
//                binding.tvNewsPortalWebsite.text = newsPortal.website
//
                binding.cvRootGovtWorkNewsItem.setOnClickListener {
                    if (binding.btViewWinner.text.equals("result")) {
                        itemClickCall(this, false)
                    } else {
                        itemClickCall(this, true)
                    }

                }

                binding.btViewWinner.setOnClickListener {
                    if (binding.btViewWinner.text.equals("result")) {
                        itemClickCall(this, false)
                    } else {
                        itemClickCall(this, true)
                    }
                }

                binding.ivLivePollShare.setOnClickListener {
                    itemClickShare(this)
                }
//
//                binding.ivWeb.setOnClickListener {
//                    itemClickWeb(this)
//                }
            }
        }
    }
}