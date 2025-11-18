package com.example.employeemanagementapp.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.employeemanagementapp.R
import com.example.employeemanagementapp.data.Department

class DepartmentAdapter(
    private val onItemClick: (Department) -> Unit
) : ListAdapter<Department, DepartmentAdapter.DepartmentViewHolder>(DepartmentDiffCallback()) {

    inner class DepartmentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgDepartmentIcon: ImageView = itemView.findViewById(R.id.imgDepartmentIcon)
        val tvDepartmentName: TextView = itemView.findViewById(R.id.tvDepartmentName)
        val tvTechieCount: TextView = itemView.findViewById(R.id.tvTechieCount)

        fun bind(department: Department) {
            tvDepartmentName.text = department.name

            // আইকন রিসোর্স আইডি ব্যবহার করে ImageView সেট করা
            imgDepartmentIcon.setImageResource(department.iconResId)

            // টেকনিশিয়ান কাউন্ট সেট করা
            tvTechieCount.text = "${department.techieCount} techies"

            itemView.setOnClickListener {
                onItemClick(department)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DepartmentViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_department_card, parent, false)
        return DepartmentViewHolder(view)
    }

    override fun onBindViewHolder(holder: DepartmentViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class DepartmentDiffCallback : DiffUtil.ItemCallback<Department>() {
        override fun areItemsTheSame(oldItem: Department, newItem: Department): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Department, newItem: Department): Boolean {
            return oldItem == newItem
        }
    }
}