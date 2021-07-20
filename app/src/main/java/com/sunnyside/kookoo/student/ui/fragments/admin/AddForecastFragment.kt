package com.sunnyside.kookoo.student.ui.fragments.admin

import android.os.Bundle
import android.view.*
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.sunnyside.kookoo.R
import com.sunnyside.kookoo.databinding.FragmentAddForecastBinding
import com.sunnyside.kookoo.hideKeyboard
import com.sunnyside.kookoo.setAppBarTitle
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
import java.time.format.DateTimeParseException

class AddForecastFragment : Fragment() {
    private var _binding: FragmentAddForecastBinding? = null
    private val binding get() = _binding!!
    private val currentUser = Firebase.auth.currentUser
    private lateinit var addForecastViewModel: AddForecastViewModel
    private lateinit var joinedClass: JoinedClassModel
    private lateinit var classId: String
    private lateinit var meetingDate: LocalDate
    private lateinit var meetingTime: LocalTime

    /*New Forecast Variables*/
    private lateinit var title: String
    private lateinit var link: String
    private lateinit var status: String
    private lateinit var meetingDateTime: LocalDateTime

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
        _binding = FragmentAddForecastBinding.inflate(inflater, container, false)
        addForecastViewModel = ViewModelProvider(this).get(AddForecastViewModel::class.java)
        joinedClass = JoinedClass.joinedClass
        classId = joinedClass.class_id


        val view = binding.root

        setupView()
        setAppBarTitle("Add Forecast")

        return view
    }

    private fun setupView() {
        binding.textName.text = currentUser?.displayName.toString()
        binding.textClassroom.text = JoinedClass.joinedClass.name

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

        val spinner : Spinner = binding.btnStatusPicker

        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.status_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            spinner.adapter = adapter
        }


        hideKeyboard(binding.edittextTitleForecast)
        hideKeyboard(binding.edittextOtherContentForecast)



    }

    private fun post() {

        binding.edittextTitleForecast.text.isNotEmpty().apply {
            title = binding.edittextTitleForecast.text.toString()
        }

        link = binding.edittextOtherContentForecast.text.toString()
        val status = binding.btnStatusPicker.selectedItem.toString()

        try {
            meetingDateTime = LocalDateTime.of(meetingDate, meetingTime)
        } catch (e: DateTimeParseException) {
            Toast.makeText(activity, "Please select a meeting date and time", Toast.LENGTH_LONG)
                .show()
        } catch (e: UninitializedPropertyAccessException) {
            Toast.makeText(activity, "Please select a meeting date and time", Toast.LENGTH_LONG)
                .show()
        }

        try {
            val newForecast = ForecastModel(
                "sksksks",
                title,
                link,
                status,
                meetingDateTime
            )


            addForecastViewModel.addForecast(newForecast, classId)

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