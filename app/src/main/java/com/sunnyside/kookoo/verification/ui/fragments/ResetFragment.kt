package com.sunnyside.kookoo.verification.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.sunnyside.kookoo.R
import com.sunnyside.kookoo.databinding.FragmentProfileBinding
import com.sunnyside.kookoo.databinding.FragmentResetpasswordBinding

class ResetFragment : Fragment() {
    private var _binding: FragmentResetpasswordBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentResetpasswordBinding.inflate(inflater, container, false)
        val view = binding.root

        binding.resetBtnBack.setOnClickListener {
            findNavController().navigate(R.id.action_forgotFragment_to_loginFragment)
        }

        return view
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}