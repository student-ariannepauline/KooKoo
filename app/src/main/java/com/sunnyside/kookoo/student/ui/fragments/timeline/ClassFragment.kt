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
import kotlinx.android.synthetic.main.fragment_class.view.*


class ClassFragment : Fragment() {
    lateinit var appBarConfiguration: AppBarConfiguration
    lateinit var navView: BottomNavigationView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_class, container, false)
        
        val navController = findNavController()
        navView = view.class_bottomNavigationView
        appBarConfiguration = AppBarConfiguration(navController.graph)

        navView.setupWithNavController(navController)

        return view
    }
}