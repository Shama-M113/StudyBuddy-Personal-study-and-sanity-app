package com.example.madaat

import android.app.TimePickerDialog
import android.media.MediaPlayer
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import java.util.*
import java.util.concurrent.TimeUnit

class FocusMode : AppCompatActivity() {

    private lateinit var timerText: TextView
    private lateinit var startBtn: Button
    private lateinit var pauseBtn: Button
    private lateinit var resetBtn: Button
    private lateinit var musicCard: LinearLayout
    private lateinit var nowPlayingText: TextView

    private var timer: CountDownTimer? = null
    private var timeLeftInMillis: Long = 25 * 60 * 1000 // default 25 mins
    private var isRunning = false
    private var mediaPlayer: MediaPlayer? = null
    private var initialTimeInMillis: Long = timeLeftInMillis

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_focus_mode)

        timerText = findViewById(R.id.countdown_timer)
        startBtn = findViewById(R.id.start_button)
        pauseBtn = findViewById(R.id.pause_button)
        resetBtn = findViewById(R.id.reset_button)
        musicCard = findViewById(R.id.music_card)
        nowPlayingText = findViewById(R.id.now_playing_text)

        updateTimerText()

        startBtn.setOnClickListener {
            if (!isRunning) {
                startTimer()
                startMusic()
            }
        }

        pauseBtn.setOnClickListener {
            pauseTimer()
            pauseMusic()
        }

        resetBtn.setOnClickListener {
            resetTimer()
            stopMusic()
        }

        // Long click on timer opens a TimePicker dialog
        timerText.setOnLongClickListener {
            showTimePicker()
            true
        }
        val startMusicBtn = findViewById<Button>(R.id.start_music)
        val pauseMusicBtn = findViewById<Button>(R.id.pause_music)
        val stopMusicBtn = findViewById<Button>(R.id.stop_music)

        startMusicBtn.setOnClickListener {
            startMusic()
        }

        pauseMusicBtn.setOnClickListener {
            pauseMusic()
        }

        stopMusicBtn.setOnClickListener {
            stopMusic()
        }

    }

    private fun startTimer() {
        timer = object : CountDownTimer(timeLeftInMillis, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                timeLeftInMillis = millisUntilFinished
                updateTimerText()
            }

            override fun onFinish() {
                isRunning = false
                stopMusic()
                Toast.makeText(this@FocusMode, "Focus session complete!", Toast.LENGTH_SHORT).show()
            }
        }.start()
        isRunning = true
    }

    private fun pauseTimer() {
        timer?.cancel()
        isRunning = false
    }

    private fun resetTimer() {
        timer?.cancel()
        timeLeftInMillis = initialTimeInMillis
        updateTimerText()
        isRunning = false
    }

    private fun updateTimerText() {
        val minutes = TimeUnit.MILLISECONDS.toMinutes(timeLeftInMillis)
        val seconds = TimeUnit.MILLISECONDS.toSeconds(timeLeftInMillis) % 60
        val formatted = String.format("%02d:%02d:%02d", minutes / 60, minutes % 60, seconds)
        timerText.text = formatted
    }

    private fun showTimePicker() {
        val calendar = Calendar.getInstance()
        val dialog = TimePickerDialog(this, { _, hour, minute ->
            timeLeftInMillis = (hour * 60 + minute) * 60 * 1000L
            initialTimeInMillis = timeLeftInMillis
            updateTimerText()
        }, 0, 25, true)
        dialog.show()
    }

    private fun startMusic() {
        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer.create(this, R.raw.lofi)
            mediaPlayer?.isLooping = true
        }
        mediaPlayer?.start()
        nowPlayingText.text = "Now Playing: Lo-Fi Beats 🎶"
    }

    private fun pauseMusic() {
        mediaPlayer?.pause()
        nowPlayingText.text = "Music Paused"
    }

    private fun stopMusic() {
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
        nowPlayingText.text = "Now Playing: Lo-Fi Beats"
    }

    override fun onDestroy() {
        super.onDestroy()
        timer?.cancel()
        stopMusic()
    }
}
