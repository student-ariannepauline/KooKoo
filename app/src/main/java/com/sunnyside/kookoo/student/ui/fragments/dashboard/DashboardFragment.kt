package com.sunnyside.kookoo.student.ui.fragments.dashboard

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.sunnyside.kookoo.R
import com.sunnyside.kookoo.databinding.FragmentDashboardBinding
import com.sunnyside.kookoo.databinding.FragmentNavDashboardBinding
import com.sunnyside.kookoo.databinding.FragmentTestDashboardBinding
import com.sunnyside.kookoo.student.ui.adapters.JoinedClassListAdapter
import com.sunnyside.kookoo.student.ui.viewmodel.DashboardViewModel


class DashboardFragment : Fragment() {
    lateinit var mDashboardViewModel: DashboardViewModel
    lateinit var userName: String

    private var _binding: FragmentNavDashboardBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mDashboardViewModel = ViewModelProvider(this).get(DashboardViewModel::class.java)

        val user = Firebase.auth.currentUser

        if (user != null) {
            userName = user.displayName.toString()
        }

        if (user != null) {
            mDashboardViewModel.getClasses(user.uid)
        }


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentNavDashboardBinding.inflate(inflater, container, false)
        val view = binding.root
        val adapter = JoinedClassListAdapter() { joinedClass ->
            val action = DashboardFragmentDirections.actionDashboardFragment2ToClassFragment2(joinedClass)
            findNavController().navigate(action)
        }
        val recyclerView = binding.joinedClassesList

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.textGreeting.text = "Hi, $userName"

        mDashboardViewModel.joinedClassesModel.observe(viewLifecycleOwner, Observer { classes ->
            if (classes != null) {
                binding.textLookslike.isVisible = false
                binding.imgDashCharacter.isVisible = false
            }
            adapter.setData(classes)
        })

        binding.btnJoinClass.setOnClickListener {
            findNavController().navigate(R.id.action_dashboardFragment2_to_joinClassTestFragment2)
        }

        binding.btnCreateClass.setOnClickListener {
            findNavController().navigate(R.id.action_dashboardFragment2_to_createClassTestFragment2)
        }

        return view

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}