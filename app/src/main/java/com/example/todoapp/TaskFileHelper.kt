package com.example.todoapp

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.File
import java.io.IOException

object TaskFileHelper {

    private const val FILE_NAME = "tasks.json"

    fun saveTasks(context: Context, tasks: List<Task>) {
        val gson = Gson()
        val jsonString = gson.toJson(tasks)
        val file = File(context.filesDir, FILE_NAME)
        try {
            file.writeText(jsonString)
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun loadTasks(context: Context): MutableList<Task> {
        val file = File(context.filesDir, FILE_NAME)
        if (!file.exists()) {
            return mutableListOf()
        }
        val jsonString = file.readText()
        val gson = Gson()
        val type = object : TypeToken<MutableList<Task>>() {}.type
        return gson.fromJson(jsonString, type)
    }
}
