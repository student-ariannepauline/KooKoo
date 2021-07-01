package com.sunnyside.kookoo.student.ui.fragments.timeline

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.ListenerRegistration
import com.sunnyside.kookoo.R
import com.sunnyside.kookoo.databinding.FragmentForecastAdminBinding
import com.sunnyside.kookoo.databinding.FragmentForecastBinding
import com.sunnyside.kookoo.setAppBarTitle
import com.sunnyside.kookoo.student.data.JoinedClass
import com.sunnyside.kookoo.student.ui.adapters.ForecastListAdapter
import com.sunnyside.kookoo.student.ui.viewmodel.ForecastViewModel

class ForecastFragment : Fragment() {
    private lateinit var mForecastViewModel: ForecastViewModel
    private lateinit var classId: String
    private lateinit var registration: ListenerRegistration

    private var _binding: FragmentForecastBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mForecastViewModel = ViewModelProvider(this).get(ForecastViewModel::class.java)
        classId = JoinedClass.joinedClass.class_id
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        inflater.inflate(R.menu.invite_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.action_invite -> {
            findNavController().navigate(R.id.inviteFragment)
            true
        }
        else -> {
            super.onOptionsItemSelected(item)
        }
    }

    override fun onStart() {
        super.onStart()
        registration = mForecastViewModel.getForecasts(classId)
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
        _binding = FragmentForecastBinding.inflate(inflater, container, false)
        val view = binding.root
        val adapter = ForecastListAdapter(mForecastViewModel) {

        }

        val recyclerView = binding.recyclerViewForecast
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        mForecastViewModel.forecasts.observe(viewLifecycleOwner, Observer { forecasts ->
            adapter.setData(forecasts)
        })

        setAppBarTitle(JoinedClass.joinedClass.name)

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}