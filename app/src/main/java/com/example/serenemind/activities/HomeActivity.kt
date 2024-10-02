package com.example.serenemind.activities

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.serenemind.R
import com.example.serenemind.constants.Constants
import com.example.serenemind.databinding.ActivityHomeBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth

class HomeActivity : AppCompatActivity(), View.OnClickListener {
    private var binding: ActivityHomeBinding? = null
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        auth = FirebaseAuth.getInstance()

        setupToolbar()
        setupOnClickListeners()

        if (Constants.IS_GUEST) {
            setupActivityForGuest()
        } else {
            setupActivityForSignedInUser()
        }
    }


    private fun setupActivityForGuest() {
        val cardViewAI = binding?.cvAi
        val cardViewRecords = binding?.cvRecords

        val disabledAlpha = 0.5f

        cardViewAI?.alpha = disabledAlpha
        cardViewRecords?.alpha = disabledAlpha

        cardViewAI?.isClickable = false
        cardViewRecords?.isClickable = false

        val tintColor = ContextCompat.getColorStateList(this@HomeActivity, R.color.gray)
        cardViewAI?.backgroundTintList = tintColor
        cardViewRecords?.backgroundTintList = tintColor
    }

    // Enable features for signed-in users
    private fun setupActivityForSignedInUser() {
        val cardViewAI = binding?.cvAi
        val cardViewRecords = binding?.cvRecords

        cardViewAI?.alpha = 1.0f
        cardViewRecords?.alpha = 1.0f

        cardViewAI?.isClickable = true
        cardViewRecords?.isClickable = true

        val defaultColor = ContextCompat.getColorStateList(this@HomeActivity, R.color.variant4)
        cardViewAI?.backgroundTintList = defaultColor
        cardViewRecords?.backgroundTintList = defaultColor
    }


    private fun setupOnClickListeners() {
        binding?.cvTestYourself?.setOnClickListener(this@HomeActivity)
        binding?.cvAi?.setOnClickListener(this@HomeActivity)
        binding?.cvRecords?.setOnClickListener(this@HomeActivity)
    }

    private fun setupToolbar() {
        setSupportActionBar(binding?.tbHome)

        val displayName = if (Constants.IS_GUEST) "Guest" else Constants.DISPLAY_NAME
        supportActionBar?.title = "Welcome, $displayName!"
        binding?.tbHome?.overflowIcon?.setTint(resources.getColor(android.R.color.black, theme))
        binding?.tbHome?.setTitleTextColor(resources.getColor(android.R.color.black, theme))
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_home, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.about -> {
                val intent = Intent(this@HomeActivity, AboutUsActivity::class.java)
                startActivity(intent)
            }
            R.id.sign_out -> {
                if(Constants.IS_GUEST) {
                    navigateToMainActivity()
                } else {
                    signOutUser()
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun navigateToMainActivity() {
        val intent = Intent(this@HomeActivity, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun signOutUser() {
        Constants.IS_GUEST = true
        Constants.DISPLAY_NAME = ""
        Constants.EMAIL_ID = ""

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        val googleSignInClient = GoogleSignIn.getClient(this, gso)

        googleSignInClient.signOut().addOnCompleteListener { signOutTask ->
            if (signOutTask.isSuccessful) {
                FirebaseAuth.getInstance().signOut()
                navigateToMainActivity()
            } else {
                Toast.makeText(
                    this@HomeActivity,
                    "Failed to sign out: ${signOutTask.exception?.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    override fun onDestroy() {
        binding = null
        super.onDestroy()
    }

    override fun onClick(view: View?) {
        when(view?.id) {
            R.id.cv_test_yourself -> {
                val intent = Intent(this@HomeActivity, TestingActivity::class.java)
                startActivity(intent)
            }
            R.id.cv_records -> {
                val intent = Intent(this@HomeActivity, RecordsActivity::class.java)
                startActivity(intent)
            }
            R.id.cv_ai -> {
                val intent = Intent(this@HomeActivity, AIActivity::class.java)
                startActivity(intent)
            }
        }
    }
}
