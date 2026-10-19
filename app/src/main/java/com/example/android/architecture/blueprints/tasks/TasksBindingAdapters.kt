package com.example.android.architecture.blueprints.todoapp.tasks

import android.graphics.Paint
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.android.architecture.blueprints.todoapp.data.Task

@BindingAdapter("app:items")
fun setItems(listView: RecyclerView, items: List<Task>?) {
    (listView.adapter as? TasksAdapter)?.submitList(items ?: emptyList())
}

@BindingAdapter("app:completedTask")
fun setStyleCompletedTask(textView: TextView, enabled: Boolean) {
    if (enabled) {
        textView.paintFlags = textView.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
    } else {
        textView.paintFlags = textView.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
    }
}
