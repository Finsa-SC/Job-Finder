package com.example.gawe17.Main.Fragment.ItemExplore

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintSet
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
                    problemWindow(false)
                    val jsonData = jsonResponse.getJSONObject("data")
                    val jsonCompany = jsonData.getJSONObject("company")

                    Title.text = jsonData.getString("name")
                    Company.text = jsonCompany.getString("name")
                    Location.text = "${jsonData.getString("locationType")}(${jsonData.getString("locationRegion")})"
                    Company.text = "Min. ${jsonData.getString("yearOfExperience")} years of experience"
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
        val erorWindow = findViewById<View>(R.id.ACTjobDetail_problemWindow)
        if(status){
            header.visibility = View.GONE
            erorWindow.visibility = View.VISIBLE

            message?.let{findViewById<TextView>(R.id.txtProblem).text = it}
            image?.let{findViewById<ImageView>(R.id.imgProblem).setImageResource(it)}
        }else{
            header.visibility = View.VISIBLE
            erorWindow.visibility = View.GONE
        }
    }

}