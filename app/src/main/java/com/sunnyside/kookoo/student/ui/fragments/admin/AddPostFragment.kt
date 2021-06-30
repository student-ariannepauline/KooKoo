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
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.sunnyside.kookoo.R
import com.sunnyside.kookoo.databinding.FragmentAddPostBinding
import com.sunnyside.kookoo.setAppBarTitle
import com.sunnyside.kookoo.student.data.JoinedClass
import com.sunnyside.kookoo.student.model.AnnouncementModel
import com.sunnyside.kookoo.student.model.JoinedClassModel
import com.sunnyside.kookoo.student.ui.pickers.DatePicker
import com.sunnyside.kookoo.student.ui.viewmodel.AddPostViewModel
import java.time.LocalDate
import java.time.LocalDateTime


class AddPostFragment : Fragment() {
    private var _binding: FragmentAddPostBinding? = null
    private val binding get() = _binding!!
    private val currentUser = Firebase.auth.currentUser
    private lateinit var addPostViewModel: AddPostViewModel
    private lateinit var joinedClass: JoinedClassModel

    private lateinit var classId: String

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
        binding.textName.text = currentUser?.displayName.toString()
        binding.textClassroom.text = JoinedClass.joinedClass.name


        binding.btnDatePicker.setOnClickListener {
            val pickerFragment = DatePicker() { selectedDate ->
                binding.btnDatePicker.text = selectedDate.toString()
            }

            pickerFragment.show(childFragmentManager, "datePicker")
        }
    }

    private fun post() {
        if (checkInput()) {
            val newPost = AnnouncementModel(
                currentUser?.displayName.toString(),
                "wala_pa",
                binding.edittextTitle.text.toString(),
                binding.edittextBody.text.toString(),
                binding.edittextOtherContent.text.toString(),
                LocalDate.parse(binding.btnDatePicker.text),
                LocalDateTime.now()
            )
            addPostViewModel.addPost(newPost, classId)
            findNavController().popBackStack()
            setAppBarTitle(joinedClass.name)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun checkInput(): Boolean {
        val conditions = listOf(
            !binding.edittextTitle.text.isNullOrBlank(),
            !binding.edittextBody.text.isNullOrBlank(),
            binding.btnDatePicker.text != "Set Deadline"
        )

        return conditions.all { true }
    }

}