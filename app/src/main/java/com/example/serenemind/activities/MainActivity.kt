package com.example.serenemind.activities

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.serenemind.R
import com.example.serenemind.constants.Constants
import com.example.serenemind.databinding.ActivityMainBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class MainActivity : AppCompatActivity() {
    private var binding: ActivityMainBinding? = null
    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        auth = FirebaseAuth.getInstance()

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this@MainActivity, gso)

        binding?.flSignInGoogle?.setOnClickListener {
            signInGoogle()
        }
        binding?.flSignInGuest?.setOnClickListener {
            signInGuest()
        }
    }

    private fun signInGuest() {
        val dialog = Dialog(this@MainActivity)
        dialog.setContentView(R.layout.dialog_guest_sign_in)
        dialog.setCancelable(false)

        val btnNo = dialog.findViewById<TextView>(R.id.tv_no)
        val btnYes = dialog.findViewById<TextView>(R.id.tv_yes)

        btnNo.setOnClickListener {
            dialog.dismiss()
        }

        btnYes.setOnClickListener {
            val intent = Intent(this@MainActivity, HomeActivity::class.java)
            // Clear guest details, making sure they are empty strings (no crash potential)
            Constants.EMAIL_ID = ""
            Constants.DISPLAY_NAME = ""
            Constants.IS_GUEST = true
            dialog.dismiss()
            startActivity(intent)
            finish()
        }

        dialog.show()
    }

    private fun signInGoogle() {
        val signInIntent = googleSignInClient.signInIntent
        launcher.launch(signInIntent)
    }

    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            result ->
        if(result.resultCode == Activity.RESULT_OK) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            handleResults(task)
        }
    }

    private fun handleResults(task: Task<GoogleSignInAccount>) {
        if (task.isSuccessful) {
            val account: GoogleSignInAccount? = task.result
            if (account != null) {
                updateUI(account)
            }
        } else {
            Log.e("GoogleSignIn", "Sign-in failed: ${task.exception?.message}")
            Toast.makeText(this@MainActivity, "Google Sign-in failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
        }
    }


    private fun updateUI(account: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        auth.signInWithCredential(credential).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                // Sign in success
                val user = auth.currentUser
                if (user != null) {
                    // Set Constants for use in HomeActivity
                    Constants.EMAIL_ID = user.email ?: ""
                    Constants.DISPLAY_NAME = user.displayName ?: ""
                    Constants.IS_GUEST = false

                    // Navigate to HomeActivity
                    val intent = Intent(this, HomeActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            } else {
                // If sign-in fails, display a message to the user.
                Log.w("GoogleSignIn", "signInWithCredential:failure", task.exception)
                Toast.makeText(this, "Authentication Failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }



    override fun onDestroy() {
        binding = null
        super.onDestroy()
    }
}
