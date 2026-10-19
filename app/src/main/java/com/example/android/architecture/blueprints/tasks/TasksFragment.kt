package com.example.android.architecture.blueprints.todoapp.tasks

import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.android.architecture.blueprints.todoapp.R
import com.example.android.architecture.blueprints.todoapp.TodoApplication
import com.example.android.architecture.blueprints.todoapp.databinding.FragmentTasksBinding
import com.example.android.architecture.blueprints.todoapp.util.EventObserver

class TasksFragment : Fragment() {

    private lateinit var viewDataBinding: FragmentTasksBinding
    private lateinit var listAdapter: TasksAdapter
    private lateinit var viewModel: TasksViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val repository = (requireContext().applicationContext as TodoApplication).tasksRepository
        viewModel = TasksViewModel(repository)

        viewDataBinding = FragmentTasksBinding.inflate(inflater, container, false).apply {
            viewmodel = viewModel
            lifecycleOwner = viewLifecycleOwner
        }
        setHasOptionsMenu(true)
        return viewDataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupListAdapter()
        setupNavigation()
        setupFab()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.tasks_fragment_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_clear -> {
                viewModel.clearCompletedTasks()
                true
            }
            R.id.menu_filter -> {
                showFilteringPopUpMenu()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun setupListAdapter() {
        val viewModel = viewDataBinding.viewmodel ?: return
        listAdapter = TasksAdapter(viewModel)
        viewDataBinding.tasksList.adapter = listAdapter
    }

    private fun setupNavigation() {
        viewModel.openTaskEvent.observe(viewLifecycleOwner, EventObserver { taskId ->
            val action = TasksFragmentDirections.actionTasksFragmentToTaskDetailFragment(taskId)
            findNavController().navigate(action)
        })
        viewModel.newTaskEvent.observe(viewLifecycleOwner, EventObserver {
            val action = TasksFragmentDirections.actionTasksFragmentToAddEditTaskFragment(
                null, requireContext().getString(R.string.add_task)
            )
            findNavController().navigate(action)
        })
    }

    private fun setupFab() {
        viewDataBinding.addTaskFab.setOnClickListener {
            viewModel.addNewTask()
        }
    }

    private fun showFilteringPopUpMenu() {
        val view = activity?.findViewById<View>(R.id.menu_filter) ?: return
        PopupMenu(requireContext(), view).run {
            menuInflater.inflate(R.menu.filter_tasks, menu)
            setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.active -> viewModel.setFiltering(TasksFilterType.ACTIVE_TASKS)
                    R.id.completed -> viewModel.setFiltering(TasksFilterType.COMPLETED_TASKS)
                    else -> viewModel.setFiltering(TasksFilterType.ALL_TASKS)
                }
                true
            }
            show()
        }
    }
}
