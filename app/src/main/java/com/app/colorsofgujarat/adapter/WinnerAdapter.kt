package com.app.colorsofgujarat.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.app.colorsofgujarat.R
import com.app.colorsofgujarat.databinding.WinnerItemBinding
import com.app.colorsofgujarat.pojo.WinnerListResponse

class WinnerAdapter(
    private val itemClickCall: (WinnerListResponse.PointsPrize, Boolean) -> Unit,
    private val itemClickWeb: (WinnerListResponse.PointsPrize) -> Unit
) :
    RecyclerView.Adapter<WinnerAdapter.HomeOffersViewHolder>() {

    private var list: ArrayList<WinnerListResponse.PointsPrize> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeOffersViewHolder {
//        val view =
//            LayoutInflater.from(parent.context)
//                .inflate(R.layout.winner_item, parent, false)
        val binding =
            WinnerItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return HomeOffersViewHolder(
            binding, itemClickCall, itemClickWeb
        )
    }

    override fun onBindViewHolder(holder: HomeOffersViewHolder, position: Int) {
        holder.bindForecast(list[position])
    }

    fun setItem(list: ArrayList<WinnerListResponse.PointsPrize>) {
        this.list = list
        notifyDataSetChanged()
    }

    fun reset() {
        this.list.clear()
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = list.size

    class HomeOffersViewHolder(
        private val binding: WinnerItemBinding,
        private val itemClickCall: (WinnerListResponse.PointsPrize, Boolean) -> Unit,
        private val itemClickWeb: (WinnerListResponse.PointsPrize) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bindForecast(
            newsPortal: WinnerListResponse.PointsPrize
        ) {
            with(newsPortal) {

                binding.tvWinnerTitle.text = newsPortal.name1 + "\n(" + newsPortal.name2 + ")"
                if (newsPortal.prize_mode == "Points Winner") {
                    binding.btViewWinner.text =
                        binding.btViewWinner.context.getString(R.string.Check_Winner)
                    binding.btViewWinner.setBackgroundColor(
                        ContextCompat.getColor(
                            binding.btViewWinner.context, R.color.gray_BEBEBE
                        )
                    )
                } else {
                    binding.btViewWinner.text =
                        binding.btViewWinner.context.getString(R.string.Prize_Details)
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
//                binding.cvRootGovtWorkNewsItem.setOnClickListener {
//                    if (binding.btViewWinner.text.equals("result")) {
//                        itemClickCall(this, false)
//                    } else {
//                        itemClickCall(this, true)
//                    }
//
//                }

                binding.btViewWinner.setOnClickListener {
                    if (binding.btViewWinner.text.equals(
                            binding.btViewWinner.context.getString(R.string.Check_Winner)
                        )
                    ) {
                        itemClickWeb(this)
                    } else {
                        itemClickCall(this, false)
                    }
                }
//
//                binding.ivWeb.setOnClickListener {
//                    itemClickWeb(this)
//                }
            }
        }
    }
}