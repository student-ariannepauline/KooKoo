package com.sunnyside.kookoo.student.ui.fragments.dashboard

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.sunnyside.kookoo.R
import com.sunnyside.kookoo.databinding.FragmentEditProfileBinding
import com.sunnyside.kookoo.databinding.FragmentSetupprofileBinding
import com.sunnyside.kookoo.hideKeyboard
import com.sunnyside.kookoo.setAppBarTitle
import com.sunnyside.kookoo.student.model.StudentProfileModel
import com.sunnyside.kookoo.student.ui.pickers.DatePicker
import com.sunnyside.kookoo.student.ui.viewmodel.CreateProfileViewModel
import com.sunnyside.kookoo.student.ui.viewmodel.EditProfileViewModel
import com.sunnyside.kookoo.utilities.AppBarFragment
import java.io.File
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*

class SetupProfileFragment : AppBarFragment() {
    private lateinit var mSetupProfileViewModel: CreateProfileViewModel
    private var _binding: FragmentSetupprofileBinding? = null
    private val binding get() = _binding!!
    private lateinit var userProfile: StudentProfileModel
    private lateinit var birthDate: Date
    private lateinit var image : Uri

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentSetupprofileBinding.inflate(inflater, container, false)
        val view = binding.root


        mSetupProfileViewModel = ViewModelProvider(this).get(CreateProfileViewModel::class.java)

        setupViews()

        return view
    }

    private fun createProfile(newProfile: StudentProfileModel) {
        mSetupProfileViewModel.setProfile(newProfile)

    }

    private fun setupViews() {
        setAppBarTitle("Seting up your Profile")

        val user = Firebase.auth.currentUser
        val email = user?.email
        val firstName = user?.displayName

        binding.textSetupName.text = firstName
        binding.textSetupEmail.text = email

        binding.setupPfp.setOnClickListener {
            //check runtime permission
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                if (activity?.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) ==
                    PackageManager.PERMISSION_DENIED){
                    //permission denied
                    val permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE);
                    //show popup to request runtime permission
                    requestPermissions(permissions, PERMISSION_CODE);
                }
                else{
                    //permission already granted
                    pickImageFromGallery();
                }
            }
            else{
                //system OS is < Marshmallow
                pickImageFromGallery();
            }
        }

        binding.btnDatePicker.setOnClickListener {
            val pickerFragment = DatePicker() { selectedDate ->
                birthDate = Date.from(
                    selectedDate.atStartOfDay().atZone(ZoneId.systemDefault())
                        .toInstant()
                )

                val birthdayFormat: DateTimeFormatter = DateTimeFormatter.ofPattern("MMM dd, yyyy")

                binding.btnDatePicker.setText(selectedDate.format(birthdayFormat))
            }

            pickerFragment.show(childFragmentManager, "datePicker")
        }

        binding.setupNextBtn.setOnClickListener {
            Log.d("Setup Profile", "Hello")
            try {
                val number = binding.setupContact.text.toString()
                val address = binding.setupAddress.text.toString()
                val program = binding.setupProgram.text.toString()
                val year = 1.toLong()
                val file = image

                val storageRef = Firebase.storage.reference
                val profilePictureRef = storageRef.child("profilePicture/${file.lastPathSegment}")
                val uploadTask = profilePictureRef.putFile(file)

                uploadTask.addOnFailureListener {
                    Log.d("Set up Profile", "Can't Upload Image")
                }.addOnSuccessListener { taskSnapshot ->

                }

                createProfile(
                    StudentProfileModel(
                        user?.uid as String,
                        firstName as String,
                        " ",
                        number,
                        address,
                        email as String,
                        birthDate,
                        program,
                        year,
                        file.lastPathSegment.toString() + ".png"
                    )
                )

                findNavController().navigate(R.id.dashboardFragment2)

            } catch (e: Exception) {
                Log.d("Setup Profile", "Wrong Input")
            }
        }

    }

    private fun pickImageFromGallery() {
        //Intent to pick image
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_PICK_CODE)
    }

    companion object {
        //image pick code
        private val IMAGE_PICK_CODE = 1000;
        //Permission code
        private val PERMISSION_CODE = 1001;
    }

    //handle requested permission result
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when(requestCode){
            PERMISSION_CODE -> {
                if (grantResults.size >0 && grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED){
                    //permission from popup granted
                    pickImageFromGallery()
                }
                else{
                    //permission from popup denied
                    Toast.makeText(requireContext(), "Permission denied", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    //handle result of picked image
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK && requestCode == IMAGE_PICK_CODE){
            image = data?.data!!
            binding.setupPfp.setImageURI(image)
        }
    }


}