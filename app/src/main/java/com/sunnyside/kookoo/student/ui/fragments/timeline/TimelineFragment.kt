package com.sunnyside.kookoo.student.ui.fragments.timeline

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import com.sunnyside.kookoo.R
import com.sunnyside.kookoo.databinding.FragmentTimelineBinding
import com.sunnyside.kookoo.setAppBarTitle
import com.sunnyside.kookoo.student.data.JoinedClass
import com.sunnyside.kookoo.student.ui.adapters.AnnouncementsListAdapter
import com.sunnyside.kookoo.student.ui.viewmodel.TimelineViewModel
import com.sunnyside.kookoo.utilities.ClassAppbarFragment

class TimelineFragment : ClassAppbarFragment() {
    private lateinit var mTimelineViewModel: TimelineViewModel
    private lateinit var classId: String
    private lateinit var registration: ListenerRegistration
    private lateinit var storage : FirebaseStorage

    private var _binding: FragmentTimelineBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mTimelineViewModel = ViewModelProvider(this).get(TimelineViewModel::class.java)
        classId = JoinedClass.joinedClass.class_id
        storage = Firebase.storage

        setHasOptionsMenu(true)
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
        // Inflate the layout for this fragment
        _binding = FragmentTimelineBinding.inflate(inflater, container, false)
        val view = binding.root
        val adapter = AnnouncementsListAdapter(mTimelineViewModel, storage)
        val recyclerView = binding.timelineRecyclerView

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())


        mTimelineViewModel.announcements.observe(viewLifecycleOwner, Observer { announcements ->
            adapter.setData(announcements)
        })


        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}