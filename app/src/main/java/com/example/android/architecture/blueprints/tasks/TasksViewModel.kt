package com.example.android.architecture.blueprints.todoapp.tasks

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import com.example.android.architecture.blueprints.todoapp.R
import com.example.android.architecture.blueprints.todoapp.data.Task
import com.example.android.architecture.blueprints.todoapp.data.TasksRepository
import com.example.android.architecture.blueprints.todoapp.util.Event
import kotlinx.coroutines.launch

class TasksViewModel(private val tasksRepository: TasksRepository) : ViewModel() {

    private val _forceItemsRefresh = MutableLiveData<Boolean>(false)

    private val _items: LiveData<List<Task>> = _forceItemsRefresh.switchMap { forceRefresh ->
        if (forceRefresh) {
            _dataLoading.value = true
            // Fast asynchronous loading simulation
            _dataLoading.value = false
        }
        tasksRepository.observeTasks()
    }
    val items: LiveData<List<Task>> = _items

    private val _dataLoading = MutableLiveData<Boolean>()
    val dataLoading: LiveData<Boolean> = _dataLoading

    private val _currentFilteringLabel = MutableLiveData<Int>()
    val currentFilteringLabel: LiveData<Int> = _currentFilteringLabel

    private val _noTasksLabel = MutableLiveData<Int>()
    val noTasksLabel: LiveData<Int> = _noTasksLabel

    private val _noTaskIconRes = MutableLiveData<Int>()
    val noTaskIconRes: LiveData<Int> = _noTaskIconRes

    private val _empty = MutableLiveData<Boolean>()
    val empty: LiveData<Boolean> = _empty

    private val _openTaskEvent = MutableLiveData<Event<String>>()
    val openTaskEvent: LiveData<Event<String>> = _openTaskEvent

    private val _newTaskEvent = MutableLiveData<Event<Unit>>()
    val newTaskEvent: LiveData<Event<Unit>> = _newTaskEvent

    init {
        setFiltering(TasksFilterType.ALL_TASKS)
        refresh()
    }

    fun setFiltering(requestType: TasksFilterType) {
        when (requestType) {
            TasksFilterType.ALL_TASKS -> {
                _currentFilteringLabel.value = R.string.label_all
                _noTasksLabel.value = R.string.no_tasks_all
                _noTaskIconRes.value = R.drawable.ic_assignment_turned_in
            }
            TasksFilterType.ACTIVE_TASKS -> {
                _currentFilteringLabel.value = R.string.label_active
                _noTasksLabel.value = R.string.no_tasks_active
                _noTaskIconRes.value = R.drawable.ic_filter_list
            }
            TasksFilterType.COMPLETED_TASKS -> {
                _currentFilteringLabel.value = R.string.label_completed
                _noTasksLabel.value = R.string.no_tasks_completed
                _noTaskIconRes.value = R.drawable.no_tasks_completed
            }
        }
        loadTasks()
    }

    fun refresh() {
        _forceItemsRefresh.value = true
    }

    private fun loadTasks() {
        viewModelScope.launch {
            val tasks = tasksRepository.getTasks()
            _empty.value = tasks.isEmpty()
        }
    }

    fun completeTask(task: Task, completed: Boolean) = viewModelScope.launch {
        if (completed) {
            tasksRepository.completeTask(task.id)
        } else {
            tasksRepository.activateTask(task.id)
        }
        loadTasks()
    }

    fun clearCompletedTasks() = viewModelScope.launch {
        tasksRepository.clearCompletedTasks()
        loadTasks()
    }

    fun openTask(taskId: String) {
        _openTaskEvent.value = Event(taskId)
    }

    fun addNewTask() {
        _newTaskEvent.value = Event(Unit)
    }
}

enum class TasksFilterType {
    ALL_TASKS,
    ACTIVE_TASKS,
    COMPLETED_TASKS
}
