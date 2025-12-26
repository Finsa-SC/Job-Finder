import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import com.example.gawe17.R
import androidx.recyclerview.widget.RecyclerView
import com.example.gawe17.Models.JobList
import com.example.gawe17.databinding.ExploreItemJobBinding

class JobAdapter(
    private val jobs: List<JobList>,
    private val onItemClick: (JobList) -> Unit,
    private val onButtonClick: (JobList) -> Unit,
    private val onButtonMarkClick: (JobList) -> Unit
): RecyclerView.Adapter<JobAdapter.JobViewHolder>(){

    class JobViewHolder(val binding: ExploreItemJobBinding)
        : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JobViewHolder {
        val binding = ExploreItemJobBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return JobViewHolder(binding)
    }

    override fun onBindViewHolder(holder: JobViewHolder, position: Int) {
        val job = jobs[position]
        with(holder.binding){
            txvName.text = job.jobName
            txvCompany.text = job.jobCompanyName
            txvLocation.text = "${job.jobLocationType} (${job.jobLocationRegion})"
            txtExperience.text = "Min. ${job.jobExperience} years of experience"

            btnApply.setOnClickListener {
                onButtonClick(job)
            }
            itemcardJob.setOnClickListener {
                onItemClick(job)
            }
            exploreItemJobBtnMark.setOnClickListener {
                onButtonMarkClick(job)
            }
        }
    }

    override fun getItemCount(): Int = jobs.size
}

