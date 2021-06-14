package com.sunnyside.kookoo.student.ui.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sunnyside.kookoo.R
import com.sunnyside.kookoo.databinding.CardLayoutJoinedClassTestBinding
import com.sunnyside.kookoo.student.model.JoinedClassModel

class JoinedClassListAdapter(private val listener: (JoinedClassModel) -> Unit) : RecyclerView.Adapter<JoinedClassListAdapter.MyViewHolder>(){

    private var joinedClassesList = emptyList<JoinedClassModel>()

    class MyViewHolder(val itemBinding: CardLayoutJoinedClassTestBinding) : RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(joinedClass: JoinedClassModel) {
            itemBinding.joinedClassName.text = joinedClass.name
            itemBinding.joinedClassIsAdmin.text = if (joinedClass.is_admin) "Admin" else "Student"

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemBinding = CardLayoutJoinedClassTestBinding.inflate(LayoutInflater.from(parent.context), parent,false)
        return MyViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = joinedClassesList[position]
        holder.itemBinding.root.setOnClickListener { listener(currentItem)}
        holder.bind(currentItem)
    }

    override fun getItemCount(): Int {
        return joinedClassesList.size
    }

    fun setData(joinedClasses: List<JoinedClassModel>) {
        this.joinedClassesList = joinedClasses
        notifyDataSetChanged()
    }


}