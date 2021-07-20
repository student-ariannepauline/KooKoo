package com.sunnyside.kookoo.student.ui.fragments.admin

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.sunnyside.kookoo.R
import com.sunnyside.kookoo.databinding.FragmentAddPostBinding
import com.sunnyside.kookoo.databinding.FragmentEditForecastBinding
import com.sunnyside.kookoo.databinding.FragmentEditPostBinding
import com.sunnyside.kookoo.setAppBarTitle
import com.sunnyside.kookoo.student.data.JoinedClass
import com.sunnyside.kookoo.student.model.AnnouncementModel
import com.sunnyside.kookoo.student.model.JoinedClassModel
import com.sunnyside.kookoo.student.ui.pickers.DatePicker
import com.sunnyside.kookoo.student.ui.viewmodel.AddPostViewModel
import com.sunnyside.kookoo.student.ui.viewmodel.EditPostViewModel
import com.sunnyside.kookoo.utilities.AppBarFragment
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

class EditPostFragment : AppBarFragment() {
    private var _binding: FragmentEditPostBinding? = null
    private val binding get() = _binding!!
    private val currentUser = Firebase.auth.currentUser
    private lateinit var editPostViewModel: EditPostViewModel
    private val currentClass = JoinedClass.joinedClass.name
    private val classId = JoinedClass.joinedClass.class_id

    private lateinit var postToEdit: AnnouncementModel
    private val args: EditPostFragmentArgs by navArgs()

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

        editPostViewModel = ViewModelProvider(this).get(EditPostViewModel::class.java)
        postToEdit = args.announcement
    }

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
        _binding = FragmentEditPostBinding.inflate(inflater, container, false)
        val view = binding.root

        setupViews()

        return view
    }

    private fun setupViews() {
        setAppBarTitle("Edit Post")

        postToEdit.let { post ->
            val formatter = DateTimeFormatter.ofPattern("dd-MMMM-yyyy")
            val date = post.deadline.format(formatter)

            authorName = post.author_name
            picLink = post.pic_link
            title = post.title
            body = post.body
            link = post.link
            deadline = post.deadline
            timestamp = post.timestamp

            binding.textName.text = currentUser?.displayName
            binding.textClassroom.text = currentClass
            binding.edittextTitle.setText(post.title)
            binding.edittextBody.setText(post.body)
            binding.edittextOtherContent.setText(post.link)
            binding.btnDatePicker.text = date

        }

        binding.btnDatePicker.setOnClickListener {
            val pickerFragment = DatePicker() { selectedDate ->
                binding.btnDatePicker.text = selectedDate.toString()
            }

            pickerFragment.show(childFragmentManager, "datePicker")
        }
    }

    private fun save() {
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
                postToEdit.documentID,
                authorName,
                picLink,
                title,
                body,
                link,
                deadline,
                timestamp
            )

            editPostViewModel.savePost(newPost, classId, postToEdit)
            findNavController().popBackStack()

        } catch (e: UninitializedPropertyAccessException) {
            Toast.makeText(activity, "Please fill out the form", Toast.LENGTH_LONG).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}