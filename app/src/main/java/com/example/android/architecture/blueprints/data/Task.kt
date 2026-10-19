package com.example.android.architecture.blueprints.todoapp.data

import java.util.UUID

data class Task @JvmOverloads constructor(
    var title: String = "",
    var description: String = "",
    var isCompleted: Boolean = false,
    val id: String = UUID.randomUUID().toString()
) {
    // Returns the title if it is not empty; otherwise, returns the description.
    val titleForList: String
        get() = if (title.isNotEmpty()) title else description

    val isActive: Boolean
        get() = !isCompleted

    val isEmpty: Boolean
        get() = title.isEmpty() && description.isEmpty()
}
