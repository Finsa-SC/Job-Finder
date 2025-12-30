package com.example.gawe17.myjob

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.gawe17.R
import com.example.gawe17.databinding.FragmentMyJobBinding
import com.google.android.material.tabs.TabLayoutMediator

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class MyJobFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    var _binding: FragmentMyJobBinding? = null
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
        _binding = FragmentMyJobBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            MyJobFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val viewPage = binding.viewPage
        val tabLayout = binding.tabLayout

        val adapter = MyViewAdapterJob(this)
        viewPage.adapter = adapter

        TabLayoutMediator(tabLayout, viewPage) {tab, position ->
            tab.text = when(position){
                0->"Saved Jobs"
                1->"Application List"
                else->"Saved Jobs"
            }
        }.attach()

    }

    override fun onResume() {
        super.onResume()
    }
}