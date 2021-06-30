package com.sunnyside.kookoo.student.ui.fragments.dashboard

import android.R.attr.label
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import com.sunnyside.kookoo.databinding.FragmentInviteCodeBinding
import com.sunnyside.kookoo.setAppBarTitle
import com.sunnyside.kookoo.student.data.JoinedClass


class InviteFragment: Fragment() {

    private var _binding: FragmentInviteCodeBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentInviteCodeBinding.inflate(inflater, container, false)
        val view = binding.root

        setupView()

        return view
    }

    private fun setupView() {
        binding.textInvite.text = "Invite Users to ${JoinedClass.joinedClass.name}"
        binding.textInviteCode.text = JoinedClass.joinedClass.class_id

        setAppBarTitle("Invite using Code")

        val clipboard = activity?.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("tite", binding.textInviteCode.text)

        binding.btnCopy.setOnClickListener {
            clipboard.setPrimaryClip(clip)
            Toast.makeText(activity,"Invite Code Copied",Toast.LENGTH_LONG).show()
        }


    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }



}