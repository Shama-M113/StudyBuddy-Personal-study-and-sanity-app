package com.example.madaat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.net.Uri
import android.widget.Button

class progresstracker : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_progresstracker)
        findViewById<Button>(R.id.btnLeetCode).setOnClickListener {
            openWebsite("https://leetcode.com")
        }

        findViewById<Button>(R.id.btnHackerRank).setOnClickListener {
            openWebsite("https://www.hackerrank.com")
        }

        findViewById<Button>(R.id.btnGeeksForGeeks).setOnClickListener {
            openWebsite("https://www.geeksforgeeks.org")
        }

        findViewById<Button>(R.id.btnCodeforces).setOnClickListener {
            openWebsite("https://codeforces.com")
        }

        findViewById<Button>(R.id.btnInterviewBit).setOnClickListener {
            openWebsite("https://www.interviewbit.com")
        }

        findViewById<Button>(R.id.btnW3Schools).setOnClickListener {
            openWebsite("https://www.w3schools.com")
        }

        findViewById<Button>(R.id.btnStackOverflow).setOnClickListener {
            openWebsite("https://stackoverflow.com")
        }
    }

    private fun openWebsite(url: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(intent)

    }
}