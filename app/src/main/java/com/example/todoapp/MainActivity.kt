package com.example.todoapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todoapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var taskViewModel: TaskViewModel
    private lateinit var adapter: TaskAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        taskViewModel = ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(application)).get(TaskViewModel::class.java)

        adapter = TaskAdapter(mutableListOf()) { task ->
            // Open EditTaskSheet when a task item is clicked
            EditTaskSheet(task).show(supportFragmentManager, "editTaskTag")
        }
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter

        taskViewModel.tasks.observe(this) { tasks ->
            adapter.updateTasks(tasks)
        }
        binding.newTaskButton.setOnClickListener {
            NewTaskSheet().show(supportFragmentManager, "newTaskTag")
        }
    }
}
