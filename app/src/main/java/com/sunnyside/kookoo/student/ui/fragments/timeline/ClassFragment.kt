package com.sunnyside.kookoo.student.ui.fragments.timeline

import android.app.ActionBar
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.sunnyside.kookoo.R
import com.sunnyside.kookoo.databinding.ActivityClassBinding
import com.sunnyside.kookoo.databinding.FragmentClassBinding
import com.sunnyside.kookoo.databinding.FragmentProfileBinding
import com.sunnyside.kookoo.student.data.JoinedClass
import com.sunnyside.kookoo.testKolanglods.ui.TimelineTestFragment
import kotlinx.android.synthetic.main.card_layout_joined_class_test.*

class ClassFragment() : Fragment() {
    private var _binding: ActivityClassBinding? = null
    private val binding get() = _binding!!
    private val args: ClassFragmentArgs by navArgs()

    private lateinit var timelineFragment: TimelineFragment
    private lateinit var forecastFragment: ForecastFragment
    private lateinit var calendarFragment: CalendarFragment

    private lateinit var currentFragment : Fragment
    private lateinit var selectedFragmentString : String
    private lateinit var fragmentMap : Map<String, Fragment>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        timelineFragment = TimelineFragment()
        forecastFragment = ForecastFragment()
        calendarFragment = CalendarFragment()

        fragmentMap = mapOf(
            "timeline" to timelineFragment,
            "forecast" to forecastFragment,
            "calendar" to calendarFragment
        )

        selectedFragmentString = "timeline"
        currentFragment = fragmentMap[selectedFragmentString] as Fragment

        makeCurrentFragment(currentFragment, selectedFragmentString)

        JoinedClass.enterClass(args.joinedClass)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = ActivityClassBinding.inflate(inflater, container, false)
        val view = binding.root

        binding.classBottomNavigationView.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.homeFragment -> makeCurrentFragment(timelineFragment, "timeline")
                R.id.forecastFragment -> makeCurrentFragment(forecastFragment, "forecast")
                R.id.calendarFragment -> makeCurrentFragment(calendarFragment, "calendar")
            }
            true
        }


        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        activity?.actionBar?.title = "Class"
    }


    private fun makeCurrentFragment(fragment: Fragment, fragmentString : String) {
        parentFragmentManager.beginTransaction().apply {
            replace(R.id.fl_wrapper, fragment)
            commit()
        }

        selectedFragmentString = fragmentString
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}