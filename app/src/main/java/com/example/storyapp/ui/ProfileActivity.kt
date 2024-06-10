package com.example.storyapp.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.storyapp.R
import com.example.storyapp.data.retrofit.ApiConfig
import com.example.storyapp.data.user.UserPref
import com.example.storyapp.data.user.UserRepository
import com.example.storyapp.data.user.dataStore
import com.example.storyapp.databinding.ActivityProfileBinding
import com.example.storyapp.login.LoginActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class ProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding
    private lateinit var profileViewModel: ProfileViewModel
    private lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViewModel()

        bottomNavigationView = findViewById(R.id.menuBar)
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home -> {
                    startActivity(Intent(this, MainActivity::class.java))
                    true
                }
                R.id.location -> {
                    startActivity(Intent(this, MapsActivity::class.java))
                    true
                }
                R.id.profile -> {
                    true
                }
                else -> false
            }
        }

        profileViewModel.getSession().observe(this) { user ->
            binding.username.text = user.name
        }

        binding.logoutButton.setOnClickListener {
            profileViewModel.logout()
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }
    }

    private fun setupViewModel() {
        val pref = UserPref.getInstance(applicationContext.dataStore)
        val repository = UserRepository.getInstance(ApiConfig.getApiService(), pref)
        profileViewModel = ViewModelProvider(this, ProfileViewModelFactory(repository))[ProfileViewModel::class.java]
    }
}