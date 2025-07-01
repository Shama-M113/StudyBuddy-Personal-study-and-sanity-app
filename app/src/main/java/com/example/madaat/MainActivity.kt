package com.example.madaat

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import android.util.Patterns

class MainActivity : AppCompatActivity() {
    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val loginLink = findViewById<TextView>(R.id.signin)
        loginLink.setOnClickListener {
            startActivity(Intent(this, signuppage::class.java))
        }

        val emailinput = findViewById<EditText>(R.id.EmailAddress)
        val passwordinput = findViewById<EditText>(R.id.password)
        val btn = findViewById<Button>(R.id.button)

        val dbHelper = DatabaseHelper(this)

        btn.setOnClickListener {
            val email = emailinput.text.toString().trim()
            val password = passwordinput.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please enter email and password", Toast.LENGTH_SHORT).show()
            } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(this, "Enter a valid email", Toast.LENGTH_SHORT).show()
            } else {
                val isValidUser = dbHelper.checkLogin(email, password)

                // LoginActivity.kt (after successful login)
                if (isValidUser) {
                    Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, HomeDashboard::class.java)
                    intent.putExtra("email", email)
                    startActivity(intent)
                    finish()
                }
                else {
                    Toast.makeText(this, "Invalid email or password", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
