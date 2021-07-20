package com.sunnyside.kookoo.student.ui.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.color.MaterialColors.getColor
import com.sunnyside.kookoo.R
import com.sunnyside.kookoo.databinding.CardLayoutForecastBinding
import com.sunnyside.kookoo.student.model.ForecastModel
import com.sunnyside.kookoo.student.model.JoinedClassModel
import com.sunnyside.kookoo.student.ui.viewmodel.ForecastViewModel
import java.time.format.DateTimeFormatter

class ForecastListAdapter(val forecastViewModel: ForecastViewModel, private val listener: (ForecastModel) -> Unit) : RecyclerView.Adapter<ForecastListAdapter.MyViewHolder>() {
    private var forecastList = ArrayList<ForecastModel>()

    inner class MyViewHolder(val itemBinding: CardLayoutForecastBinding) : RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(forecast: ForecastModel) {
            val meetingTimeFormat : DateTimeFormatter = DateTimeFormatter.ofPattern("EEE, d MMM yyyy h:mm a")
            itemBinding.itemTitleForecast.text = forecast.title
            itemBinding.itemStatusStateForecast.text = forecast.status
            itemBinding.itemOtherContentForecast.text = forecast.link
            itemBinding.itemDateForecast.text = forecast.meeting_time.format(meetingTimeFormat)

            if (forecast.status == "CANCELLED") {
                itemBinding.itemStatusStateForecast.setTextColor(Color.RED)
            } else if (forecast.status == "PENDING") {
                itemBinding.itemStatusStateForecast.setTextColor(Color.GRAY)
            }

            itemBinding.root.setOnClickListener {
                listener.invoke(forecast)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemBinding = CardLayoutForecastBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = forecastList[position]
        holder.bind(currentItem)

    }

    override fun getItemCount(): Int {
        return forecastList.size
    }

    fun setData(forecasts: ArrayList<ForecastModel>) {
        forecastList = forecasts
        notifyDataSetChanged()
    }

    fun deleteItem(pos : Int) {
        val forecastToDelete: ForecastModel = forecastList[pos]
        forecastViewModel.deleteForecast(forecastToDelete.documentID)
        forecastList.removeAt(pos)

        notifyItemRemoved(pos)
    }
}