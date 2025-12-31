package com.example.gawe17.explore

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gawe17.MainActivity
import com.example.gawe17.core.util.UIHelper
import com.example.gawe17.explore.detail.JobDetailActivity
import com.example.gawe17.model.JobList
import com.example.gawe17.R
import com.example.gawe17.databinding.FragmentExploreBinding
import com.example.gawe17.core.network.ApiHelper
import com.example.gawe17.model.JobCardMode
import org.json.JSONObject
import java.net.URLEncoder

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class ExploreFragment : Fragment() {
    private lateinit var problemLayout: View
    private var rv: RecyclerView? = null


    private var txtLocation: String? = null
    //filter
    var _binding: FragmentExploreBinding? = null
    val binding get() = _binding!!

    //problem
    private lateinit var problemText: TextView
    private lateinit var problemImage: ImageView

    //list
    private var jobList = mutableListOf<JobList>()
    private val appliedIds = mutableSetOf<Int>()
    private val favoriteIds get() = (requireActivity() as MainActivity).favoriteIds
    private lateinit var adapter: JobAdapter


    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentExploreBinding.inflate(inflater, container, false)
        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ExploreFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        problemLayout = view.findViewById(R.id.includeProblem)

//        filter
        binding.btnAll.setOnClickListener {
            txtLocation = null
            activeButton(binding.btnAll)
            loadJobs()
        }
        binding.btnRemote.setOnClickListener {
            txtLocation = "Remote"
            activeButton(binding.btnRemote)
            loadJobs()
        }
        binding.btnOnsite.setOnClickListener {
            txtLocation = "Onsite"
            activeButton(binding.btnOnsite)
            loadJobs()
        }
        binding.txtSearch?.setOnEditorActionListener { v, actionId, event ->
            if(actionId == EditorInfo.IME_ACTION_SEARCH){
                loadJobs()
                true
            }else false
        }
//        problem
        problemText = view.findViewById(R.id.txtProblem)
        problemImage = view.findViewById(R.id.imgProblem)

//        core
        val rv = binding.rvJob

        adapter = JobAdapter(
            jobs = jobList,
            mode = JobCardMode.Explore,
            favoriteIds = favoriteIds,
            onApply = { job ->
                applyJob(job.jobId)
                job.isApplied=!job.isApplied
                adapter.notifyDataSetChanged()
            },
            onMark = {job ->
                if(job.jobId in favoriteIds){
                    favoriteIds.remove(job.jobId)
                    job.isMarked = false
                }else{
                    favoriteIds.add(job.jobId)
                    job.isMarked=true
                }
                adapter.notifyItemChanged(jobList.indexOf(job))
            },
            onCard = {job ->
                val intent = Intent(requireContext(), JobDetailActivity::class.java)
                intent.putExtra("JOB_ID", job.jobId)
                startActivity(intent)
            }
        )
        rv.adapter = adapter
        rv.layoutManager = LinearLayoutManager(requireContext())

        loadJobs()
        activeButton(binding.btnAll)
        appliedJob()
    }

    override fun onResume() {
        super.onResume()
        loadJobs()
    }

    private fun loadJobs(){
        Thread{
            val (code, response) = ApiHelper.get(endpointBuilder(search = binding.txtSearch?.text?.toString(), location = txtLocation))

            val jsonResponse = if(!response.isNullOrBlank()) JSONObject(response) else null

            activity?.runOnUiThread {
                if(code == 200 && jsonResponse!= null){
                    val jsonData = jsonResponse.getJSONArray("data")

                    if(jsonData.length() <=0){
                        isProblem(
                            true,
                            "No Jobs Found Matching Your Criteria!!",
                            R.drawable.furina_shock
                        )
                    }else{
                        isProblem(false)
                        jobList.clear()
                        for(i in 0 until jsonData.length()){
                            val json = jsonData.getJSONObject(i)
                            val company = json.getJSONObject("company")
                            if(json.getInt("quota") <= 0) continue
                            val id = json.getInt("id")
                            jobList.add(JobList(
                                jobId = id,
                                jobName = json.getString("name"),
                                jobCompanyName = company.getString("name"),
                                jobLocationType = json.getString("locationType"),
                                jobLocationRegion = json.getString("locationRegion"),
                                jobExperience = json.getString("yearOfExperience"),
                                quota = json.getInt("quota"),
                                isApplied = id in appliedIds,
                                isMarked = id in favoriteIds
                            ))
                        }
                    }
                }
                else if(jsonResponse!=null){
                    isProblem(
                        true,
                        jsonResponse.optString("message", "We Encountered an Issue While Processing Your Request!!"),
                        R.drawable.furina_sigh
                    )
                }else{
                    isProblem(
                        true,
                        "Server is Unreacheable. It's Not You, It's Us!",
                        R.drawable.furina_crying
                    )
                }
                adapter.notifyDataSetChanged()
            }
        }.start()
    }
    fun applyJob(jobId: Int){
        if(jobId in appliedIds){
            UIHelper.showDialog(requireContext(), "You Are Currently Applying This Job Before", R.drawable.furina_intimidating)
            return
        }
        Thread {
            val (code, response) = ApiHelper.post("jobs/${jobId}/apply")
            activity?.runOnUiThread {
                if(code==200){
                    UIHelper.showDialog(requireContext(), "Success Applied a Job!", R.drawable.furina_instruction)
                    appliedIds.add(jobId)
                }
            }
        }.start()
    }
    private fun appliedJob(){
        Thread{
            val (code, response) = ApiHelper.get("job-applications")
            val jsonResponse = if(!response.isNullOrBlank()) JSONObject(response) else null
            activity?.runOnUiThread {
                if(code==200&&jsonResponse!=null){
                    val jsonData = jsonResponse.getJSONArray("data")
                    for(i in 0 until jsonData.length()){
                        val json = jsonData.getJSONObject(i).getJSONObject("job")
                        appliedIds.add(json.getInt("id"))
                    }
                }
            }
        }.start()
    }

    private fun endpointBuilder(search: String? = null, location: String? = null): String{
        val params = mutableListOf<String?>()
        if(!search.isNullOrBlank()){
            params.add("search=${URLEncoder.encode(search, "UTF-8")}")
        }
        if(!location.isNullOrBlank()){
            params.add("location=${URLEncoder.encode(location, "UTF-8")}")
        }
        return if(params.isEmpty()) "jobs" else "jobs?" + params.joinToString("&")
    }

    fun isProblem(status: Boolean, txtProblem: String? = null, imgProblem: Int? = null){
        if(status){
            jobList.clear()

            problemLayout.visibility = View.VISIBLE
            rv?.visibility = View.GONE

            txtProblem?.let {  problemText.text = it  }
            imgProblem?.let { problemImage.setImageResource(it) }
        }else{
            problemLayout.visibility = View.GONE
            rv?.visibility = View.VISIBLE
        }
    }

    private fun activeButton(btn: Button){
        binding.btnAll.isSelected = false
        binding.btnOnsite.isSelected = false
        binding.btnRemote.isSelected = false

        btn.isSelected = true
    }
}