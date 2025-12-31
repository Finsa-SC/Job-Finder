package com.example.gawe17.explore

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Adapter
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.gawe17.R
import com.example.gawe17.databinding.ExploreItemJobBinding
import com.example.gawe17.model.JobCardMode
import com.example.gawe17.model.JobList

class JobAdapter(
    private val jobs: List<JobList>,
    private val mode: JobCardMode,
    private val favoriteIds: Set<Int>,
    private val onApply: (JobList) -> Unit,
    private val onMark: (JobList) -> Unit,
    private val onCard: (JobList) ->Unit,
): RecyclerView.Adapter<JobAdapter.ViewHolder>(){

    class ViewHolder(val binding: ExploreItemJobBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ExploreItemJobBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val job = jobs[position]

        holder.binding.txvName.text = job.jobName
        holder.binding.txvCompany.text = job.jobCompanyName
        holder.binding.txvLocation.text = "${job.jobLocationType} (${job.jobLocationRegion})"
        holder.binding.txtExperience.text = "Min. ${job.jobExperience} years of experience"


        when(mode){
            JobCardMode.Explore -> {
                holder.binding.btnApply.isVisible = true
                holder.binding.btnApply.text = "Apply"
                holder.binding.exploreItemJobBtnMark.isVisible = true
                holder.binding.exploreItemJobBtnMark.setImageResource(R.drawable.outline_bookmark_24)
                val isMarked = job.jobId in favoriteIds
                if(isMarked){
                    holder.binding.exploreItemJobBtnMark.setBackgroundResource(R.drawable.circle_button_active)
                    holder.binding.exploreItemJobBtnMark.setColorFilter(Color.WHITE)
                }else{
                    holder.binding.exploreItemJobBtnMark.setBackgroundResource(R.drawable.circle_button)
                    holder.binding.exploreItemJobBtnMark.setColorFilter(Color.GRAY)
                }
            }
            JobCardMode.Favorite -> {
                holder.binding.btnApply.isVisible = true
                holder.binding.btnApply.text = "Apply"
                holder.binding.exploreItemJobBtnMark.isVisible = true
                holder.binding.exploreItemJobBtnMark.setImageResource(R.drawable.outline_bookmark_remove_24)
                holder.binding.exploreItemJobBtnMark.setBackgroundResource(R.drawable.circle_button_active)
                holder.binding.exploreItemJobBtnMark.setColorFilter(Color.WHITE)

            }
            JobCardMode.Applied -> {
                holder.binding.btnApply.isVisible = true
                holder.binding.btnApply.text = "Waiting for applied"
                holder.binding.exploreItemJobBtnMark.isVisible = false
                if(job.isApplied){
                    holder.binding.btnApply.isEnabled = false
                }
            }
        }

        holder.binding.btnApply.setOnClickListener {
            onApply(job)
        }
        holder.binding.exploreItemJobBtnMark.setOnClickListener {
            onMark(job)
        }
        holder.binding.itemcardJob.setOnClickListener {
            onCard(job)
        }
    }

    override fun getItemCount(): Int = jobs.size
}