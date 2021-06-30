package com.sunnyside.kookoo.student.ui.fragments.timeline

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.sunnyside.kookoo.R
import com.sunnyside.kookoo.setAppBarTitle
import com.sunnyside.kookoo.student.data.JoinedClass

class ForecastHost : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        setAppBarTitle(JoinedClass.joinedClass.name)
        return inflater.inflate(R.layout.fragment_forecast_host, container, false)
    }


}