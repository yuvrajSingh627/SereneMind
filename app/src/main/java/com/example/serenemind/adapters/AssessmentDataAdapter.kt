package com.example.serenemind.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.serenemind.R
import com.example.serenemind.classes.AssessmentData

class AssessmentDataAdapter(private val context: Context, private val assessmentList: List<AssessmentData>)
    : RecyclerView.Adapter<AssessmentDataAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AssessmentDataAdapter.ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_assessment_data, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: AssessmentDataAdapter.ViewHolder, position: Int) {
        val data = assessmentList[position]

        holder.testName.text = data.testName
        holder.testScore.text = data.score.toString()
        holder.testDate.text = data.date
    }

    override fun getItemCount(): Int {
        return assessmentList.size
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val testName = itemView.findViewById<TextView>(R.id.tv_test_name_actual)
        val testScore = itemView.findViewById<TextView>(R.id.tv_test_score_actual)
        val testDate = itemView.findViewById<TextView>(R.id.tv_test_date_actual)
    }
}