package com.sunnyside.kookoo.student.ui.fragments.admin

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.ListenerRegistration
import com.sunnyside.kookoo.R
import com.sunnyside.kookoo.databinding.FragmentTimelineAdminBinding
import com.sunnyside.kookoo.student.data.JoinedClass
import com.sunnyside.kookoo.student.ui.adapters.AnnouncementsListAdapter
import com.sunnyside.kookoo.student.ui.adapters.SwipeToDeleteAnnouncement
import com.sunnyside.kookoo.student.ui.viewmodel.TimelineViewModel

class TimelineAdminFragment : Fragment() {
    private lateinit var mTimelineViewModel: TimelineViewModel
    private lateinit var classId: String
    private lateinit var registration: ListenerRegistration

    private var _binding: FragmentTimelineAdminBinding? = null
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
        // Inflate the layout for this fragment
        _binding = FragmentTimelineAdminBinding.inflate(inflater, container, false)
        val view = binding.root
        val adapter = AnnouncementsListAdapter(mTimelineViewModel)
        val recyclerView = binding.timelineTestList

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        mTimelineViewModel.announcements.observe(viewLifecycleOwner, Observer { announcements ->
            adapter.setData(announcements)
        })

        val itemTouchHelper = ItemTouchHelper(SwipeToDeleteAnnouncement(adapter))
        itemTouchHelper.attachToRecyclerView(recyclerView)


        binding.addPostButton.setOnClickListener {
            findNavController().navigate(R.id.action_timelineAdminFragment2_to_addPostFragment2)
        }

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}