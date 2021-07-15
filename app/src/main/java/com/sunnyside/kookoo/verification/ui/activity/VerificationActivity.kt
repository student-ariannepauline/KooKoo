package com.sunnyside.kookoo.verification.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.sunnyside.kookoo.R
import com.sunnyside.kookoo.databinding.ActivityVerificationBinding

lateinit var binding : ActivityVerificationBinding
private lateinit var navController: NavController
private lateinit var appBarConfiguration: AppBarConfiguration

class VerificationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVerificationBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)


        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        navController = navHostFragment.navController
        appBarConfiguration = AppBarConfiguration(navController.graph)

        setSupportActionBar(binding.myToolbar)
        binding.myToolbar.setupWithNavController(
            navController,
            appBarConfiguration
        )



    }
}