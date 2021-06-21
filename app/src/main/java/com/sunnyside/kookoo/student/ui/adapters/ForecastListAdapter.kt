package com.sunnyside.kookoo.student.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sunnyside.kookoo.databinding.CardLayoutForecastBinding
import com.sunnyside.kookoo.student.model.ForecastModel
import com.sunnyside.kookoo.student.model.JoinedClassModel
import java.time.format.DateTimeFormatter

class ForecastListAdapter(private val listener: (ForecastModel) -> Unit) : RecyclerView.Adapter<ForecastListAdapter.MyViewHolder>() {
    private var forecastList = emptyList<ForecastModel>()

    class MyViewHolder(val itemBinding: CardLayoutForecastBinding) : RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(forecast: ForecastModel) {
            val meetingTimeFormat : DateTimeFormatter = DateTimeFormatter.ofPattern("EEE, d MMM yyyy h:mm a")
            itemBinding.itemTitleForecast.text = forecast.title
            itemBinding.itemStatusStateForecast.text = forecast.status
            itemBinding.itemOtherContentForecast.text = forecast.link
            itemBinding.itemDateForecast.text = forecast.meeting_time.format(meetingTimeFormat)
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

    fun setData(forecasts: List<ForecastModel>) {
        forecastList = forecasts
        notifyDataSetChanged()
    }
}