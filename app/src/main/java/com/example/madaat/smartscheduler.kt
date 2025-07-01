package com.example.madaat

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.madaat.databinding.ActivitySmartschedulerBinding
import java.text.SimpleDateFormat
import java.util.*

class smartscheduler : AppCompatActivity() {
    private lateinit var db: DatabaseHelper
    private lateinit var binding: ActivitySmartschedulerBinding
    private lateinit var userEmail: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySmartschedulerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = DatabaseHelper(this)
        userEmail = intent.getStringExtra("email") ?: "student@example.com"

        val currentDate = getDate(0)
        val tomorrowDate = getDate(1)

        binding.todayLabel.text = "Today, ${formatDisplayDate(currentDate)}"

        loadTasks(currentDate, binding.taskListToday)
        binding.viewScheduleButton.setOnClickListener {
            loadTasks(tomorrowDate, binding.taskListTomorrow)
        }

        binding.addTaskButton.setOnClickListener {
            showAddTaskDialog(currentDate)
        }
    }

    private fun getDate(daysToAdd: Int): String {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DATE, daysToAdd)
        return SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(calendar.time)
    }

    private fun formatDisplayDate(dateStr: String): String {
        val parser = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val formatter = SimpleDateFormat("MMM dd", Locale.getDefault())
        return formatter.format(parser.parse(dateStr)!!)
    }

    private fun loadTasks(date: String, taskContainer: LinearLayout) {
        taskContainer.removeAllViews()
        val tasks = db.getTasks(userEmail, date)

        // If this is today's task list, update the task count text
        val today = getDate(0)
        Log.d("SmartScheduler", "Loading tasks for $date: ${tasks.size} found")
        if (date == today && taskContainer.id == binding.taskListToday.id) {
            binding.taskCountText.text = "You have ${tasks.size} tasks"
        }

        for (task in tasks) {
            val taskLayout = LinearLayout(this).apply {
                orientation = LinearLayout.HORIZONTAL
                setPadding(10, 10, 10, 10)
                layoutParams = LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                ).apply {
                    setMargins(0, 8, 0, 8)
                }
            }

            val taskText = TextView(this).apply {
                text = task.task
                textSize = 16f
                setPadding(0, 0, 16, 0)
                layoutParams = LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f)
            }

            val checkBox = CheckBox(this).apply {
                isChecked = task.isDone == 1
                setOnCheckedChangeListener { _, isChecked ->
                    if (isChecked) {
                        db.updateTaskDone(task.id)
                    }
                }
            }

            val deleteBtn = ImageView(this).apply {
                setImageResource(R.drawable.delete)
                setOnClickListener {
                    db.deleteTask(task.id)
                    loadTasks(date, taskContainer) // reload after delete
                }
            }

            taskLayout.addView(taskText)
            taskLayout.addView(checkBox)
            taskLayout.addView(deleteBtn)
            taskContainer.addView(taskLayout)
        }
    }



    private fun showAddTaskDialog(defaultDate: String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Add Task")

        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_task, null)
        builder.setView(dialogView)

        val taskInput = dialogView.findViewById<EditText>(R.id.task_input)
        val datePicker = dialogView.findViewById<DatePicker>(R.id.date_picker)

        builder.setPositiveButton("Add") { _, _ ->
            val taskText = taskInput.text.toString().trim()
            if (taskText.isNotEmpty()) {
                val calendar = Calendar.getInstance()
                calendar.set(datePicker.year, datePicker.month, datePicker.dayOfMonth)
                val selectedDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(calendar.time)

                val success = db.insertTask(userEmail, taskText, selectedDate)
                if (success) {
                    Toast.makeText(this, "Task added", Toast.LENGTH_SHORT).show()
                    val today = getDate(0)
                    if (selectedDate == today) {
                        loadTasks(today, binding.taskListToday)
                    }
                }
            }
        }

        builder.setNegativeButton("Cancel", null)
        builder.show()
    }
}
