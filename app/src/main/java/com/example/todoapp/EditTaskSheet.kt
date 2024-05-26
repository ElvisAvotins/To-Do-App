package com.example.todoapp

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.todoapp.databinding.FragmentEditTaskSheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class EditTaskSheet(private val task: Task) : BottomSheetDialogFragment() {
    private var _binding: FragmentEditTaskSheetBinding? = null
    private val binding get() = _binding!!
    private lateinit var taskViewModel: TaskViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEditTaskSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        taskViewModel = ViewModelProvider(requireActivity()).get(TaskViewModel::class.java)

        binding.title.setText(task.title)

        val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault())
        val createdDateString = dateFormat.format(task.createdDate)
        binding.createdDate.text = getString(R.string.created_date, createdDateString)

        updateDeadlineTextView(dateFormat)

        binding.setDeadlineButton.setOnClickListener {
            showDateTimePicker { date ->
                task.deadline = date
                updateDeadlineTextView(dateFormat)
            }
        }

        binding.removeDeadlineButton.setOnClickListener {
            task.deadline = null
            binding.deadlineDate.text = getString(R.string.no_deadline)
        }

        binding.saveButton.setOnClickListener {
            val updatedTitle = binding.title.text.toString()
            task.title = updatedTitle
            taskViewModel.updateTask(task)
            dismiss()
        }

        binding.deleteButton.setOnClickListener {
            taskViewModel.deleteTask(task)
            dismiss()
        }
    }

    private fun showDateTimePicker(onDateTimeSet: (Date) -> Unit) {
        val calendar = Calendar.getInstance()
        DatePickerDialog(
            requireContext(),
            { _, year, month, dayOfMonth ->
                calendar.set(year, month, dayOfMonth)
                TimePickerDialog(
                    requireContext(),
                    { _, hourOfDay, minute ->
                        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
                        calendar.set(Calendar.MINUTE, minute)
                        onDateTimeSet(calendar.time)
                    },
                    calendar.get(Calendar.HOUR_OF_DAY),
                    calendar.get(Calendar.MINUTE),
                    true
                ).show()
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    private fun updateDeadlineTextView(dateFormat: SimpleDateFormat) {
        task.deadline?.let {
            val deadlineString = dateFormat.format(it)
            binding.deadlineDate.text = getString(R.string.deadline, deadlineString)
        } ?: run {
            binding.deadlineDate.text = getString(R.string.no_deadline)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
