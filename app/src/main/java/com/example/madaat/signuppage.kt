package com.example.madaat

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import android.content.Intent
import android.util.Log
import android.util.Patterns


class signuppage : AppCompatActivity() {
    private lateinit var dbHelper: DatabaseHelper
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signuppage)
        dbHelper = DatabaseHelper(this)
        val loginLink = findViewById<TextView>(R.id.loginLink)
        loginLink.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        val btn = findViewById<Button>(R.id.button2)
        val usernameField = findViewById<EditText>(R.id.username)
        val emailField = findViewById<EditText>(R.id.singup_email)
        val passwordField = findViewById<EditText>(R.id.singup_password)
        val confirmPasswordField = findViewById<EditText>(R.id.singup_confirm_password)

        btn.setOnClickListener {
            val username = usernameField.text.toString().trim()
            val email = emailField.text.toString().trim()
            val password = passwordField.text.toString()
            val confirmPassword = confirmPasswordField.text.toString()

            if (validateSignUpForm(username, email, password, confirmPassword)) {
                val success = dbHelper.insertUser(username, email, password)
                if (success) {
                    showToast("Signup successful")
                    startActivity(Intent(this, HomeDashboard::class.java))
                    finish()
                } else {
                    showToast("Sign Up failed: Email may already exist")
                }
            }
        }
        val allUsers = dbHelper.getAllUsers()
        for (user in allUsers) {
            Log.d("DB_USER", user)
        }

    }

    private fun validateSignUpForm(
        username: String,
        email: String,
        password: String,
        confirmPassword: String
    ): Boolean {
        if (username.isEmpty()) {
            showToast("Username is required")
            return false
        }

        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            showToast("Enter a valid email")
            return false
        }

        if (password.length < 6) {
            showToast("Password must be at least 6 characters")
            return false
        }

        if (password != confirmPassword) {
            showToast("Passwords do not match")
            return false
        }

        return true
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

}
