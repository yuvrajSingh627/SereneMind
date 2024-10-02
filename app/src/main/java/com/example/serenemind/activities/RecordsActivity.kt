package com.example.serenemind.activities

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.serenemind.R
import com.example.serenemind.adapters.AssessmentDataAdapter
import com.example.serenemind.classes.AssessmentData
import com.example.serenemind.databinding.ActivityRecordsBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class RecordsActivity : AppCompatActivity() {

    private var binding: ActivityRecordsBinding? = null
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: AssessmentDataAdapter
    private lateinit var assessmentList: MutableList<AssessmentData>
    private lateinit var user: FirebaseUser
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRecordsBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        user = FirebaseAuth.getInstance().currentUser!!
        database = FirebaseDatabase.getInstance().reference.child("users").child(user.uid).child("assessments")

        setupToolbar()
        setupRecyclerView()
        retrieveAssessmentData()
    }
    private fun setupRecyclerView() {
        recyclerView = findViewById(R.id.rv_assessment_data)
        recyclerView.layoutManager = LinearLayoutManager(this)
        assessmentList = mutableListOf()
        adapter = AssessmentDataAdapter(this@RecordsActivity, assessmentList)
        recyclerView.adapter = adapter
    }

    private fun retrieveAssessmentData() {
        binding?.pbCircular?.visibility = View.VISIBLE
        binding?.tvFetchingData?.visibility = View.VISIBLE
        database.addValueEventListener(object : ValueEventListener {
            @SuppressLint("NotifyDataSetChanged")
            override fun onDataChange(snapshot: DataSnapshot) {
                assessmentList.clear()

                for (dataSnapshot in snapshot.children) {
                    val assessment = dataSnapshot.getValue(AssessmentData::class.java)
                    assessment?.let {
                        assessmentList.add(it)
                    }
                }

                adapter.notifyDataSetChanged()
                if(assessmentList.isEmpty()) {
                    binding?.tvRvEmpty?.visibility = View.VISIBLE
                }else   binding?.tvRvEmpty?.visibility = View.GONE
                binding?.pbCircular?.visibility = View.GONE
                binding?.tvFetchingData?.visibility = View.GONE
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

    private fun setupToolbar() {
        setSupportActionBar(binding?.tbRecords)
        supportActionBar?.title = "Records"
        binding?.tbRecords?.setTitleTextColor(resources.getColor(android.R.color.black, theme))
    }

    override fun onDestroy() {
        binding = null
        super.onDestroy()
    }
}