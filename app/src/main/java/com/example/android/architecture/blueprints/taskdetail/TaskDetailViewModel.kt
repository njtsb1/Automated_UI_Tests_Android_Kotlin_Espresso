package com.example.android.architecture.blueprints.todoapp.taskdetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.architecture.blueprints.todoapp.data.Task
import com.example.android.architecture.blueprints.todoapp.data.TasksRepository
import com.example.android.architecture.blueprints.todoapp.util.Event
import kotlinx.coroutines.launch

class TaskDetailViewModel(private val tasksRepository: TasksRepository) : ViewModel() {

    private val _task = MutableLiveData<Task?>()
    val task: LiveData<Task?> = _task

    private val _completed = MutableLiveData<Boolean>()
    val completed: LiveData<Boolean> = _completed

    private val _dataLoading = MutableLiveData<Boolean>()
    val dataLoading: LiveData<Boolean> = _dataLoading

    private val _isDataAvailable = MutableLiveData<Boolean>()
    val isDataAvailable: LiveData<Boolean> = _isDataAvailable

    private val _editTaskEvent = MutableLiveData<Event<String>>()
    val editTaskEvent: LiveData<Event<String>> = _editTaskEvent

    private val _deleteTaskEvent = MutableLiveData<Event<Unit>>()
    val deleteTaskEvent: LiveData<Event<Unit>> = _deleteTaskEvent

    fun start(taskId: String?) {
        if (taskId == null) return
        _dataLoading.value = true
        viewModelScope.launch {
            val result = tasksRepository.getTask(taskId)
            _task.value = result
            _completed.value = result?.isCompleted ?: false
            _isDataAvailable.value = result != null
            _dataLoading.value = false
        }
    }

    fun setCompleted(checked: Boolean) = viewModelScope.launch {
        val currentTask = _task.value ?: return@launch
        if (checked) {
            tasksRepository.completeTask(currentTask.id)
        } else {
            tasksRepository.activateTask(currentTask.id)
        }
        start(currentTask.id)
    }

    fun editTask() {
        _task.value?.id?.let {
            _editTaskEvent.value = Event(it)
        }
    }

    fun deleteTask() = viewModelScope.launch {
        _task.value?.id?.let {
            tasksRepository.deleteTask(it)
            _deleteTaskEvent.value = Event(Unit)
        }
    }

    fun refresh() {
        _task.value?.id?.let { start(it) }
    }
}
