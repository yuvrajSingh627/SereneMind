package com.example.serenemind.activities

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.serenemind.R
import com.example.serenemind.databinding.ActivityAboutUsBinding

class AboutUsActivity : AppCompatActivity() {
    private var binding: ActivityAboutUsBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAboutUsBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        setupToolbar()
    }
    private fun setupToolbar() {
        setSupportActionBar(binding?.tbHome)
        supportActionBar?.title = "About"
        binding?.tbHome?.setTitleTextColor(resources.getColor(android.R.color.black, theme))
    }

    override fun onDestroy() {
        binding = null
        super.onDestroy()
    }
}