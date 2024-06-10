package com.example.storyapp.ui


import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.*
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.storyapp.R
import com.example.storyapp.addstory.AddStoryActivity
import com.example.storyapp.databinding.ActivityMainBinding
import com.example.storyapp.detailstory.DetailStoryActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    private val viewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: ListStoryAdapter
    private lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.getSession().observe(this) { user ->
            if (!user.isLogin) {
                startActivity(Intent(this, WelcomeActivity::class.java))
                finish()
            } else {
                getData(user.token)
                setupAction(user.token)
            }
        }

        setupView()
        supportActionBar?.show()

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
                    startActivity(Intent(this, ProfileActivity::class.java))
                    true
                }
                else -> false
            }
        }

        adapter = ListStoryAdapter()
        adapter.notifyDataSetChanged()
        adapter.setOnClickCallback(object : ListStoryAdapter.OnItemClickCallback {
            override fun onItemClicked(data: Story) {
                Intent(this@MainActivity, DetailStoryActivity::class.java).also {
                    it.putExtra(DetailStoryActivity.NAME, data.name)
                    it.putExtra(DetailStoryActivity.DESC, data.description)
                    it.putExtra(DetailStoryActivity.URL, data.photoUrl)
                    startActivity(it)
                }
            }
        })
        binding.apply {
            rvMain.layoutManager = LinearLayoutManager(this@MainActivity)
            rvMain.setHasFixedSize(true)
            rvMain.adapter = adapter
        }
    }

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.btn_add -> {
                startActivity(Intent(this@MainActivity, AddStoryActivity::class.java))
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.pengaturan_menu, menu)
        return true
    }

    private fun setupAction(token: String) {
        viewModel.story(token).observe(this) { user ->
            if (user != null) {
                binding.mainPb.visibility = View.GONE
                val adapter = ListStoryAdapter()
                binding.rvMain.layoutManager = LinearLayoutManager(this)
                adapter.submitData(lifecycle, user)
                binding.rvMain.adapter = adapter
            }
        }
    }

    private fun getData(token: String) {
        val adapter = ListStoryAdapter()
        binding.rvMain.adapter = adapter.withLoadStateFooter(
            footer = LoadingStateAdapter {
                adapter.retry()
            }
        )
        viewModel.story(token).observe(this) { pagingData ->
            adapter.submitData(lifecycle, pagingData)
        }
    }
}
