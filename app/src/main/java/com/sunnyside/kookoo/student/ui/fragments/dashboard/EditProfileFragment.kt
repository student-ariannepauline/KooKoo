package com.sunnyside.kookoo.student.ui.fragments.dashboard

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.sunnyside.kookoo.R
import com.sunnyside.kookoo.databinding.FragmentEditProfileBinding
import com.sunnyside.kookoo.databinding.FragmentProfileBinding
import com.sunnyside.kookoo.setAppBarTitle
import com.sunnyside.kookoo.student.model.StudentProfileModel
import com.sunnyside.kookoo.student.ui.viewmodel.EditProfileViewModel
import com.sunnyside.kookoo.utilities.AppBarFragment
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class EditProfileFragment : AppBarFragment() {
    private lateinit var mEditProfileViewModel: EditProfileViewModel
    private var _binding: FragmentEditProfileBinding? = null
    private val binding get() = _binding!!
    private lateinit var userProfile: StudentProfileModel
    private val args : EditProfileFragmentArgs by navArgs()

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        inflater.inflate(R.menu.edit_profile_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.action_save -> {
            save()
            true
        }
        else -> {
            super.onOptionsItemSelected(item)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentEditProfileBinding.inflate(inflater, container, false)
        val view = binding.root

        val user = Firebase.auth.currentUser

        mEditProfileViewModel = ViewModelProvider(this).get(EditProfileViewModel::class.java)
        userProfile = args.profile

        setupViews()


        return view
    }

    private fun setupViews() {
        setAppBarTitle("Edit Profile")

        userProfile.let { profile ->
            val birthdayFormat: DateTimeFormatter = DateTimeFormatter.ofPattern("MMM dd, yyyy")
            val birthday =
                LocalDateTime.ofInstant(profile.birthDate.toInstant(), ZoneId.systemDefault())
            binding.profileName.text = profile.firstName
            binding.profileEmail.setText(profile.email)
            binding.profileYearLevel.text = profile.level.toString() + " year " + profile.program
            binding.profileNumber.setText(profile.contactNumber)
            binding.profileAddress.setText(profile.address)
            binding.profileBirthday.setText(birthday.format(birthdayFormat))
        }

    }

    private fun save() {
        findNavController().popBackStack()
    }
}