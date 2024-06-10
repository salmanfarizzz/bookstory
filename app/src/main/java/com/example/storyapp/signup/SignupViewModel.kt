package com.example.storyapp.signup

import androidx.lifecycle.ViewModel
import com.example.storyapp.data.user.UserRepository

class SignupViewModel(private val repository: UserRepository): ViewModel() {
    fun register(name: String, email: String, password: String) = repository.signup(name, email, password)
}