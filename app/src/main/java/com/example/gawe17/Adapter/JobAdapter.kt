import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.gawe17.R
import androidx.recyclerview.widget.RecyclerView
import com.example.gawe17.Model.JobList

class JobAdapter(
    private val jobs: List<JobList>
): RecyclerView.Adapter<JobAdapter.JobViewHolder>(){

    class JobViewHolder(view: View): RecyclerView.ViewHolder(view){
        val title: TextView = view.findViewById(R.id.txvName)
        val company: TextView = view.findViewById(R.id.txvCompany)
        val location: TextView = view.findViewById(R.id.txvLocation)
        val experience: TextView = view.findViewById(R.id.txtExperience)
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
    }

    override fun getItemCount(): Int = jobs.size
}

