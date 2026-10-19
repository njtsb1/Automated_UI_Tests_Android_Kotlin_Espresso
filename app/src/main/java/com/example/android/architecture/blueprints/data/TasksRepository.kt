package com.example.android.architecture.blueprints.todoapp.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class TasksRepository {

    private val cachedTasks = LinkedHashMap<String, Task>()
    private val observableTasks = MutableLiveData<List<Task>>()

    fun observeTasks(): LiveData<List<Task>> {
        return observableTasks
    }

    private fun refreshVisibleTasksList() {
        observableTasks.postValue(cachedTasks.values.toList())
    }

    suspend fun getTasks(): List<Task> = withContext(Dispatchers.IO) {
        return@withContext cachedTasks.values.toList()
    }

    suspend fun getTask(taskId: String): Task? = withContext(Dispatchers.IO) {
        return@withContext cachedTasks[taskId]
    }

    suspend fun saveTask(task: Task) = withContext(Dispatchers.IO) {
        cachedTasks[task.id] = task
        refreshVisibleTasksList()
    }

    suspend fun completeTask(taskId: String) = withContext(Dispatchers.IO) {
        cachedTasks[taskId]?.isCompleted = true
        refreshVisibleTasksList()
    }

    suspend fun activateTask(taskId: String) = withContext(Dispatchers.IO) {
        cachedTasks[taskId]?.isCompleted = false
        refreshVisibleTasksList()
    }

    suspend fun clearCompletedTasks() = withContext(Dispatchers.IO) {
        val it = cachedTasks.entries.iterator()
        while (it.hasNext()) {
            val entry = it.next()
            if (entry.value.isCompleted) {
                it.remove()
            }
        }
        refreshVisibleTasksList()
    }

    suspend fun deleteTask(taskId: String) = withContext(Dispatchers.IO) {
        cachedTasks.remove(taskId)
        refreshVisibleTasksList()
    }
}
