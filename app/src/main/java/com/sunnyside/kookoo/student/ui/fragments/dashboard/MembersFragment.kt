package com.sunnyside.kookoo.student.ui.fragments.dashboard

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.ListenerRegistration
import com.sunnyside.kookoo.R
import com.sunnyside.kookoo.databinding.FragmentMembersBinding
import com.sunnyside.kookoo.databinding.FragmentTimelineBinding
import com.sunnyside.kookoo.setAppBarTitle
import com.sunnyside.kookoo.student.data.JoinedClass
import com.sunnyside.kookoo.student.model.JoinedClassModel
import com.sunnyside.kookoo.student.model.MemberModel
import com.sunnyside.kookoo.student.ui.adapters.MembersListAdapter
import com.sunnyside.kookoo.student.ui.viewmodel.MembersViewModel
import com.sunnyside.kookoo.utilities.AppBarFragment


class MembersFragment : AppBarFragment() {
    private var _binding: FragmentMembersBinding? = null
    private val binding get() = _binding!!
    private lateinit var membersViewModel: MembersViewModel
    private lateinit var classId : String
    private lateinit var joinedClass : JoinedClassModel
    private lateinit var registration: ListenerRegistration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        membersViewModel = ViewModelProvider(this).get(MembersViewModel::class.java)

        joinedClass = JoinedClass.joinedClass
        classId = joinedClass.class_id

    }

    override fun onStart() {
        super.onStart()
        registration = membersViewModel.getMembers(classId)
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
        _binding = FragmentMembersBinding.inflate(inflater, container, false)
        val view = binding.root
        val adapter = MembersListAdapter {

        }
        val recyclerView = binding.membersRecyclerView


        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        membersViewModel.membersList.observe(viewLifecycleOwner, Observer { members ->
            adapter.setData(members)
        })

        setAppBarTitle("Members of ${joinedClass.name}")

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}