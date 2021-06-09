package com.sunnyside.kookoo.testKolanglods.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.sunnyside.kookoo.R
import com.sunnyside.kookoo.student.ui.viewmodel.DashboardViewModel
import kotlinx.android.synthetic.main.fragment_test_dashboard.view.*


class TestDashboardFragment : Fragment() {
    lateinit var mDashboardViewModel: DashboardViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_test_dashboard, container, false)

        mDashboardViewModel = ViewModelProvider(this).get(DashboardViewModel::class.java)
        val user = Firebase.auth.currentUser

        if (user != null) {
            mDashboardViewModel.getClasses(user.uid)
        }

        mDashboardViewModel.joinedClasses.observe(viewLifecycleOwner, Observer{classes ->
            Log.d("tite", classes.toString())
        })



        view.test_logout.setOnClickListener {
            Firebase.auth.signOut()
            findNavController().navigate(R.id.action_testDashboardFragment_to_verificationActivity2)
        }

        view.test_joinClass.setOnClickListener {
            findNavController().navigate(R.id.action_testDashboardFragment_to_timelineFragment)
        }

        view.test_createClass.setOnClickListener {
            findNavController().navigate(R.id.action_testDashboardFragment_to_createClassTestFragment)
        }

        return view
    }


}