package com.example.employeemanagementapp.viewmodel

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.employeemanagementapp.data.Employee

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
            binding.tvName.text = emp.name
            binding.tvDepartment.text = emp.department

            if (emp.imageUri.isNotEmpty()) {
                binding.imgProfile.load(emp.imageUri) {
                    placeholder(R.drawable.ic_person_placeholder)
                    error(R.drawable.ic_person_placeholder)
                }
            } else {
                binding.imgProfile.setImageResource(R.drawable.ic_person_placeholder)
            }

            binding.root.setOnClickListener { onItemClick(emp) }
            binding.btnMore.setOnClickListener { onEditClick(emp) }
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