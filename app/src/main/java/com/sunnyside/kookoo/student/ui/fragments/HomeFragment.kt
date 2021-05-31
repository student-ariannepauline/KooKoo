package com.sunnyside.kookoo.student.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.sunnyside.kookoo.R
import com.sunnyside.kookoo.student.ui.viewmodel.DashboardViewModel
import kotlinx.android.synthetic.main.fragment_home.view.*

class HomeFragment : Fragment() {
    private lateinit var mDashboardViewModel: DashboardViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        val user = Firebase.auth.currentUser
        mDashboardViewModel = ViewModelProvider(this).get(DashboardViewModel::class.java)

        if (user != null) {
            mDashboardViewModel.getProfile(user.uid)
        }

        mDashboardViewModel.userProfile.observe(viewLifecycleOwner, Observer{ profile ->
            view.user_email_txt.text = profile.email
            view.user_id_txt.text = profile.uid
            view.user_name_txt.text = profile.firstName + " " + profile.lastName
            view.user_program.text = profile.program
        })



        view.log_out_btn.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            findNavController().navigate(R.id.action_homeFragment_to_verificationActivity)
        }


        return view
    }

}