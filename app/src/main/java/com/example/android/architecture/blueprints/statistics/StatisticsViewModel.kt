package com.example.android.architecture.blueprints.todoapp.statistics

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.architecture.blueprints.todoapp.data.TasksRepository
import kotlinx.coroutines.launch

class StatisticsViewModel(private val tasksRepository: TasksRepository) : ViewModel() {

    private val _dataLoading = MutableLiveData<Boolean>(false)
    val dataLoading: LiveData<Boolean> = _dataLoading

    private val _empty = MutableLiveData<Boolean>(true)
    val empty: LiveData<Boolean> = _empty

    private val _activeTasksPercent = MutableLiveData<Float>(0f)
    val activeTasksPercent: LiveData<Float> = _activeTasksPercent

    private val _completedTasksPercent = MutableLiveData<Float>(0f)
    val completedTasksPercent: LiveData<Float> = _completedTasksPercent

    fun refresh() {
        _dataLoading.value = true
        viewModelScope.launch {
            val tasks = tasksRepository.getTasks()
            if (tasks.isEmpty()) {
                _empty.value = true
                _activeTasksPercent.value = 0f
                _completedTasksPercent.value = 0f
            } else {
                _empty.value = false
                val totalTasks = tasks.size
                val completedTasks = tasks.count { it.isCompleted }
                val activeTasks = totalTasks - completedTasks

                _activeTasksPercent.value = (activeTasks.toFloat() / totalTasks) * 100
                _completedTasksPercent.value = (completedTasks.toFloat() / totalTasks) * 100
            }
            _dataLoading.value = false
        }
    }
}
