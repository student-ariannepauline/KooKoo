package com.sunnyside.kookoo.student.ui.fragments.dashboard

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.sunnyside.kookoo.R
import com.sunnyside.kookoo.databinding.FragmentEditProfileBinding
import com.sunnyside.kookoo.databinding.FragmentProfileBinding
import com.sunnyside.kookoo.hideKeyboard
import com.sunnyside.kookoo.setAppBarTitle
import com.sunnyside.kookoo.student.model.StudentProfileModel
import com.sunnyside.kookoo.student.ui.pickers.DatePicker
import com.sunnyside.kookoo.student.ui.viewmodel.EditProfileViewModel
import com.sunnyside.kookoo.utilities.AppBarFragment
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*

class EditProfileFragment : AppBarFragment() {
    private lateinit var mEditProfileViewModel: EditProfileViewModel
    private var _binding: FragmentEditProfileBinding? = null
    private val binding get() = _binding!!
    private lateinit var userProfile: StudentProfileModel
    private val args: EditProfileFragmentArgs by navArgs()

    private lateinit var birthDate : Date

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

        hideKeyboard(binding.profileEmail)
        hideKeyboard(binding.profileNumber)
        hideKeyboard(binding.profileAddress)

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
            birthDate = profile.birthDate

            val picLink = profile.picLink

            val storageRef = Firebase.storage.reference
            val image = storageRef.child("/profilePicture/${picLink}")

            image.downloadUrl
                .addOnSuccessListener { url ->
                    activity?.let {
                        Glide.with(it).
                        load(url)
                            .into(binding.imgProfilePfp)
                    }

                }
        }

        binding.profileBtnBack.setOnClickListener {
            save()
        }

        binding.profileBirthday.setOnClickListener {
            val pickerFragment = DatePicker() { selectedDate ->
                birthDate = Date.from(selectedDate.atStartOfDay().atZone(ZoneId.systemDefault())
                    .toInstant())

                val birthdayFormat: DateTimeFormatter = DateTimeFormatter.ofPattern("MMM dd, yyyy")

                binding.profileBirthday.setText(selectedDate.format(birthdayFormat))
            }

            pickerFragment.show(childFragmentManager, "datePicker")
        }

    }

    private fun save() {
        val editedProfile = StudentProfileModel(
            userProfile.uid,
            userProfile.firstName,
            userProfile.lastName,
            binding.profileNumber.text.toString(),
            binding.profileAddress.text.toString(),
            binding.profileEmail.text.toString(),
            birthDate,
            userProfile.program,
            userProfile.level,
            userProfile.picLink
        )

        mEditProfileViewModel.saveProfile(editedProfile)

        findNavController().popBackStack()
    }
}