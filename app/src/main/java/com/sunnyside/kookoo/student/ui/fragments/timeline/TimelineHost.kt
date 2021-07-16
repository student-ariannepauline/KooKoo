package com.sunnyside.kookoo.student.ui.fragments.timeline

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseUser
import com.sunnyside.kookoo.R
import com.sunnyside.kookoo.databinding.ActivityStudentBinding
import com.sunnyside.kookoo.databinding.FragmentInviteCodeBinding
import com.sunnyside.kookoo.setAppBarTitle
import com.sunnyside.kookoo.student.data.JoinedClass
import com.sunnyside.kookoo.student.model.JoinedClassModel
import com.sunnyside.kookoo.utilities.ClassAppbarFragment


class TimelineHost : ClassAppbarFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_timeline_host, container, false)
    }


}