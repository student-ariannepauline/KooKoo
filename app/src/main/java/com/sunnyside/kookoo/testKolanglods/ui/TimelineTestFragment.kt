package com.sunnyside.kookoo.testKolanglods.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.ListenerRegistration
import com.sunnyside.kookoo.R
import com.sunnyside.kookoo.databinding.FragmentTimelineTestBinding
import com.sunnyside.kookoo.student.data.JoinedClass
import com.sunnyside.kookoo.student.model.AnnouncementModel
import com.sunnyside.kookoo.student.ui.adapters.AnnouncementsListAdapter
import com.sunnyside.kookoo.student.ui.viewmodel.TimelineViewModel
import java.time.LocalDate
import java.time.LocalDateTime.now


class TimelineTestFragment : Fragment() {
    private lateinit var mTimelineViewModel: TimelineViewModel
    private lateinit var classId: String
    private lateinit var registration: ListenerRegistration

    private var _binding: FragmentTimelineTestBinding? = null
    private val binding get() = _binding!!



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mTimelineViewModel = ViewModelProvider(this).get(TimelineViewModel::class.java)
        classId = JoinedClass.joinedClass.class_id
    }

    override fun onStart() {
        super.onStart()
        registration = mTimelineViewModel.getAnnouncements(classId)
    }

    override fun onStop() {
        super.onStop()

        registration.remove()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTimelineTestBinding.inflate(inflater, container, false)
        val view = binding.root
        val adapter = AnnouncementsListAdapter()
        val recyclerView = binding.timelineTestList

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        mTimelineViewModel.announcements.observe(viewLifecycleOwner, Observer { announcements ->
            adapter.setData(announcements)
        })


        binding.addPostButton.setOnClickListener {
            findNavController().navigate(R.id.action_timelineTestFragment2_to_addPostFragment2)
        }

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}