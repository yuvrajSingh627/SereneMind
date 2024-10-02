package com.example.serenemind.activities

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.serenemind.R
import com.example.serenemind.adapters.TestsAdapter
import com.example.serenemind.constants.Constants
import com.example.serenemind.databinding.ActivityTestingBinding

class TestingActivity : AppCompatActivity() {
    private var binding: ActivityTestingBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTestingBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        setupToolbar()

        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        val recyclerView = binding?.rvTests // Reference to your RecyclerView in the layout
        val adapter = TestsAdapter(
            this,
            Constants.testNames
        ) // Replace 'yourTestDataList' with your actual data

        recyclerView?.adapter = adapter
        recyclerView?.layoutManager = LinearLayoutManager(this)
    }

    private fun setupToolbar() {
        setSupportActionBar(binding?.tbTest)
        supportActionBar?.title = "Assess yourself"
        binding?.tbTest?.setTitleTextColor(resources.getColor(android.R.color.black, theme))
    }

    override fun onDestroy() {
        binding = null
        super.onDestroy()
    }
}