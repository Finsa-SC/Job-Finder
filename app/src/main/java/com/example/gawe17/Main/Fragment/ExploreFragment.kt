package com.example.gawe17.Main.Fragment

import JobAdapter
import android.app.Dialog
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
import com.example.gawe17.Helper.ApiHelper
import com.example.gawe17.Helper.UIHelper
import com.example.gawe17.Main.Fragment.ItemExplore.JobDetailActivity
import com.example.gawe17.Models.JobList
import com.example.gawe17.R
import com.google.android.material.textfield.TextInputEditText
import org.json.JSONObject
import java.net.URLEncoder
import kotlin.concurrent.thread
import kotlin.text.clear

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"



/**
 * A simple [Fragment] subclass.
 * Use the [ExploreFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ExploreFragment : Fragment() {
    private lateinit var problemLayout: View
    private var rv: RecyclerView? = null

    //filter
    private var txtSearch: TextInputEditText? = null
    private var txtlocation: String? = null
    private lateinit var btnAll: Button
    private lateinit var btnRemote: Button
    private lateinit var btnOnsite: Button

    //problem
    private lateinit var problemText: TextView
    private lateinit var problemImage: ImageView

    //list
    private var jobList = mutableListOf<JobList>()
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
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_explore, container, false)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ExploreFragment.
         */
        // TODO: Rename and change types and number of parameters
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
        txtSearch = view.findViewById(R.id.txtSearch)
        btnAll = view.findViewById(R.id.btnAll)
        btnRemote = view.findViewById(R.id.btnRemote)
        btnOnsite = view.findViewById(R.id.btnOnsite)
        btnAll.setOnClickListener {
            txtlocation = null
            activeButton(btnAll)
            loadJobs()
        }
        btnRemote.setOnClickListener {
            txtlocation = "Remote"
            activeButton(btnRemote)
            loadJobs()
        }
        btnOnsite.setOnClickListener {
            txtlocation = "Onsite"
            activeButton(btnOnsite)
            loadJobs()
        }
        txtSearch?.setOnEditorActionListener { v, actionId, event ->
            if(actionId == EditorInfo.IME_ACTION_SEARCH){
                loadJobs()
                true
            }else false
        }
//        pribblem
        problemText = view.findViewById(R.id.txtProblem)
        problemImage = view.findViewById(R.id.imgProblem)
//        core
        rv = view.findViewById(R.id.rvJob)

        adapter = JobAdapter(
            jobList,
            onItemClick = { job ->
                val intent = Intent(requireContext(), JobDetailActivity::class.java)
                intent.putExtra("JOB_ID", job.jobId)
                startActivity(intent)
            },{ job ->
                applyJob(job.jobId)
                loadJobs()
            }, { job ->

            }
        )
        rv?.adapter = adapter
        rv?.layoutManager = LinearLayoutManager(requireContext())

        loadJobs()
        activeButton(btnAll)
    }

    private fun loadJobs(){
        Thread{
            val (code, response) = ApiHelper.get(endpointBuilder(search = txtSearch?.text?.toString(), location = txtlocation))

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
                            jobList.add(JobList(
                                jobId = json.getInt("id"),
                                jobName = json.getString("name"),
                                jobCompanyName = company.getString("name"),
                                jobLocationType = json.getString("locationType"),
                                jobLocationRegion = json.getString("locationRegion"),
                                jobExperience = json.getString("yearOfExperience"),
                                quota = json.getInt("quota")
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
    private fun applyJob(jobId: Int){
        Thread {
            val (code, response) = ApiHelper.post("jobs/${jobId}/apply")
            activity?.runOnUiThread {
                if(code==200){
                    UIHelper.showDialog(requireContext(), "Success Applied a Job!")
                }
            }
        }.start()

    }

    private fun endpointBuilder(search: String? = null, location: String? = null): String{
        var params = mutableListOf<String?>()
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
        btnAll.isSelected = false
        btnOnsite.isSelected = false
        btnRemote.isSelected = false

        btn.isSelected = true
    }
}