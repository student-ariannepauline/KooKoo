package com.sunnyside.kookoo.student.ui.fragments.admin

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.ListenerRegistration
import com.sunnyside.kookoo.R
import com.sunnyside.kookoo.databinding.FragmentForecastAdminBinding
import com.sunnyside.kookoo.databinding.FragmentTimelineTestBinding
import com.sunnyside.kookoo.student.data.JoinedClass
import com.sunnyside.kookoo.student.model.ForecastModel
import com.sunnyside.kookoo.student.ui.adapters.ForecastListAdapter
import com.sunnyside.kookoo.student.ui.adapters.SwipeToDeleteForecast
import com.sunnyside.kookoo.student.ui.viewmodel.ForecastViewModel
import java.time.LocalDate
import java.time.LocalDateTime


class ForecastAdminFragment : Fragment() {
    private lateinit var mForecastViewModel: ForecastViewModel
    private lateinit var classId: String
    private lateinit var registration: ListenerRegistration

    private var _binding: FragmentForecastAdminBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mForecastViewModel = ViewModelProvider(this).get(ForecastViewModel::class.java)
        classId = JoinedClass.joinedClass.class_id
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
        _binding = FragmentForecastAdminBinding.inflate(inflater, container, false)
        val view = binding.root
        val adapter = ForecastListAdapter(mForecastViewModel) { forecast ->
            val action = ForecastAdminFragmentDirections.actionForecastAdminFragment2ToEditForecastFragment(forecast)
            findNavController().navigate(action)
        }

        val recyclerView = binding.recyclerViewForecast
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())


        mForecastViewModel.forecasts.observe(viewLifecycleOwner, Observer { forecasts ->
            adapter.setData(forecasts)
        })

        binding.btnAddForecast.setOnClickListener {
            findNavController().navigate(R.id.action_forecastAdminFragment2_to_addForecastFragment)
        }

        val itemTouchHelper = ItemTouchHelper(SwipeToDeleteForecast(adapter))
        itemTouchHelper.attachToRecyclerView(recyclerView)

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}