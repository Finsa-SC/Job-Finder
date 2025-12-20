package com.example.gawe17.Main

import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.example.gawe17.Main.Fragment.ExploreFragment
import com.example.gawe17.Main.Fragment.MyJobFragment
import com.example.gawe17.Main.Fragment.ProfileFragment
import com.example.gawe17.R

class MainActivity : AppCompatActivity() {
    private lateinit var navExplore: TextView
    private lateinit var navMyJob: TextView
    private lateinit var navProfile: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        navExplore = findViewById(R.id.nvgExplore)
        navMyJob = findViewById(R.id.nvgMyJob)
        navProfile= findViewById(R.id.nvgProfile)
        if(savedInstanceState ==null){loadFragment(ExploreFragment())}


        navExplore.setOnClickListener { loadFragment(ExploreFragment()) }
        navMyJob.setOnClickListener { loadFragment(MyJobFragment()) }
        navProfile.setOnClickListener { loadFragment(ProfileFragment()) }
    }

    private fun loadFragment(fragment: Fragment){
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .commit()
    }
}