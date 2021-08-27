package com.app.colorsofgujarat.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.colorsofgujarat.databinding.PollAndSurveyItemBinding
import com.app.colorsofgujarat.pojo.DistrictPollListResponse

class PollAndSurveyAdapter(
    private val itemClickCall: (DistrictPollListResponse.Poll) -> Unit,
    private val itemClickWeb: (DistrictPollListResponse.Poll) -> Unit
) :
    RecyclerView.Adapter<PollAndSurveyAdapter.HomeOffersViewHolder>() {

    private var list: ArrayList<DistrictPollListResponse.Poll> = ArrayList()
    private var pollOptionList: ArrayList<DistrictPollListResponse.PollOption> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeOffersViewHolder {
//        val view =
//            LayoutInflater.from(parent.context)
//                .inflate(R.layout.poll_and_survey_item, parent, false)
        val binding =
            PollAndSurveyItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return HomeOffersViewHolder(
            binding, itemClickCall, itemClickWeb
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
        private val binding: PollAndSurveyItemBinding,
        private val itemClickCall: (DistrictPollListResponse.Poll) -> Unit,
        private val itemClickWeb: (DistrictPollListResponse.Poll) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

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
            binding.tvPollAndSurveyQuestion.text = newsPortal.name

            binding.rbExcellent.text = pollOption[0].option_name
            binding.rbGood.text = pollOption[1].option_name
            binding.rbcantAnswer.text = pollOption[2].option_name
            binding.rbBad.text = pollOption[3].option_name
            binding.rbVeryBad.text = pollOption[4].option_name

            binding.rbExcellent.setOnClickListener {
                newsPortal.checkedRadioId = pollOption[0].option_id
                resetRadioButtons()
                binding.rbExcellent.isChecked = !binding.rbExcellent.isChecked
            }

            binding.rbGood.setOnClickListener {
                newsPortal.checkedRadioId = pollOption[1].option_id
                resetRadioButtons()
                binding.rbGood.isChecked = !binding.rbGood.isChecked
            }

            binding.rbcantAnswer.setOnClickListener {
                newsPortal.checkedRadioId = pollOption[2].option_id
                resetRadioButtons()
                binding.rbcantAnswer.isChecked = !binding.rbcantAnswer.isChecked
            }

            binding.rbBad.setOnClickListener {
                newsPortal.checkedRadioId = pollOption[3].option_id
                resetRadioButtons()
                binding.rbBad.isChecked = !binding.rbBad.isChecked
            }

            binding.rbVeryBad.setOnClickListener {
                newsPortal.checkedRadioId = pollOption[4].option_id
                resetRadioButtons()
                binding.rbVeryBad.isChecked = !binding.rbVeryBad.isChecked
            }

            if (newsPortal.user_rating.isNotEmpty()) {
                if (newsPortal.user_rating == pollOption[0].option_id) {
                    resetRadioButtons()
                    binding.rbExcellent.isChecked = true
                }

                if (newsPortal.user_rating == pollOption[1].option_id) {
                    resetRadioButtons()
                    binding.rbGood.isChecked = true
                }

                if (newsPortal.user_rating == pollOption[2].option_id) {
                    resetRadioButtons()
                    binding.rbcantAnswer.isChecked = true
                }

                if (newsPortal.user_rating == pollOption[3].option_id) {
                    resetRadioButtons()
                    binding.rbBad.isChecked = true
                }

                if (newsPortal.user_rating == pollOption[4].option_id) {
                    resetRadioButtons()
                    binding.rbVeryBad.isChecked = true
                }
                radioButtonenableStatus(false)
            } else {
                resetRadioButtons()
                radioButtonenableStatus(true)
            }
        }

        private fun resetRadioButtons() {
            binding.rbExcellent.isChecked = false
            binding.rbGood.isChecked = false
            binding.rbcantAnswer.isChecked = false
            binding.rbBad.isChecked = false
            binding.rbVeryBad.isChecked = false
        }

        private fun radioButtonenableStatus(isenable: Boolean) {
            binding.rbExcellent.isEnabled = isenable
            binding.rbGood.isEnabled = isenable
            binding.rbcantAnswer.isEnabled = isenable
            binding.rbBad.isEnabled = isenable
            binding.rbVeryBad.isEnabled = isenable
        }
    }
}