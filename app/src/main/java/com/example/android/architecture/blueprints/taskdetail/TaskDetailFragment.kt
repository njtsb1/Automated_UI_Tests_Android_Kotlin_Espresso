package com.example.android.architecture.blueprints.todoapp.taskdetail

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.android.architecture.blueprints.todoapp.R
import com.example.android.architecture.blueprints.todoapp.TodoApplication
import com.example.android.architecture.blueprints.todoapp.databinding.FragmentTaskDetailBinding
import com.example.android.architecture.blueprints.todoapp.util.EventObserver

class TaskDetailFragment : Fragment() {

    private lateinit var viewDataBinding: FragmentTaskDetailBinding
    private val args: TaskDetailFragmentArgs by navArgs()
    private lateinit var viewModel: TaskDetailViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val repository = (requireContext().applicationContext as TodoApplication).tasksRepository
        viewModel = TaskDetailViewModel(repository)

        viewDataBinding = FragmentTaskDetailBinding.inflate(inflater, container, false).apply {
            viewmodel = viewModel
            lifecycleOwner = viewLifecycleOwner
        }
        setHasOptionsMenu(true)
        return viewDataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.start(args.taskId)
        setupFab()
        setupNavigation()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.taskdetail_fragment_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_delete -> {
                viewModel.deleteTask()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun setupFab() {
        viewDataBinding.editTaskFab.setOnClickListener {
            viewModel.editTask()
        }
    }

    private fun setupNavigation() {
        viewModel.editTaskEvent.observe(viewLifecycleOwner, EventObserver { taskId ->
            val action = TaskDetailFragmentDirections.actionTaskDetailFragmentToAddEditTaskFragment(
                taskId, requireContext().getString(R.string.edit_task)
            )
            findNavController().navigate(action)
        })
        viewModel.deleteTaskEvent.observe(viewLifecycleOwner, EventObserver {
            val action = TaskDetailFragmentDirections.actionTaskDetailFragmentToTasksFragment()
            findNavController().navigate(action)
        })
    }
}
