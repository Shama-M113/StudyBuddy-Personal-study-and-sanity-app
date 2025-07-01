package com.example.madaat


import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.os.Bundle
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class Emotion : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_emotion2)

        val inputField = findViewById<EditText>(R.id.emotional_input)
        val letItOutButton = findViewById<Button>(R.id.let_it_out_button)
        val calmingMessage = findViewById<TextView>(R.id.calming_message)

        letItOutButton.setOnClickListener {
            // Disable button to prevent multiple clicks
            letItOutButton.isEnabled = false

            // Animate the EditText to scale up & fade out like a "bubble"
            inputField.animate()
                .scaleX(1.5f)
                .scaleY(1.5f)
                .alpha(0f)
                .setDuration(800)
                .setInterpolator(AccelerateDecelerateInterpolator())
                .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        // Reset animation properties
                        inputField.text.clear()
                        inputField.alpha = 1f
                        inputField.scaleX = 1f
                        inputField.scaleY = 1f

                        // Optionally hide the EditText
                        inputField.visibility = View.GONE

                        // Show calming message
                        calmingMessage.text = "Take a deep breath. You’re doing great."
                        calmingMessage.visibility = View.VISIBLE

                        // Re-enable the button if needed
                        letItOutButton.isEnabled = true
                    }
                })
                .start()
        }
    }
}
