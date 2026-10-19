package com.example.android.architecture.blueprints.todoapp.tasks

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.DrawerActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.example.android.architecture.blueprints.todoapp.R
import org.hamcrest.core.IsNot.not
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class TasksActivityTest {

    @Test
    fun createNewTask_and_checkInList() {
        // Initialize the main Activity scenario
        val activityScenario = ActivityScenario.launch(TasksActivity::class.java)

        // Click the floating action button (FAB) to add a new task
        onView(withId(R.id.add_task_fab)).perform(click())

        // Enter the task title and description into the corresponding fields
        onView(withId(R.id.add_task_title_edit_text)).perform(typeText("Finish Bootcamp Challenge"), closeSoftKeyboard())
        onView(withId(R.id.add_task_description_edit_text)).perform(typeText("Complete Kotlin and Espresso automated UI tests project."), closeSoftKeyboard())

        // Click the FAB to save the task
        onView(withId(R.id.save_task_fab)).perform(click())

        // Verify that the saved task appears correctly in the main list
        onView(withText("Finish Bootcamp Challenge")).check(matches(isDisplayed()))

        activityScenario.close()
    }

    @Test
    fun completeTask_and_verifyStatistics() {
        val activityScenario = ActivityScenario.launch(TasksActivity::class.java)

        // Add a default task for the flow test
        onView(withId(R.id.add_task_fab)).perform(click())
        onView(withId(R.id.add_task_title_edit_text)).perform(typeText("Automated Testing"), closeSoftKeyboard())
        onView(withId(R.id.add_task_description_edit_text)).perform(typeText("Write clean assertions using Espresso library."), closeSoftKeyboard())
        onView(withId(R.id.save_task_fab)).perform(click())

        // Click the checkbox of the created task to mark it as complete
        onView(withId(R.id.complete_checkbox)).perform(click())

        // Open the Navigation Drawer using Espresso Contrib
        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open())

        // Navigate to the Statistics screen via the Drawer menu
        onView(withId(R.id.nav_view)).perform(click())
        
        // Verify that the task calculations and percentages have been updated on the screen
        onView(withId(R.id.stats_completed_text)).check(matches(isDisplayed()))
        onView(withId(R.id.stats_active_text)).check(matches(withText("Active tasks: 0.0%")))

        activityScenario.close()
    }
}
