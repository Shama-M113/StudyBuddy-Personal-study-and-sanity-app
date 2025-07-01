package com.example.madaat

import com.example.madaat.R

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.TooltipCompat



class HomeDashboard : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_dashboard)
         val scheduler=findViewById<ImageView>(R.id.schedule)
        TooltipCompat.setTooltipText(scheduler, "This is smart scheduler")
        // HomeDashboard.kt
        scheduler.setOnClickListener {
            val emailFromLogin = intent.getStringExtra("email") ?: "student@example.com"
            val intentToScheduler = Intent(this, smartscheduler::class.java)
            intentToScheduler.putExtra("email", emailFromLogin)
            startActivity(intentToScheduler)
        }


        val doubt=findViewById<ImageView>(R.id.doubt)
        TooltipCompat.setTooltipText(doubt, "Have any doubt??")
        doubt.setOnClickListener {
            val intent = Intent(this, doubtsolver::class.java)
            startActivity(intent)
           // Option
        }
        val progress=findViewById<TextView>(R.id.progress)
        TooltipCompat.setTooltipText(progress, "Track your prgress")
        progress.setOnClickListener {
            val intent = Intent(this, progresstracker::class.java)
            startActivity(intent)
            // Option
        }
        val focus=findViewById<ImageView>(R.id.focus)
        TooltipCompat.setTooltipText(focus, "Focus mode")
        focus.setOnClickListener {
            val intent = Intent(this, FocusMode::class.java)
            startActivity(intent)
            // Option
        }
        val emotion=findViewById<ImageView>(R.id.emotion)
        TooltipCompat.setTooltipText(emotion, "Dump your emotion")
        emotion.setOnClickListener {
            val intent = Intent(this, Emotion::class.java)
            startActivity(intent)
            // Option
        }

        val chatbotIcon = findViewById<ImageView>(R.id.chatbot)
        TooltipCompat.setTooltipText(emotion, "Contact Support")
        chatbotIcon.setOnClickListener {
            val intent = Intent(this, ChatbotActivity::class.java)
            startActivity(intent)
            // Option
        }


    }
}