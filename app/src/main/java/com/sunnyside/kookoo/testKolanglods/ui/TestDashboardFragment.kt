package com.sunnyside.kookoo.testKolanglods.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.sunnyside.kookoo.R
import com.sunnyside.kookoo.databinding.FragmentTestDashboardBinding
import com.sunnyside.kookoo.student.model.JoinedClassModel
import com.sunnyside.kookoo.student.ui.adapters.JoinedClassListAdapter
import com.sunnyside.kookoo.student.ui.viewmodel.DashboardViewModel


class TestDashboardFragment : Fragment() {
    lateinit var mDashboardViewModel: DashboardViewModel
    private var _binding: FragmentTestDashboardBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mDashboardViewModel = ViewModelProvider(this).get(DashboardViewModel::class.java)

        val user = Firebase.auth.currentUser

        if (user != null) {
            mDashboardViewModel.getClasses(user.uid)
        }


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // View Binding
        _binding = FragmentTestDashboardBinding.inflate(inflater, container, false)
        val view = binding.root
        val adapter = JoinedClassListAdapter(){

        }
        val recyclerView = binding.joinedClassesList

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())


        mDashboardViewModel.joinedClassesModel.observe(viewLifecycleOwner, Observer{ classes ->
            adapter.setData(classes)
        })


        binding.testLogout.setOnClickListener {
            Firebase.auth.signOut()
            findNavController().navigate(R.id.action_testDashboardFragment_to_verificationActivity2)
        }

        binding.testJoinClass.setOnClickListener {
            findNavController().navigate(R.id.action_testDashboardFragment_to_joinClassTestFragment)
        }

        binding.testCreateClass.setOnClickListener {
            findNavController().navigate(R.id.action_testDashboardFragment_to_createClassTestFragment)
        }

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}