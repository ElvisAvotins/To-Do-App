package com.example.todoapp

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import java.util.Date

class TaskViewModel(application: Application) : AndroidViewModel(application) {
    private val _tasks = MutableLiveData<MutableList<Task>>(mutableListOf())
    val tasks: LiveData<MutableList<Task>> = _tasks

    init {
        loadTasks()
    }

    fun addTask(title: String, deadline: Date? = null) {
        val currentTasks = _tasks.value ?: mutableListOf()
        currentTasks.add(Task(title = title, deadline = deadline))
        _tasks.value = currentTasks
        saveTasks()
    }

    fun updateTask(updatedTask: Task) {
        val currentTasks = _tasks.value ?: mutableListOf()
        val index = currentTasks.indexOfFirst { it.title == updatedTask.title }
        if (index != -1) {
            currentTasks[index] = updatedTask
            _tasks.value = currentTasks
            saveTasks()
        }
    }

    fun deleteTask(task: Task) {
        val currentTasks = _tasks.value ?: mutableListOf()
        currentTasks.remove(task)
        _tasks.value = currentTasks
        saveTasks()
    }

    fun markTaskCompleted(task: Task, completed: Boolean) {
        val currentTasks = _tasks.value ?: mutableListOf()
        val index = currentTasks.indexOfFirst { it.title == task.title }
        if (index != -1) {
            currentTasks[index].completed = completed
            currentTasks[index].completedDate = if (completed) Date() else null
            _tasks.value = currentTasks
            saveTasks()
        }
    }

    private fun loadTasks() {
        _tasks.value = TaskFileHelper.loadTasks(getApplication())
    }

    private fun saveTasks() {
        _tasks.value?.let { TaskFileHelper.saveTasks(getApplication(), it) }
    }
}
