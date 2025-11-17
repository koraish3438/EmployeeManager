package com.example.employeemanagementapp.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import com.example.employeemanagementapp.R
import com.example.employeemanagementapp.data.Employee
import com.example.employeemanagementapp.databinding.ItemEmployeeBinding

class EmployeeAdapter(
    private val currentUserId: String,
    private val onItemClick: (Employee) -> Unit,
    private val onEditClick: (Employee) -> Unit,
    private val onDeleteClick: (Employee) -> Unit
) : ListAdapter<Employee, EmployeeAdapter.EmployeeVH>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EmployeeVH {
        val binding = ItemEmployeeBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return EmployeeVH(binding)
    }

    override fun onBindViewHolder(holder: EmployeeVH, position: Int) {
        holder.bind(getItem(position))
    }

    inner class EmployeeVH(private val binding: ItemEmployeeBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(emp: Employee) {
            // Name
            binding.tvName.text = emp.name

            // Role as department (optional: replace with actual department name via lookup)
            binding.tvDepartment.text = emp.role

            // Profile image
            val imageUri = emp.profileUrl
            if (!imageUri.isNullOrEmpty()) {
                binding.ivProfile.load(imageUri) {
                    placeholder(R.drawable.outline_person_24)
                    error(R.drawable.outline_person_24)
                    transformations(CircleCropTransformation())
                }
            } else {
                binding.ivProfile.setImageResource(R.drawable.outline_person_24)
            }

            // Click listeners
            binding.root.setOnClickListener { onItemClick(emp) }
            binding.ivEdit.setOnClickListener { onEditClick(emp) }
            binding.root.setOnLongClickListener {
                onDeleteClick(emp)
                true
            }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<Employee>() {
        override fun areItemsTheSame(oldItem: Employee, newItem: Employee) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Employee, newItem: Employee) = oldItem == newItem
    }
}
