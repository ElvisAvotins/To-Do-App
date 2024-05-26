package com.example.todoapp

import java.util.Date

data class Task(
    var title: String,
    var completed: Boolean = false,
    val createdDate: Date = Date(),
    var completedDate: Date? = null,
    var deadline: Date? = null // Add this line
)
