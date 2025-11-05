package com.example.employeemanagementapp.viewmodel

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
            // Name & Department
            binding.tvName.text = emp.name ?: "No Name"
            binding.tvDepartment.text = emp.department ?: "No Department"

            // Profile Image with null check
            val imageUri = emp.imageUri
            if (!imageUri.isNullOrEmpty()) {
                binding.imgProfile.load(imageUri) {
                    placeholder(R.drawable.outline_person_24)
                    error(R.drawable.outline_person_24)
                    crossfade(true)
                    transformations(CircleCropTransformation())
                }
            } else {
                binding.imgProfile.setImageResource(R.drawable.outline_person_24)
            }

            // Click listeners
            binding.root.setOnClickListener { onItemClick(emp) }
            binding.btnMore.setOnClickListener { onEditClick(emp) }
            binding.root.setOnLongClickListener {
                onDeleteClick(emp)
                true
            }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<Employee>() {
        override fun areItemsTheSame(oldItem: Employee, newItem: Employee) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Employee, newItem: Employee) =
            oldItem == newItem
    }
}
