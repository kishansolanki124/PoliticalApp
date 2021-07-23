package com.politics.politicalapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.politics.politicalapp.R
import com.politics.politicalapp.pojo.DistrictPollListResponse
import kotlinx.android.synthetic.main.poll_and_survey_item.view.*

class PollAndSurveyAdapter(
    private val itemClickCall: (DistrictPollListResponse.Poll) -> Unit,
    private val itemClickWeb: (DistrictPollListResponse.Poll) -> Unit
) :
    RecyclerView.Adapter<PollAndSurveyAdapter.HomeOffersViewHolder>() {

    private var list: ArrayList<DistrictPollListResponse.Poll> = ArrayList()
    private var pollOptionList: ArrayList<DistrictPollListResponse.PollOption> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeOffersViewHolder {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.poll_and_survey_item, parent, false)
        return HomeOffersViewHolder(
            view, itemClickCall, itemClickWeb
        )
    }

    override fun onBindViewHolder(holder: HomeOffersViewHolder, position: Int) {
        holder.bindForecast(list[position], pollOptionList)
    }

    fun setItem(
        list: ArrayList<DistrictPollListResponse.Poll>,
        pollOptionList: ArrayList<DistrictPollListResponse.PollOption>
    ) {
        this.list.addAll(list)
        this.pollOptionList.addAll(pollOptionList)
        notifyDataSetChanged()
    }

    fun reset() {
        this.list.clear()
        this.pollOptionList.clear()
        notifyDataSetChanged()
    }

    fun getPollList(): ArrayList<DistrictPollListResponse.Poll> {
        return this.list
    }

    override fun getItemCount(): Int = list.size

    class HomeOffersViewHolder(
        view: View,
        private val itemClickCall: (DistrictPollListResponse.Poll) -> Unit,
        private val itemClickWeb: (DistrictPollListResponse.Poll) -> Unit
    ) : RecyclerView.ViewHolder(view) {

//        constructor(parent: ViewGroup) : this(
//            LayoutInflater.from(parent.context).inflate(
//                R.layout.item_vatan_nu_gham,
//                parent, false
//            )
//        )

        fun bindForecast(
            newsPortal: DistrictPollListResponse.Poll,
            pollOption: ArrayList<DistrictPollListResponse.PollOption>
        ) {
            itemView.tvPollAndSurveyQuestion.text = newsPortal.name

            itemView.rbExcellent.text = pollOption[0].option_name
            itemView.rbGood.text = pollOption[1].option_name
            itemView.rbcantAnswer.text = pollOption[2].option_name
            itemView.rbBad.text = pollOption[3].option_name
            itemView.rbVeryBad.text = pollOption[4].option_name

            itemView.rbExcellent.setOnClickListener {
                newsPortal.checkedRadioId = pollOption[0].option_id
                resetRadioButtons()
                itemView.rbExcellent.isChecked = !itemView.rbExcellent.isChecked
            }

            itemView.rbGood.setOnClickListener {
                newsPortal.checkedRadioId = pollOption[1].option_id
                resetRadioButtons()
                itemView.rbGood.isChecked = !itemView.rbGood.isChecked
            }

            itemView.rbcantAnswer.setOnClickListener {
                newsPortal.checkedRadioId = pollOption[2].option_id
                resetRadioButtons()
                itemView.rbcantAnswer.isChecked = !itemView.rbcantAnswer.isChecked
            }

            itemView.rbBad.setOnClickListener {
                newsPortal.checkedRadioId = pollOption[3].option_id
                resetRadioButtons()
                itemView.rbBad.isChecked = !itemView.rbBad.isChecked
            }

            itemView.rbVeryBad.setOnClickListener {
                newsPortal.checkedRadioId = pollOption[4].option_id
                resetRadioButtons()
                itemView.rbVeryBad.isChecked = !itemView.rbVeryBad.isChecked
            }

            if (newsPortal.user_rating.isNotEmpty()) {
                if (newsPortal.user_rating == pollOption[0].option_id) {
                    resetRadioButtons()
                    itemView.rbExcellent.isChecked = true
                }

                if (newsPortal.user_rating == pollOption[1].option_id) {
                    resetRadioButtons()
                    itemView.rbGood.isChecked = true
                }

                if (newsPortal.user_rating == pollOption[2].option_id) {
                    resetRadioButtons()
                    itemView.rbcantAnswer.isChecked = true
                }

                if (newsPortal.user_rating == pollOption[3].option_id) {
                    resetRadioButtons()
                    itemView.rbBad.isChecked = true
                }

                if (newsPortal.user_rating == pollOption[4].option_id) {
                    resetRadioButtons()
                    itemView.rbVeryBad.isChecked = true
                }
            } else {
                resetRadioButtons()
            }
        }

        private fun resetRadioButtons() {
            itemView.rbExcellent.isChecked = false
            itemView.rbGood.isChecked = false
            itemView.rbcantAnswer.isChecked = false
            itemView.rbBad.isChecked = false
            itemView.rbVeryBad.isChecked = false
        }
    }
}