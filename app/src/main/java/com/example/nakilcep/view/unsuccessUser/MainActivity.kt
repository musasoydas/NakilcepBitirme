package com.example.nakilcep.view.unsuccessUser

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.nakilcep.R
import com.example.nakilcep.databinding.ActivityMainBinding
import com.example.nakilcep.extensions.hide
import com.example.nakilcep.extensions.show

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //menuyu bağlama
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        navController = navHostFragment.navController

        binding.bottommenu.setupWithNavController(navHostFragment.navController)

        navHostFragment.navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.loginScreen -> binding.bottommenu.hide()

                R.id.forgotPassword -> binding.bottommenu.hide()

                R.id.newAddress -> binding.bottommenu.hide()

                R.id.registeredAddress -> binding.bottommenu.hide()

                R.id.signUpFragment -> binding.bottommenu.hide()

                else -> binding.bottommenu.show()
            }
        }
        setContentView(binding.root)
    }
    //extention function
    //top Level func


}