package com.sunnyside.kookoo.student.ui.fragments.timeline

import android.app.ActionBar
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
    lateinit var navView: BottomNavigationView

    private var _binding: ActivityClassBinding? = null
    private val binding get() = _binding!!
    private val args: ClassFragmentArgs by navArgs()

    private lateinit var timelineFragment: TimelineHost
    private lateinit var forecastFragment: ForecastHost
    private lateinit var calendarFragment: CalendarFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        timelineFragment = TimelineHost()
        forecastFragment = ForecastHost()
        calendarFragment = CalendarFragment()

        JoinedClass.enterClass(args.joinedClass)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = ActivityClassBinding.inflate(inflater, container, false)
        val view = binding.root

        makeCurrentFragment(timelineFragment)

        binding.classBottomNavigationView.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.homeFragment -> makeCurrentFragment(timelineFragment)
                R.id.forecastFragment -> makeCurrentFragment(forecastFragment)
                R.id.calendarFragment -> makeCurrentFragment(calendarFragment)
            }
            true
        }


        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        activity?.actionBar?.title = "Class"
    }

    private fun makeCurrentFragment(fragment: Fragment) =
        parentFragmentManager.beginTransaction().apply {
            replace(R.id.fl_wrapper, fragment)
            commit()
        }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}