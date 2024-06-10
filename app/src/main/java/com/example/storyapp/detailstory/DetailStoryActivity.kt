package com.example.storyapp.detailstory

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.bumptech.glide.Glide
import com.example.storyapp.data.Result
import com.example.storyapp.databinding.ActivityDetailStoryBinding
import com.example.storyapp.ui.ViewModelFactory
import com.example.storyapp.ui.MainViewModel



class DetailStoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailStoryBinding
    private val viewModel by viewModels<DetailViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private val mainViewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(this)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)



        val receiveId = intent.getStringExtra("id_user").toString()
        mainViewModel.getSession().observe(this) { user ->
            viewModel.getDetail(user.token, receiveId)
        }

        viewModel.detailResult.observe(this){ result ->
            when(result){
                is Result.Loading -> {}
                is Result.Success -> {
                    binding.apply {
                        tvDetailName.text = result.data.name
                        tvDetailDescription.text = result.data.description
                    }
                    Glide.with(this).load(result.data.photoUrl).into(binding.ivDetailPhoto)
                }
                is Result.Error -> {}
            }
        }

    }
    companion object {
        const val NAME = "name"
        const val DESC = "description"
        const val URL = "url"
    }
}