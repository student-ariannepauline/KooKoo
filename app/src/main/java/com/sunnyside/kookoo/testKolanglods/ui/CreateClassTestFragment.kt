package com.sunnyside.kookoo.testKolanglods.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.sunnyside.kookoo.R
import com.sunnyside.kookoo.databinding.FragmentCreateClassTestBinding
import com.sunnyside.kookoo.databinding.FragmentProfileBinding
import com.sunnyside.kookoo.student.ui.viewmodel.DashboardViewModel

class CreateClassTestFragment : Fragment() {
    lateinit var mDashboardViewModel: DashboardViewModel
    private var _binding: FragmentCreateClassTestBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentCreateClassTestBinding.inflate(inflater, container, false)
        val view = binding.root
        mDashboardViewModel = ViewModelProvider(this).get(DashboardViewModel::class.java)
        val user = Firebase.auth.currentUser

        binding.testCreateClassBtn.setOnClickListener {
            if (user != null) {
                mDashboardViewModel.createClass(binding.testCreateClassText.text.toString(), user.uid)
            }
            findNavController().navigate(R.id.action_createClassTestFragment2_to_dashboardFragment2)
        }

        binding.testJoinBackBtn.setOnClickListener {
            findNavController().navigate(R.id.action_createClassTestFragment2_to_dashboardFragment2)
        }
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}