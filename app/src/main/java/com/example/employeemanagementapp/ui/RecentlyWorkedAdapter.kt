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
import com.example.employeemanagementapp.data.RecentlyWorked

class RecentlyWorkedAdapter(
    private val onItemClick: (RecentlyWorked) -> Unit
) : ListAdapter<RecentlyWorked, RecentlyWorkedAdapter.RecentViewHolder>(RecentDiffCallback()) {

    inner class RecentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvName: TextView = itemView.findViewById(R.id.tvEmployeeName)
        val tvRole: TextView = itemView.findViewById(R.id.tvEmployeeRole)
        val imgProfile: ImageView = itemView.findViewById(R.id.imgEmployeeProfile)
        val imgMore: ImageView? = itemView.findViewById(R.id.imgMore)

        fun bind(recent: RecentlyWorked) {
            tvName.text = recent.employeeName
            tvRole.text = recent.employeeRole

            // Ensure Click Listener handles null imgMore gracefully
            // If imgMore exists, set listener on imgMore; otherwise, set on the whole item view
            val clickableElement: View = imgMore ?: itemView

            clickableElement.setOnClickListener {
                onItemClick(recent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecentViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_recent_employee, parent, false)
        return RecentViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecentViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class RecentDiffCallback : DiffUtil.ItemCallback<RecentlyWorked>() {
        override fun areItemsTheSame(oldItem: RecentlyWorked, newItem: RecentlyWorked): Boolean {
            return oldItem.employeeId == newItem.employeeId
        }

        override fun areContentsTheSame(oldItem: RecentlyWorked, newItem: RecentlyWorked): Boolean {
            return oldItem == newItem
        }
    }
}