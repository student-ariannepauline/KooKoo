package com.sunnyside.kookoo.testKolanglods.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.sunnyside.kookoo.R
import com.sunnyside.kookoo.student.ui.viewmodel.DashboardViewModel
import kotlinx.android.synthetic.main.fragment_join_class_test.view.*


class JoinClassTestFragment : Fragment() {
    lateinit var mDashboardViewModel: DashboardViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_join_class_test, container, false)
        mDashboardViewModel = ViewModelProvider(this).get(DashboardViewModel::class.java)
        val user = Firebase.auth.currentUser

        view.testJoinClass_btn.setOnClickListener {
            if (user != null) {
                mDashboardViewModel.joinClass(view.testJoinClass_text.text.toString(), user.uid)
            }
            findNavController().navigate(R.id.action_joinClassTestFragment_to_testDashboardFragment)
        }

        view.testJoinBack_btn.setOnClickListener {
            findNavController().navigate(R.id.action_joinClassTestFragment_to_testDashboardFragment)
        }

        return view
    }


}