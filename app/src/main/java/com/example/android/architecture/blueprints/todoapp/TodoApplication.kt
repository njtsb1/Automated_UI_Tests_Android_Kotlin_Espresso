package com.example.android.architecture.blueprints.todoapp

import android.app.Application
import com.example.android.architecture.blueprints.todoapp.data.TasksRepository
import timber.log.Timber

class TodoApplication : Application() {

    // Global repository instance (a simple Service Locator to streamline the challenge)
    val tasksRepository: TasksRepository by lazy {
        TasksRepository()
    }

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}
