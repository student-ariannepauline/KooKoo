package com.sunnyside.kookoo.student.ui.fragments.admin

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.sunnyside.kookoo.R
import com.sunnyside.kookoo.databinding.FragmentEditForecastBinding
import com.sunnyside.kookoo.databinding.FragmentForecastAdminBinding
import com.sunnyside.kookoo.setAppBarTitle
import com.sunnyside.kookoo.student.data.JoinedClass
import com.sunnyside.kookoo.student.model.ForecastModel
import com.sunnyside.kookoo.student.ui.pickers.DatePicker
import com.sunnyside.kookoo.student.ui.pickers.TimePicker
import com.sunnyside.kookoo.student.ui.viewmodel.EditForecastViewModel
import com.sunnyside.kookoo.utilities.AppBarFragment
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

class EditForecastFragment : AppBarFragment() {
    private lateinit var mEditForecastViewModel : EditForecastViewModel
    private val classId = JoinedClass.joinedClass.class_id
    private val currentUser = Firebase.auth.currentUser
    private val currentClass = JoinedClass.joinedClass.name

    private var _binding: FragmentEditForecastBinding? = null
    private val binding get() = _binding!!

    private lateinit var forecastToEdit : ForecastModel
    private val args : EditForecastFragmentArgs by navArgs()

    private lateinit var meetingDate: LocalDate
    private lateinit var meetingTime: LocalTime

    /*New Forecast Variables*/
    private lateinit var title: String
    private lateinit var link: String
    private lateinit var status: String
    private lateinit var meetingDateTime: LocalDateTime


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mEditForecastViewModel = ViewModelProvider(this).get(EditForecastViewModel::class.java)
        forecastToEdit = args.forecast

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
        _binding = FragmentEditForecastBinding.inflate(inflater, container, false)
        val view = binding.root

        setupViews()

        return view
    }

    private fun setupViews() {
        setAppBarTitle("Edit Forecast")

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

        forecastToEdit.let { forecast ->

            val formatter = DateTimeFormatter.ofPattern("dd-MMMM-yyyy")
            val timeFormatter = DateTimeFormatter.ofPattern("h:m a")
            val date = forecast.meeting_time.format(formatter)
            val time = forecast.meeting_time.format(timeFormatter)

            val statusMap = mapOf(
                "CONFIRMED" to 0,
                "CANCELLED" to 1,
                "PENDING" to 2
            )

            binding.edittextTitleForecast.setText(forecast.title)
            binding.edittextOtherContentForecast.setText(forecast.link)
            binding.btnDatePicker.text = date
            binding.btnTimePicker.text = time
            spinner.setSelection(statusMap[forecast.status] as Int)

            title = forecast.title
            link = forecast.link
            status = forecast.status
            meetingDateTime = forecast.meeting_time
            meetingTime = LocalTime.from(forecast.meeting_time)
            meetingDate = LocalDate.from(forecast.meeting_time)
        }
        binding.textName.text = currentUser?.displayName
        binding.textClassroom.text = currentClass

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

    private fun save() {

        binding.edittextTitleForecast.text.isNotEmpty().apply {
            title = binding.edittextTitleForecast.text.toString()
        }

        link = binding.edittextOtherContentForecast.text.toString()
        status = binding.btnStatusPicker.selectedItem.toString()

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
                forecastToEdit.documentID,
                title,
                link,
                status,
                meetingDateTime
            )


            mEditForecastViewModel.saveForecast(newForecast, classId, forecastToEdit)

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