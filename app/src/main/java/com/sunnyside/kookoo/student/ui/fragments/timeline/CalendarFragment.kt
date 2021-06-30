package com.sunnyside.kookoo.student.ui.fragments.timeline

import android.icu.util.Calendar
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.sunnyside.kookoo.R
import com.sunnyside.kookoo.databinding.ActivityClassBinding
import com.sunnyside.kookoo.databinding.FragmentCalendarBinding
import com.sunnyside.kookoo.setAppBarTitle
import com.sunnyside.kookoo.student.data.JoinedClass
import com.sunnyside.kookoo.student.model.CalendarAnnouncementModel
import com.sunnyside.kookoo.student.model.CalendarForecastModel
import com.sunnyside.kookoo.student.ui.adapters.CalendarListAdapter
import com.sunnyside.kookoo.student.ui.viewmodel.CalendarViewModel
import kotlinx.android.synthetic.main.fragment_calendar.*
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

class CalendarFragment : Fragment() {
    private lateinit var dateId : String
    private lateinit var calendar : Calendar
    private lateinit var formatter : DateTimeFormatter
    private lateinit var mCalendarViewModel: CalendarViewModel
    private lateinit var classId: String

    private var _binding: FragmentCalendarBinding? = null
    private val binding get() = _binding!!


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mCalendarViewModel = ViewModelProvider(this).get(CalendarViewModel::class.java)
        classId = JoinedClass.joinedClass.class_id

        calendar = Calendar.getInstance()

        formatter = DateTimeFormatter.ofPattern("dd-MMMM-yyyy")
        setDate()
        getEvents()


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentCalendarBinding.inflate(inflater, container, false)
        val view = binding.root
        val adapter = CalendarListAdapter()
        val recyclerView = binding.listEvents
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())


        binding.calendarView.setOnDateChangeListener { view, year, month, dayOfMonth ->
            calendar.set(year, month, dayOfMonth)
            binding.calendarView.date = calendar.timeInMillis
            setDate()
            getEvents()
        }

        mCalendarViewModel.events.observe(viewLifecycleOwner, Observer { events ->
            adapter.setData(events)
        })

        setAppBarTitle(JoinedClass.joinedClass.name)


        return view
    }

    private fun getEvents() {
        mCalendarViewModel.getEvents(classId, dateId)
        Log.d("tite", "classId : $classId, dateId: $dateId")
    }

    private fun setDate() {
        dateId = LocalDate.from(
            calendar.time.toInstant().atZone(ZoneId.systemDefault())
                .toLocalDate()
        ).format(formatter)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}