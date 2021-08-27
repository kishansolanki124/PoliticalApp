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
import com.app.colorsofgujarat.databinding.QuizAndContestItemBinding
import com.app.colorsofgujarat.pojo.QuizAndContestResponse

class QuizAndContestAdapter(
    private val itemClickCall: (QuizAndContestResponse.Quiz) -> Unit,
    private val itemClickShare: (QuizAndContestResponse.Quiz) -> Unit,
    private val itemClickWeb: (QuizAndContestResponse.Quiz) -> Unit
) :
    RecyclerView.Adapter<QuizAndContestAdapter.HomeOffersViewHolder>() {

    private var list: ArrayList<QuizAndContestResponse.Quiz> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeOffersViewHolder {
//        val view =
//            LayoutInflater.from(parent.context)
//                .inflate(R.layout.quiz_and_contest_item, parent, false)
        val binding =
            QuizAndContestItemBinding.inflate(
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
        private val binding: QuizAndContestItemBinding,
        private val itemClickCall: (QuizAndContestResponse.Quiz) -> Unit,
        private val itemClickShare: (QuizAndContestResponse.Quiz) -> Unit,
        private val itemClickWeb: (QuizAndContestResponse.Quiz) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

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

                binding.tvQuizAndContestTitle.text = newsPortal.name

                if (newsPortal.quiz_mode == "Quiz Winner") {
                    binding.tvGiveRateGet10Point.text = ""
                    binding.btViewWinner.text = "check winner"
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
                        SpannableString(binding.tvGiveRateGet10Point.context.getString(R.string.points_and_win_prizes))
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

                    val forthText =
                        SpannableString(binding.tvGiveRateGet10Point.context.getString(R.string.points_and_win_prizes_2))
                    forthText.setSpan(
                        ForegroundColorSpan(
                            ContextCompat.getColor(
                                binding.tvGiveRateGet10Point.context,
                                R.color.red_CC252C
                            )
                        ),
                        0, forthText.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                    binding.tvGiveRateGet10Point.append(forthText)

                    ///(Participate &amp; Get 10 points / Win Prize)
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
                    if (binding.btViewWinner.text.equals("running")) {
                        itemClickCall(this)
                    } else {
                        itemClickWeb(this)
                    }
                }

                binding.btViewWinner.setOnClickListener {
                    if (binding.btViewWinner.text.equals("check winner")) {
                        itemClickWeb(this)
                    } else {
                        itemClickCall(this)
                    }
                }

                binding.ivShareQuizAndContest.setOnClickListener {
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