package com.sunnyside.kookoo.student.ui.fragments.admin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.sunnyside.kookoo.databinding.FragmentAddForecastBinding
import com.sunnyside.kookoo.student.data.JoinedClass
import com.sunnyside.kookoo.student.model.ForecastModel
import com.sunnyside.kookoo.student.model.JoinedClassModel
import com.sunnyside.kookoo.student.ui.pickers.DatePicker
import com.sunnyside.kookoo.student.ui.pickers.TimePicker
import com.sunnyside.kookoo.student.ui.viewmodel.AddForecastViewModel
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

class AddForecastFragment : Fragment() {
    private var _binding: FragmentAddForecastBinding? = null
    private val binding get() = _binding!!
    private val currentUser = Firebase.auth.currentUser
    private lateinit var addForecastViewModel: AddForecastViewModel
    private lateinit var joinedClass : JoinedClassModel
    private lateinit var classId: String
    private lateinit var meetingDate : LocalDate
    private lateinit var meetingTime : LocalTime

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentAddForecastBinding.inflate(inflater, container, false)
        addForecastViewModel = ViewModelProvider(this).get(AddForecastViewModel::class.java)
        joinedClass = JoinedClass.joinedClass
        classId = joinedClass.class_id

        val view = binding.root

        setupView()

        return view
    }

    private fun setupView() {
        binding.textName.text = currentUser?.displayName.toString()
        binding.textClassroom.text = JoinedClass.joinedClass.name

        binding.btnPost.setOnClickListener {
            post()
        }

        binding.btnDatePicker.setOnClickListener {
            val pickerFragment = DatePicker() { selectedDate ->
                meetingDate = selectedDate
                binding.btnDatePicker.text = selectedDate.toString()
            }

            pickerFragment.show(childFragmentManager, "datePicker")
        }

        binding.btnTimePicker.setOnClickListener {
            val timePickerFragment = TimePicker() { selectedTime ->
                meetingTime = selectedTime
                val formatter24 = SimpleDateFormat("HH:mm")
                val formatter12 = SimpleDateFormat("hh:mm a")
                val timeObj = formatter24.parse(selectedTime.toString())

                binding.btnTimePicker.text = formatter12.format(timeObj)
            }
            timePickerFragment.show(childFragmentManager, "timePicker")
        }
    }

    private fun post() {
        val newForecast = ForecastModel(
            "Sample",
            "Sample",
            "CONFIRMED",
            LocalDateTime.of(meetingDate, meetingTime)
        )

        addForecastViewModel.addForecast(newForecast, classId)

        findNavController().popBackStack()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}