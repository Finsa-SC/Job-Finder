package com.example.gawe17

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.example.gawe17.databinding.ActivityMainBinding
import com.example.gawe17.explore.ExploreFragment
import com.example.gawe17.profile.ProfileFragment
import com.example.gawe17.myjob.MyJobFragment

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        if(savedInstanceState ==null){loadFragment(ExploreFragment())}


        binding.nvgExplore.setOnClickListener { loadFragment(ExploreFragment()) }
        binding.nvgMyJob.setOnClickListener { loadFragment(MyJobFragment()) }
        binding.nvgProfile.setOnClickListener { loadFragment(ProfileFragment()) }
    }

    private fun loadFragment(fragment: Fragment){
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .commit()
    }
}