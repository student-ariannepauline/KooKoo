package com.sunnyside.kookoo.student.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.sunnyside.kookoo.R
import com.sunnyside.kookoo.student.ui.viewmodel.DashboardViewModel
import com.sunnyside.kookoo.student.ui.viewmodel.ProfileViewModel
import kotlinx.android.synthetic.main.fragment_home.view.*
import kotlinx.android.synthetic.main.fragment_profile.view.*

class ViewProfileFragment : Fragment() {
    private lateinit var mProfileViewModel: ProfileViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        val user = Firebase.auth.currentUser
        mProfileViewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)

        if (user != null) {
            mProfileViewModel.getProfile(user.uid)
        }

        mProfileViewModel.userProfile.observe(viewLifecycleOwner, Observer{profile ->
            view.profile_name.text = profile.firstName
            view.profile_email.setText(profile.email)
            view.profile_yearLevel.text = profile.level.toString() + " year " +  profile.program
            view.profile_number.setText(profile.contactNumber)
            view.profile_address.setText(profile.address)
            view.profile_birthday.setText(profile.birthDate.toString())
        })

        return view
    }

}