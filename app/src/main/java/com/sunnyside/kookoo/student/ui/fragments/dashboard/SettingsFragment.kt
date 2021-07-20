package com.sunnyside.kookoo.student.ui.fragments.dashboard

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.sunnyside.kookoo.R
import com.sunnyside.kookoo.setAppBarTitle
import com.sunnyside.kookoo.utilities.AppBarFragment

class SettingsFragment : AppBarFragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        setAppBarTitle(" ")


        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

}