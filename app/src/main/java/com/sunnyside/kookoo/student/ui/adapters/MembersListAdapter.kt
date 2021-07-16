package com.sunnyside.kookoo.student.ui.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.sunnyside.kookoo.databinding.CardLayoutForecastBinding
import com.sunnyside.kookoo.databinding.CardLayoutMembersDisplayBinding
import com.sunnyside.kookoo.student.model.ForecastModel
import com.sunnyside.kookoo.student.model.MemberModel

class MembersListAdapter (private val listener: (MemberModel) -> Unit) : RecyclerView.Adapter<MembersListAdapter.MyViewHolder>() {
    private var membersList = ArrayList<MemberModel>()

    class MyViewHolder(val itemBinding: CardLayoutMembersDisplayBinding) : RecyclerView.ViewHolder(itemBinding.root)  {
        fun bind(member : MemberModel) {
            itemBinding.memberTxtUsername.text = member.name
            itemBinding.memberTxtGradeLevel.text = "${member.program}"

            try {
                val image = Firebase.storage.reference.child("/profilePicture/${member.picLink}")

                image.downloadUrl
                    .addOnSuccessListener { url ->
                        Glide.with(itemBinding.root).load(url)
                            .into(itemBinding.memberImgProfile)
                    }

            } catch (e: Exception) {
                Log.d("Adapter", "walang ganyan lods")
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemBinding = CardLayoutMembersDisplayBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = membersList[position]
        holder.bind(currentItem)
    }

    override fun getItemCount(): Int {
        return membersList.size
    }

    fun setData(members : ArrayList<MemberModel>) {
        membersList = members
        notifyDataSetChanged()
    }

}