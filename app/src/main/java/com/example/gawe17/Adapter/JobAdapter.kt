import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import com.example.gawe17.R
import androidx.recyclerview.widget.RecyclerView
import com.example.gawe17.Models.JobList

class JobAdapter(
    private val jobs: List<JobList>,
    private val onItemClick: (JobList) -> Unit,
    private val onButtonClick: (JobList) -> Unit,
    private val onButtonMarkClick: (JobList) -> Unit
): RecyclerView.Adapter<JobAdapter.JobViewHolder>(){

    class JobViewHolder(view: View): RecyclerView.ViewHolder(view){
        val title: TextView = view.findViewById(R.id.txvName)
        val company: TextView = view.findViewById(R.id.txvCompany)
        val location: TextView = view.findViewById(R.id.txvLocation)
        val experience: TextView = view.findViewById(R.id.txtExperience)
        val cardView: View = view.findViewById(R.id.itemcardJob)
        val btnApply: Button = view.findViewById(R.id.btnApply)
        val btnMark: ImageButton = view.findViewById(R.id.exploreItemJob_btnMark)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JobViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.explore_item_job, parent,false)
        return JobViewHolder(view)
    }

    override fun onBindViewHolder(holder: JobViewHolder, position: Int) {
        val job = jobs[position]
        holder.title.text = job.jobName
        holder.company.text = job.jobCompanyName
        holder.location.text = "${job.jobLocationType} (${job.jobLocationRegion})"
        holder.experience.text = "Min. ${job.jobExperience} years of experience"

        holder.btnApply.setOnClickListener {
            onButtonClick(job)
        }
        holder.cardView.setOnClickListener {
            onItemClick(job)
        }
        holder.btnMark.setOnClickListener {
            onButtonMarkClick(job)
        }
    }

    override fun getItemCount(): Int = jobs.size
}

