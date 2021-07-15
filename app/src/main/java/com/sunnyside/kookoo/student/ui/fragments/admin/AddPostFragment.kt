package com.sunnyside.kookoo.student.ui.fragments.admin

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import com.sunnyside.kookoo.R
import com.sunnyside.kookoo.databinding.FragmentAddPostBinding
import com.sunnyside.kookoo.hideKeyboard
import com.sunnyside.kookoo.setAppBarTitle
import com.sunnyside.kookoo.student.data.JoinedClass
import com.sunnyside.kookoo.student.model.AnnouncementModel
import com.sunnyside.kookoo.student.model.JoinedClassModel
import com.sunnyside.kookoo.student.ui.pickers.DatePicker
import com.sunnyside.kookoo.student.ui.viewmodel.AddPostViewModel
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeParseException


class AddPostFragment : Fragment() {
    private var _binding: FragmentAddPostBinding? = null
    private val binding get() = _binding!!
    private val currentUser = Firebase.auth.currentUser
    private lateinit var addPostViewModel: AddPostViewModel
    private lateinit var joinedClass: JoinedClassModel

    private lateinit var classId: String

    /*New Post Variables*/
    private lateinit var authorName: String
    private lateinit var picLink: String
    private lateinit var title: String
    private lateinit var body: String
    private lateinit var link: String
    private lateinit var deadline: LocalDate
    private lateinit var timestamp: LocalDateTime


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        inflater.inflate(R.menu.add_post_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.action_post -> {
            post()
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
        _binding = FragmentAddPostBinding.inflate(inflater, container, false)
        addPostViewModel = ViewModelProvider(this).get(AddPostViewModel::class.java)
        joinedClass = JoinedClass.joinedClass
        classId = joinedClass.class_id

        val view = binding.root


        setupView()
        setAppBarTitle("Make an Announcement")

        return view
    }

    private fun setupView() {
        val db = Firebase.firestore

        binding.textName.text = currentUser?.displayName.toString()
        binding.textClassroom.text = JoinedClass.joinedClass.name

        currentUser?.uid?.let { uid ->
            val profileRef = db.collection("user_profile").whereEqualTo("uid", uid)

            profileRef.get()
                .addOnSuccessListener { documents ->
                    if (documents != null && !documents.isEmpty) {
                        val document = documents.first()

                        picLink = document.data["picLink"] as String

                        val storageRef = Firebase.storage.reference
                        val image = storageRef.child("/profilePicture/${picLink}")

                        image.downloadUrl
                            .addOnSuccessListener { url ->
                                Glide.with(this).
                                load(url)
                                    .into(binding.imgProfile)
                            }
                    }
                }
        }

        binding.btnDatePicker.setOnClickListener {
            val pickerFragment = DatePicker() { selectedDate ->
                binding.btnDatePicker.text = selectedDate.toString()
            }

            pickerFragment.show(childFragmentManager, "datePicker")
        }



        hideKeyboard(binding.edittextTitle)
        hideKeyboard(binding.edittextBody)
        hideKeyboard(binding.edittextOtherContent)

    }

    private fun post() {

        currentUser?.displayName?.let { displayName ->
            authorName = displayName
        }

        binding.edittextTitle.text.toString().trim().isBlank().apply {
            title = binding.edittextTitle.text.toString()
        }

        body = binding.edittextBody.text.toString()
        link = binding.edittextOtherContent.text.toString()


        try {
            deadline = LocalDate.parse(binding.btnDatePicker.text)
        } catch (e: DateTimeParseException) {
            Toast.makeText(activity, "Please choose a deadline", Toast.LENGTH_LONG).show()
        }

        timestamp = LocalDateTime.now()

        try {
            val newPost = AnnouncementModel(
                "sksksks",
                authorName,
                picLink,
                title,
                body,
                link,
                deadline,
                timestamp
            )

            addPostViewModel.addPost(newPost, classId)
            findNavController().popBackStack()
            setAppBarTitle(joinedClass.name)

        } catch (e: UninitializedPropertyAccessException) {
            Toast.makeText(activity, "Please fill out the form", Toast.LENGTH_LONG).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}