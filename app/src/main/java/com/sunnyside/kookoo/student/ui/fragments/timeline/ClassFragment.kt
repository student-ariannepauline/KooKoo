package com.sunnyside.kookoo.student.ui.fragments.timeline

import android.app.ActionBar
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.sunnyside.kookoo.R
import com.sunnyside.kookoo.databinding.FragmentClassBinding
import com.sunnyside.kookoo.databinding.FragmentProfileBinding

class ClassFragment : Fragment() {
    lateinit var appBarConfiguration: AppBarConfiguration
    lateinit var navView: BottomNavigationView

    private var _binding: FragmentClassBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentClassBinding.inflate(inflater, container, false)
        val view = binding.root
        
        val navController = findNavController()
        navView = binding.classBottomNavigationView
        appBarConfiguration = AppBarConfiguration(navController.graph)

        navView.setupWithNavController(navController)

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}