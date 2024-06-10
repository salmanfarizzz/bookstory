package com.example.storyapp.data.user
data class User(
    val email: String,
    val token: String,
    val name: String,
    val isLogin: Boolean = false
)