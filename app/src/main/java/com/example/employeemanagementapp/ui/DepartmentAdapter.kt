package com.example.employeemanagementapp.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.RoundedCornersTransformation
import com.example.employeemanagementapp.R
import com.example.employeemanagementapp.data.Department
import com.example.employeemanagementapp.databinding.ItemDepartmentCardBinding

class DepartmentAdapter(
    private val onItemClick: (Department) -> Unit
) : ListAdapter<Department, DepartmentAdapter.DepartmentVH>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DepartmentVH {
        val binding = ItemDepartmentCardBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return DepartmentVH(binding)
    }

    override fun onBindViewHolder(holder: DepartmentVH, position: Int) {
        holder.bind(getItem(position))
    }

    inner class DepartmentVH(private val binding: ItemDepartmentCardBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(dept: Department) {
            binding.tvDepartmentName.text = dept.name ?: "No Name"

            // Optional: department icon/image if available
            binding.imgDepartmentIcon.load(dept.imageUri ?: "") {
                placeholder(R.drawable.outline_person_24)
                error(R.drawable.outline_person_24)
                transformations(RoundedCornersTransformation(12f))
            }

            binding.root.setOnClickListener { onItemClick(dept) }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<Department>() {
        override fun areItemsTheSame(oldItem: Department, newItem: Department) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Department, newItem: Department) = oldItem == newItem
    }
}
