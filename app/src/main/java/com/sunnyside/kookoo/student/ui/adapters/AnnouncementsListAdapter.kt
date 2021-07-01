package com.sunnyside.kookoo.student.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sunnyside.kookoo.databinding.CardLayoutForecastBinding
import com.sunnyside.kookoo.databinding.CardLayoutHomeBinding
import com.sunnyside.kookoo.databinding.CardLayoutJoinedClassTestBinding
import com.sunnyside.kookoo.student.model.AnnouncementModel
import com.sunnyside.kookoo.student.ui.viewmodel.TimelineViewModel
import java.time.format.DateTimeFormatter

class AnnouncementsListAdapter(val timelineViewModel: TimelineViewModel) : RecyclerView.Adapter<AnnouncementsListAdapter.MyViewHolder>() {
    private var announcementList = ArrayList<AnnouncementModel>()

    class MyViewHolder(val itemBinding: CardLayoutHomeBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(announcement: AnnouncementModel) {
            val deadlineFormat : DateTimeFormatter = DateTimeFormatter.ofPattern("EEE, dd MMM yyyy")
            val timestampFormat : DateTimeFormatter = DateTimeFormatter.ofPattern("EEE, d MMM yyyy h:mm a")

            itemBinding.itemTitleHome.text = announcement.title
            itemBinding.itemBodyHome.text = announcement.body
            itemBinding.itemDeadlineHome.text = announcement.deadline.format(deadlineFormat).toString()
            itemBinding.itemOtherContentHome.text = announcement.link
            itemBinding.itemTimestampHome.text = announcement.timestamp.format(timestampFormat).toString()
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AnnouncementsListAdapter.MyViewHolder {
        val itemBinding = CardLayoutHomeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: AnnouncementsListAdapter.MyViewHolder, position: Int) {
        val currentItem = announcementList[position]
        holder.bind(currentItem)
    }

    override fun getItemCount(): Int {
        return announcementList.size
    }

    fun setData(announcements: ArrayList<AnnouncementModel>) {
        this.announcementList = announcements
        notifyDataSetChanged()
    }

    fun deleteItem(pos: Int) {
        val announcementToDelete: AnnouncementModel = announcementList[pos]
        timelineViewModel.deleteAnnouncement(announcementToDelete.documentID)
        announcementList.removeAt(pos)

        notifyItemRemoved(pos)
    }


}