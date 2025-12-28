package com.example.gawe17.explore.detail

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.gawe17.R
import com.example.gawe17.core.network.ApiHelper
import com.example.gawe17.databinding.ActivityJobDetailBinding
import com.google.android.material.tabs.TabLayoutMediator
import org.json.JSONObject

class JobDetailActivity : AppCompatActivity() {
    var jsonJob: JSONObject? = null

    private lateinit var binding: ActivityJobDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityJobDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

//        navigation
        val tabLayout = binding.tabLayout
        val viewPager = binding.viewPage

        val adapter = MyPagerAdapterDetail(this)
        viewPager.adapter = adapter

        TabLayoutMediator(tabLayout, viewPager) {tab, position ->
            tab.text = when(position){
                0 -> "About Company"
                1 -> "About Job"
                else -> ""
            }
        }.attach()


        val _jobId = intent.getIntExtra("JOB_ID", -1)

        binding.jobDetailBtnBackward.setOnClickListener { this.finish() }
        loadJob(_jobId)
    }

    private fun loadJob(jobId: Int?){
        Thread{
            val (code, response) = ApiHelper.get("jobs/$jobId")
            val jsonResponse = if(response!=null) JSONObject(response) else null

            runOnUiThread {
                if(code==200 && jsonResponse!=null){
                    problemWindow(false)
                    val jsonData = jsonResponse.getJSONObject("data")
                    jsonJob = jsonData
                    val jsonCompany = jsonData.getJSONObject("company")

                    binding.jobDetailTitle.text = jsonData.getString("name")
                    binding.jobDetailCompany.text = jsonCompany.getString("name")
                    binding.jobDetailLocation.text = "${jsonData.getString("locationType")}(${jsonData.getString("locationRegion")})"
                    binding.jobDetailMinimunExperience.text = "Min. ${jsonData.getString("yearOfExperience")} years of experience"
                }
                else if(jsonResponse!=null){
                    problemWindow(
                        true,
                        jsonResponse.optString("message", "Failed to Load Information"),
                        R.drawable.furina_shock
                    )
                }else{
                    problemWindow(
                        true,
                        "Server Does Not Responding. It's Not You, It's Us!",
                        R.drawable.furina_crying
                    )
                }
            }
        }.start()
    }

    private fun problemWindow(status: Boolean, message: String? = null, image: Int? = null){
        val header = findViewById<LinearLayout>(R.id.ACTjobDetail_Header)
        val errorWindow = findViewById<View>(R.id.ACTjobDetail_problemWindow)
        if(status){
            header.visibility = View.GONE
            errorWindow.visibility = View.VISIBLE

            message?.let{findViewById<TextView>(R.id.txtProblem).text = it}
            image?.let{findViewById<ImageView>(R.id.imgProblem).setImageResource(it)}
        }else{
            header.visibility = View.VISIBLE
            errorWindow.visibility = View.GONE
        }
    }

}