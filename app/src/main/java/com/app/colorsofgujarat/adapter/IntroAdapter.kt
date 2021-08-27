package com.app.colorsofgujarat.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.colorsofgujarat.databinding.IntroItemBinding
import com.app.colorsofgujarat.pojo.SettingsResponse
import com.bumptech.glide.Glide

class IntroAdapter(private val itemClick: (SettingsResponse.WelcomeBanner) -> Unit) :
    RecyclerView.Adapter<IntroAdapter.HomeOffersViewHolder>() {

    private var list: List<SettingsResponse.WelcomeBanner> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeOffersViewHolder {
        val binding =
            IntroItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )

        return HomeOffersViewHolder(
            binding
        )
    }

    override fun onBindViewHolder(holder: HomeOffersViewHolder, position: Int) {
        holder.bind(list[position], itemClick)
    }

    fun setItem(list: List<SettingsResponse.WelcomeBanner>) {
        this.list = list
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = list.size

    class HomeOffersViewHolder(
        private val binding: IntroItemBinding,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(
            introImageModel: SettingsResponse.WelcomeBanner,
            itemClick: (SettingsResponse.WelcomeBanner) -> Unit
        ) {
//            val circularProgressDrawable = CircularProgressDrawable(itemView.introImage.context)
//            circularProgressDrawable.strokeWidth = 5f
//            circularProgressDrawable.centerRadius = 30f
//            circularProgressDrawable.start()
//
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
//                circularProgressDrawable.colorFilter = BlendModeColorFilter(
//                    ContextCompat.getColor(
//                        itemView.introImage.context,
//                        R.color.pink
//                    ), BlendMode.SRC_ATOP
//                )
//            } else {
//                circularProgressDrawable.setColorFilter(
//                    ContextCompat.getColor(
//                        itemView.introImage.context,
//                        R.color.pink
//                    ), PorterDuff.Mode.SRC_ATOP
//                )
//            }

            //circularProgressDrawable.start()

            itemView.setOnClickListener {
                itemClick(introImageModel)
            }

            Glide.with(binding.introImage.context)
                .load(introImageModel.up_pro_img)
                //.error(R.drawable.error_load)
                //.placeholder(circularProgressDrawable)
                .into(binding.introImage)
        }
    }
}