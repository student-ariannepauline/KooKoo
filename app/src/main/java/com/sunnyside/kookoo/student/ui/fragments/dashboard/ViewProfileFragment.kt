package com.sunnyside.kookoo.student.ui.fragments.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.sunnyside.kookoo.R
import com.sunnyside.kookoo.databinding.FragmentProfileBinding
import com.sunnyside.kookoo.hideKeyboard
import com.sunnyside.kookoo.setAppBarTitle
import com.sunnyside.kookoo.student.ui.viewmodel.ProfileViewModel
import com.sunnyside.kookoo.utilities.AppBarFragment
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class ViewProfileFragment : AppBarFragment() {
    private lateinit var mProfileViewModel: ProfileViewModel
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val view = binding.root

        val user = Firebase.auth.currentUser
        mProfileViewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)

        if (user != null) {
            mProfileViewModel.getProfile(user.uid)
        }

        mProfileViewModel.userProfileModel.observe(viewLifecycleOwner, Observer { profile ->
            val birthdayFormat: DateTimeFormatter = DateTimeFormatter.ofPattern("MMM dd, yyyy")
            val birthday =
                LocalDateTime.ofInstant(profile.birthDate.toInstant(), ZoneId.systemDefault())
            binding.profileName.text = profile.firstName
            binding.profileEmail.setText(profile.email)
            binding.profileYearLevel.text = profile.level.toString() + " year " + profile.program
            binding.profileNumber.setText(profile.contactNumber)
            binding.profileAddress.setText(profile.address)
            binding.profileBirthday.setText(birthday.format(birthdayFormat))
        })

        setupViews()

        return view
    }

    private fun setupViews() {
        setAppBarTitle("Profile")

        hideKeyboard(binding.profileAddress)
        hideKeyboard(binding.profileNumber)
        hideKeyboard(binding.profileEmail)

        binding.profileEditButton.setOnClickListener {
            mProfileViewModel.userProfileModel.value?.let { it1 ->
                val action =
                    ViewProfileFragmentDirections.actionViewProfileFragment3ToEditProfileFragment(
                        it1
                    )
                findNavController().navigate(action)
            }


        }

    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}