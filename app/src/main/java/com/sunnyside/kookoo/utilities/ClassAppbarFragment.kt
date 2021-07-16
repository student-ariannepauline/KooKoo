package com.sunnyside.kookoo.utilities

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.sunnyside.kookoo.R
import com.sunnyside.kookoo.setAppBarTitle
import com.sunnyside.kookoo.student.data.JoinedClass
import com.sunnyside.kookoo.student.ui.viewmodel.TimelineViewModel

open class ClassAppbarFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setHasOptionsMenu(true)

        setAppBarTitle(JoinedClass.joinedClass.name)
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
        R.id.action_members -> {
            findNavController().navigate(R.id.membersFragment)
            true
        }
        else -> {
            super.onOptionsItemSelected(item)
        }
    }
}