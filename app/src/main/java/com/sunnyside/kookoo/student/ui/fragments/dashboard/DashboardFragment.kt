package com.sunnyside.kookoo.student.ui.fragments.dashboard

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ListenerRegistration
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
    lateinit var registration : ListenerRegistration

    private var _binding: FragmentNavDashboardBinding? = null
    private val binding get() = _binding!!

    override fun onStart() {
        super.onStart()
        val user = Firebase.auth.currentUser

        if (user != null) {
            registration = mDashboardViewModel.getClasses(user.uid)
        }
    }

    override fun onStop() {
        super.onStop()
        registration.remove()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mDashboardViewModel = ViewModelProvider(this).get(DashboardViewModel::class.java)

        val user = Firebase.auth.currentUser

        if (user != null) {
            userName = user.displayName.toString()
        }

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
        _binding = FragmentNavDashboardBinding.inflate(inflater, container, false)
        val view = binding.root
        val adapter = JoinedClassListAdapter() { joinedClass ->
            if (!joinedClass.is_admin) {
                val action = DashboardFragmentDirections.actionDashboardFragment2ToClassFragment2(joinedClass)
                findNavController().navigate(action)
            } else {
                val action = DashboardFragmentDirections.actionDashboardFragment2ToClassAdminFragment(joinedClass)
                findNavController().navigate(action)
            }

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