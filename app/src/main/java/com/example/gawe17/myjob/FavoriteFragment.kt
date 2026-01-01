package com.example.gawe17.myjob

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gawe17.MainActivity
import com.example.gawe17.R
import com.example.gawe17.core.network.ApiHelper
import com.example.gawe17.core.util.UIHelper
import com.example.gawe17.databinding.FragmentFavoriteBinding
import com.example.gawe17.explore.JobAdapter
import com.example.gawe17.explore.detail.JobDetailActivity
import com.example.gawe17.model.JobCardMode
import com.example.gawe17.model.JobList
import org.json.JSONObject

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class FavoriteFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null
    var _binding: FragmentFavoriteBinding? = null
    val binding get() = _binding!!

    private val jobList = mutableListOf<JobList>()
    private lateinit var adapter: JobAdapter
    private lateinit var rv: RecyclerView
    private val favoriteIds get() = (requireActivity() as MainActivity).favoriteIds
    private val appliedIds get() = (requireActivity() as MainActivity).appliedIds
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoriteBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FavoriteFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        rv = binding.rvJob

        adapter = JobAdapter(
            jobs = jobList,
            mode = JobCardMode.Favorite,
            favoriteIds = favoriteIds,
            onApply = { job->
                applyJob(job.jobId)
            },
            onMark = { job ->
                val index = jobList.indexOfFirst { it.jobId == job.jobId }
                if(index != -1){
                    favoriteIds.remove(job.jobId)
                    jobList.removeAt(index)
                    adapter.notifyItemRemoved(index)
                    if(favoriteIds.isEmpty()) showProblem()
                }
            },
            onCard = {job->
                openDetail(job.jobId)
            }
        )
        rv.adapter = adapter
        rv.layoutManager = LinearLayoutManager(requireContext())

        appliedJob()
        loadFavoriteJob()
    }

    override fun onResume() {
        super.onResume()
        loadFavoriteJob()
    }

    private fun loadFavoriteJob(){

        if(favoriteIds.isEmpty()){
            showProblem()
            return
        }
        Thread{
            jobList.clear()
            for(id in favoriteIds){
                val (code, response) = ApiHelper.get("jobs/${id}")
                val jsonResponse = if(!response.isNullOrBlank()) JSONObject(response) else null
                activity?.runOnUiThread {
                    if(code==200&&jsonResponse!=null){
                        isProblem(false)
                        val jsonData = jsonResponse.getJSONObject("data")
                        val jsonCompany = jsonData.getJSONObject("company")
                        jobList.add(JobList(
                            jobId = jsonData.getInt("id"),
                            jobName = jsonData.getString("name"),
                            jobCompanyName = jsonCompany.getString("name"),
                            jobLocationType = jsonData.getString("locationType"),
                            jobLocationRegion = jsonData.getString("locationRegion"),
                            jobExperience = jsonData.getString("yearOfExperience"),
                            quota = jsonData.getInt("quota"),
                        ))
                    }else if(jsonResponse!=null){
                        isProblem(true, jsonResponse.optString("message","Sorry, But We Can't Load Your Page Now"), R.drawable.furina_shock)
                    }else{
                        isProblem(true, "Server Doesn't Responding. It's Not You, It's Us", R.drawable.furina_crying)
                    }
                    adapter.notifyDataSetChanged()
                }
            }
        }.start()
    }

    private fun showProblem(){
        isProblem(true, "no data needs to be displayed at this time", R.drawable.furina_boring)
    }
    private fun isProblem(status: Boolean, text: String? = null, image: Int? = null){
        val viewProblem = view?.findViewById<View>(R.id.problemLayout)
        if(status){
            rv.visibility = View.GONE
            viewProblem?.visibility = View.VISIBLE
            text?.let { view?.findViewById<TextView>(R.id.txtProblem)?.text = it }
            image?.let { view?.findViewById<ImageView>(R.id.imgProblem)?.setImageResource(it) }
        }else{
            rv.visibility = View.VISIBLE
            viewProblem?.visibility = View.GONE
        }
    }

    private fun applyJob(jobId: Int){
        if (jobId !in appliedIds) {
            Thread {
                val (code, response) = ApiHelper.post("jobs/${jobId}/apply")
                activity?.runOnUiThread {
                    if (code == 200) {
                        UIHelper.showDialog(
                            requireContext(),
                            "Success Applied a Job!",
                            R.drawable.furina_instruction
                        )
                    }
                }
            }.start()
        }else{
            UIHelper.showDialog(
                requireContext(),
                "You Are Currently Applying This Job Before",
                R.drawable.furina_intimidating)
        }
    }

    private fun appliedJob(){
        Thread{
            val (code, response) = ApiHelper.get("job-applications")
            val jsonResponse = if(!response.isNullOrBlank()) JSONObject(response) else return@Thread
            activity?.runOnUiThread {
                if (code == 200) {
                    val jsonData = jsonResponse.getJSONArray("data")
                    appliedIds.clear()
                    for (i in 0 until jsonData.length()) {
                        val json = jsonData.getJSONObject(i).getJSONObject("job")
                        val ids = json.getInt("id")
                        if(!appliedIds.contains(ids)) appliedIds.add(ids)
                    }
                }
            }
        }.start()
    }

    private fun openDetail(jobId: Int){
        val intent = Intent(requireContext(), JobDetailActivity::class.java)
        intent.putExtra("JOB_ID", jobId)
        startActivity(intent)
    }
}