package com.sunnyside.kookoo.student.ui.fragments.admin

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.sunnyside.kookoo.R
import com.sunnyside.kookoo.databinding.FragmentAddPostBinding
import com.sunnyside.kookoo.databinding.FragmentTimelineTestBinding


class AddPostFragment : Fragment() {
    private var _binding: FragmentAddPostBinding? = null
    private val binding get() = _binding!!
    private val args: AddPostFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentAddPostBinding.inflate(inflater, container, false)
        val view = binding.root
        val classId = args.classId

        Toast.makeText(
            context, classId,
            Toast.LENGTH_SHORT
        ).show()

        binding.btnPost.setOnClickListener{
            post()
        }

        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }

        return view
    }

    private fun post() {
        findNavController().popBackStack()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}