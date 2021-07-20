package com.sunnyside.kookoo.student.ui.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.sunnyside.kookoo.R
import com.sunnyside.kookoo.databinding.CardLayoutCalendarAnnouncementBinding
import com.sunnyside.kookoo.databinding.CardLayoutCalendarForecastBinding
import com.sunnyside.kookoo.student.model.CalendarAnnouncementModel
import com.sunnyside.kookoo.student.model.CalendarForecastModel

class CalendarListAdapter : RecyclerView.Adapter<CalendarListAdapter.BaseViewHolder<*>>(){
    private var calendarDataList: List<Any> = emptyList()

    sealed class BaseViewHolder<T>(open val itemBinding : ViewBinding) : RecyclerView.ViewHolder(itemBinding.root){
        abstract fun bind(item : T)
    }

    companion object {
        private const val TYPE_ANNOUNCEMENT = 0
        private const val TYPE_FORECAST = 1

    }

    inner class AnnouncementViewHolder(override val itemBinding: CardLayoutCalendarAnnouncementBinding) : BaseViewHolder<CalendarAnnouncementModel>(itemBinding){
        override fun bind(item : CalendarAnnouncementModel) {
            itemBinding.itemCalendarAnnouncementTitle.text = item.title
        }
    }

    inner class ForecastViewHolder(override val itemBinding: CardLayoutCalendarForecastBinding) : BaseViewHolder<CalendarForecastModel>(itemBinding){
        override fun bind(item : CalendarForecastModel) {
            itemBinding.itemCalendarForecastTitle.text = item.title
            itemBinding.itemCalendarForecastStatus.text = item.status

            if (item.status == "CANCELLED") {
                itemBinding.itemCalendarForecastStatus.setTextColor(Color.RED)
            } else if (item.status == "PENDING") {
                itemBinding.itemCalendarForecastStatus.setTextColor(Color.GRAY)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*> {
        return when (viewType) {
            TYPE_ANNOUNCEMENT -> {
                AnnouncementViewHolder(CardLayoutCalendarAnnouncementBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            }
            TYPE_FORECAST -> {
                ForecastViewHolder(CardLayoutCalendarForecastBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: BaseViewHolder<*>, position: Int) {
        val element = calendarDataList[position]
        when (holder) {
            is AnnouncementViewHolder -> holder.bind(element as CalendarAnnouncementModel)
            is ForecastViewHolder -> holder.bind(element as CalendarForecastModel)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (calendarDataList[position]) {
            is CalendarAnnouncementModel -> TYPE_ANNOUNCEMENT
            is CalendarForecastModel -> TYPE_FORECAST
            else -> throw IllegalArgumentException("Invalid type of data $position")
        }
    }

    override fun getItemCount(): Int {
        return calendarDataList.size
    }

    fun setData(events : List<Any>) {
        this.calendarDataList = events
        notifyDataSetChanged()
    }

}