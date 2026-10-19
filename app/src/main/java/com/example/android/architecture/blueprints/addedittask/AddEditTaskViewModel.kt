package com.example.android.architecture.blueprints.todoapp.addedittask

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.architecture.blueprints.todoapp.data.Task
import com.example.android.architecture.blueprints.todoapp.data.TasksRepository
import com.example.android.architecture.blueprints.todoapp.util.Event
import kotlinx.coroutines.launch

class AddEditTaskViewModel(private val tasksRepository: TasksRepository) : ViewModel() {

    val title = MutableLiveData<String>()
    val description = MutableLiveData<String>()

    private val _dataLoading = MutableLiveData<Boolean>(false)
    val dataLoading: LiveData<Boolean> = _dataLoading

    private val _taskUpdatedEvent = MutableLiveData<Event<Unit>>()
    val taskUpdatedEvent: LiveData<Event<Unit>> = _taskUpdatedEvent

    private var taskId: String? = null
    private var isNewTask: Boolean = true

    fun start(taskId: String?) {
        this.taskId = taskId
        if (taskId == null) {
            isNewTask = true
            return
        }
        isNewTask = false
        _dataLoading.value = true
        viewModelScope.launch {
            tasksRepository.getTask(taskId)?.let { task ->
                title.value = task.title
                description.value = task.description
            }
            _dataLoading.value = false
        }
    }

    fun saveTask() {
        val currentTitle = title.value
        val currentDescription = description.value

        if (currentTitle.isNullOrEmpty() || currentDescription.isNullOrEmpty()) {
            return
        }

        viewModelScope.launch {
            if (isNewTask) {
                tasksRepository.saveTask(Task(currentTitle, currentDescription))
            } else {
                taskId?.let {
                    tasksRepository.saveTask(Task(currentTitle, currentDescription, id = it))
                }
            }
            _taskUpdatedEvent.value = Event(Unit)
        }
    }
}
