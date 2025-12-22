package com.example.gawe17.Main.Fragment.ItemExplore

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.gawe17.Helper.ApiHelper
import com.example.gawe17.R
import org.json.JSONObject

//Init

//UI Component
private lateinit var Title: TextView
private lateinit var Company: TextView
private lateinit var Location: TextView
private lateinit var Experience: TextView
private lateinit var AboutCompany: TextView
private lateinit var AboutJob: TextView
private lateinit var Backward: Button

class JobDetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_job_detail)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

//        UI Component Init
        Title = findViewById(R.id.jobDetail_Title)
        Company = findViewById(R.id.jobDetail_Company)
        Location = findViewById(R.id.jobDetail_Location)
        Experience = findViewById(R.id.jobDetail_MinimunExperience)
//        AboutCompany = findViewById(R.id.jobDetail_Company)
//        AboutJob = findViewById(R.id.jobD)
        Backward = findViewById(R.id.jobDetail_btnBackward)

        val _jobId = intent.getIntExtra("JOB_ID", -1)


        Backward.setOnClickListener { this.finish() }
        loadJob(_jobId)
    }

    private fun loadJob(jobId: Int?){
        Thread{
            val (code, response) = ApiHelper.get("jobs/$jobId")
            val jsonResponse = if(response!=null) JSONObject(response) else null

            runOnUiThread {
                if(code==200 && jsonResponse!=null){
                    val jsonData = jsonResponse.getJSONObject("data")
                    val jsonCompany = jsonData.getJSONObject("company")

                    Title.text = jsonData.getString("name")
                    Company.text = jsonCompany.getString("name")
                    Location.text = "${jsonData.getString("locationType")}(${jsonData.getString("locationRegion")})"
                    Company.text = "Min. ${jsonData.getString("yearOfExperience")} years of experience"
                }
            }
        }.start()
    }
}