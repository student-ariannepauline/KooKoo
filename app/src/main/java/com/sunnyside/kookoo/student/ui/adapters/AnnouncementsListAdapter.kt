package com.sunnyside.kookoo.student.ui.adapters

import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.imageview.ShapeableImageView
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageException
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import com.sunnyside.kookoo.databinding.CardLayoutForecastBinding
import com.sunnyside.kookoo.databinding.CardLayoutHomeBinding
import com.sunnyside.kookoo.databinding.CardLayoutJoinedClassTestBinding
import com.sunnyside.kookoo.student.model.AnnouncementModel
import com.sunnyside.kookoo.student.ui.viewmodel.TimelineViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.format.DateTimeFormatter

class AnnouncementsListAdapter(
    val timelineViewModel: TimelineViewModel,
    val storage: FirebaseStorage
) : RecyclerView.Adapter<AnnouncementsListAdapter.MyViewHolder>() {
    private var announcementList = ArrayList<AnnouncementModel>()


    class MyViewHolder(val itemBinding: CardLayoutHomeBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {

        fun bind(announcement: AnnouncementModel) {
            val deadlineFormat: DateTimeFormatter = DateTimeFormatter.ofPattern("EEE, dd MMM yyyy")
            val timestampFormat: DateTimeFormatter =
                DateTimeFormatter.ofPattern("EEE, d MMM yyyy h:mm a")

            itemBinding.itemTitleHome.text = announcement.title
            itemBinding.itemBodyHome.text = announcement.body
            itemBinding.itemDeadlineHome.text =
                announcement.deadline.format(deadlineFormat).toString()
            itemBinding.itemOtherContentHome.text = announcement.link
            itemBinding.itemTimestampHome.text =
                announcement.timestamp.format(timestampFormat).toString()
            itemBinding.itemImgProfile


            try {
                val image = Firebase.storage.reference.child("/profilePicture/${announcement.pic_link}")

                image.downloadUrl
                    .addOnSuccessListener { url ->
                        Glide.with(itemBinding.root).load(url)
                            .into(itemBinding.itemImgProfile)
                    }

            } catch (e: Exception) {
                Log.d("Adapter", "walang ganyan lods")
            }

        }

    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        val itemBinding =
            CardLayoutHomeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
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