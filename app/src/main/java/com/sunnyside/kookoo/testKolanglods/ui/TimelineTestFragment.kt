package com.sunnyside.kookoo.testKolanglods.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.type.DateTime
import com.sunnyside.kookoo.R
import com.sunnyside.kookoo.databinding.FragmentTimelineTestBinding
import com.sunnyside.kookoo.student.model.AnnouncementModel
import com.sunnyside.kookoo.student.ui.adapters.AnnouncementsListAdapter
import com.sunnyside.kookoo.student.ui.viewmodel.TimelineViewModel
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalDateTime.now
import java.util.*


class TimelineTestFragment : Fragment() {
    private lateinit var mTimelineViewModel: TimelineViewModel
    private var _binding: FragmentTimelineTestBinding? = null
    private val binding get() = _binding!!
    private lateinit var class_id: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mTimelineViewModel = ViewModelProvider(this).get(TimelineViewModel::class.java)
        class_id = "7eetxdi5uC1tg7CPH90a"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTimelineTestBinding.inflate(inflater, container, false)
        val view = binding.root
        val adapter = AnnouncementsListAdapter()
        val recyclerView = binding.timelineTestList

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        val announcements = listOf(
            AnnouncementModel(
                "Morris John",
                "wala pa",
                "History ng Bayo",
                "Sa isang buong papel, isulat ang history ng pagbabayo. Bigas ha, yung tinutktok para gilingin. Wag ka bastos please",
                " ",
                LocalDate.of(2021,6,19),
                now(),
            ),
            AnnouncementModel(
                "Morris John",
                "wala pa",
                "Dance Battle",
                "Kumuha ng isang kapartner at hamunin sa isang Dougie Battle. Siguraduhing nakapag warm-up at may suot ng proper attire",
                " ",
                LocalDate.of(2021,6,19),
                now(),
            ),
            AnnouncementModel(
                "Morris John",
                "wala pa",
                "Anatomy Project",
                "Humanap ng palaka sa inyong local bukirin. Panuorin ang naka attach na link sa kung papaano ang mag disect ng palaka. Sundan " +
                        "ang video at picturan ang inyong gawa",
                " ",
                LocalDate.of(2021,6,19),
                now(),
            )
        )

        adapter.setData(announcements)


        binding.addPostButton.setOnClickListener {
            val action = TimelineTestFragmentDirections.actionTimelineTestFragment2ToAddPostFragment2(class_id)
            findNavController().navigate(action)
        }

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}