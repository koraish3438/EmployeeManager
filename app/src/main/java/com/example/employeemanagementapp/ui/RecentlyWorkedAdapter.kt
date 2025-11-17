package com.example.employeemanagementapp.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import com.example.employeemanagementapp.R
import com.example.employeemanagementapp.data.RecentlyWorked
import com.example.employeemanagementapp.databinding.ItemRecentEmployeeBinding

class RecentlyWorkedAdapter(
    private val onItemClick: (RecentlyWorked) -> Unit
) : ListAdapter<RecentlyWorked, RecentlyWorkedAdapter.RecentVH>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecentVH {
        val binding = ItemRecentEmployeeBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return RecentVH(binding)
    }

    override fun onBindViewHolder(holder: RecentVH, position: Int) {
        holder.bind(getItem(position))
    }

    inner class RecentVH(private val binding: ItemRecentEmployeeBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(recent: RecentlyWorked) {
            binding.tvEmployeeName.text = recent.employeeName ?: "No Name"
            binding.tvEmployeeRole.text = recent.employeeRole ?: "No Role"

            binding.imgEmployeeProfile.load(recent.employeeImageUri ?: "") {
                placeholder(R.drawable.outline_person_24)
                error(R.drawable.outline_person_24)
                transformations(CircleCropTransformation())
            }

            binding.root.setOnClickListener { onItemClick(recent) }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<RecentlyWorked>() {
        override fun areItemsTheSame(oldItem: RecentlyWorked, newItem: RecentlyWorked) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: RecentlyWorked, newItem: RecentlyWorked) = oldItem == newItem
    }
}
