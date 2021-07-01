package com.sunnyside.kookoo.student.ui.fragments.admin

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.sunnyside.kookoo.R
import com.sunnyside.kookoo.databinding.ActivityClassBinding
import com.sunnyside.kookoo.databinding.FragmentClassAdminBinding
import com.sunnyside.kookoo.student.data.JoinedClass
import com.sunnyside.kookoo.student.ui.fragments.timeline.*

class ClassAdminFragment : Fragment() {
    private var _binding: FragmentClassAdminBinding? = null
    private val binding get() = _binding!!
    private val args: ClassAdminFragmentArgs by navArgs()

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
        _binding = FragmentClassAdminBinding.inflate(inflater, container, false)
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