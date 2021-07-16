package com.sunnyside.kookoo.student.ui.fragments.dashboard

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
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
import java.io.ByteArrayOutputStream
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
    private lateinit var image : Uri
    private lateinit var picLink : String

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

            picLink = profile.picLink

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

        binding.imgProfilePfp.setOnClickListener {
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

        try {
            val bitmap = (binding.imgProfilePfp.drawable as BitmapDrawable).bitmap
            val baos = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 20, baos)
            val data = baos.toByteArray()

            val storageRef = Firebase.storage.reference
            val profilePictureRef = storageRef.child("profilePicture/${image.lastPathSegment}")

            val uploadTask = profilePictureRef.putBytes(data)

            uploadTask.addOnFailureListener {
                Log.d("Set up Profile", "Can't Upload Image")
            }.addOnSuccessListener { taskSnapshot ->

            }

            picLink = image.lastPathSegment.toString() + ".png"
        }
        catch (e:Exception) {

        }

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
            picLink
        )

        mEditProfileViewModel.saveProfile(editedProfile)



        findNavController().popBackStack()
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
            binding.imgProfilePfp.setImageURI(image)
        }
    }
}