package com.example.gawe17.myjob

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gawe17.R
import com.example.gawe17.core.network.ApiHelper
import com.example.gawe17.databinding.FragmentApplicationListBinding
import com.example.gawe17.explore.JobAdapter
import com.example.gawe17.model.JobCardMode
import com.example.gawe17.model.JobList
import org.json.JSONObject

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class ApplicationListFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var rv: RecyclerView
    private lateinit var adapter: JobAdapter

    val jobList = mutableListOf<JobList>()

    var _binding: FragmentApplicationListBinding? = null
    val binding get() = _binding!!

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
        _binding = FragmentApplicationListBinding.inflate(inflater, container, false)
        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ApplicationListFragment().apply {
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
            mode = JobCardMode.Applied,
            onApply = {},
            onMark = {},
            onCard = {}
        )

        rv.adapter = adapter
        rv.layoutManager = LinearLayoutManager(requireContext())

        loadJobApplication()
    }

    private fun loadJobApplication(){
        Thread{
            val (code, response) = ApiHelper.get("job-applications")
            val jsonResponse = if(response!=null) JSONObject(response) else null
            activity?.runOnUiThread {
                if(code==200&&jsonResponse!=null){
                    val jsonData = jsonResponse.getJSONArray("data")
                    if(jsonData.length() <= 0){
                        isProblem(true, "there are no jobs you applied for, let's find some", R.drawable.furina_cherfull)
                    }else{
                        isProblem(false)
                        jobList.clear()
                        for(i in 0 until jsonData.length()){
                            val json = jsonData.getJSONObject(i)
                            val job = json.getJSONObject("job")
                            val company = job.getJSONObject("company")
                            jobList.add(JobList(
                                jobId = job.getInt("id"),
                                jobName = job.getString("name"),
                                jobCompanyName = company.getString("name"),
                                jobLocationType = job.getString("locationType"),
                                jobLocationRegion = job.getString("locationRegion"),
                                jobExperience = job.getString("yearOfExperience"),
                                quota = job.getInt("quota")
                            ))
                        }
                    }

                }else if(jsonResponse!=null){
                    isProblem(true, jsonResponse.optString("message", "Sorry, there is a small error"), R.drawable.furina_shock)
                }else{
                    isProblem(true, "Server Doesn't Responding. It's Not You, It's Us", R.drawable.furina_crying)
                }
                adapter.notifyDataSetChanged()
            }
        }.start()
    }

    private fun isProblem(status: Boolean, text: String? = null, image: Int? = null){
        val playout = view?.findViewById<View>(R.id.problemLayout)
        val ptext = view?.findViewById<TextView>(R.id.txtProblem)
        val pimage= view?.findViewById<ImageView>(R.id.imgProblem)
        if(status){
            jobList.clear()
            playout?.visibility = View.VISIBLE
            rv.visibility = View.GONE
            text?.let{ptext?.text=it}
            image?.let{pimage?.setImageResource(it)}
        }else{
            rv.visibility = View.VISIBLE
            playout?.visibility = View.GONE
        }
    }

    override fun onResume() {
        super.onResume()
        loadJobApplication()
    }
}