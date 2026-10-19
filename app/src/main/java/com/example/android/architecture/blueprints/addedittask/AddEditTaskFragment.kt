package com.example.android.architecture.blueprints.todoapp.addedittask

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.android.architecture.blueprints.todoapp.TodoApplication
import com.example.android.architecture.blueprints.todoapp.databinding.FragmentAddEditTaskBinding
import com.example.android.architecture.blueprints.todoapp.util.EventObserver

class AddEditTaskFragment : Fragment() {

    private lateinit var viewDataBinding: FragmentAddEditTaskBinding
    private val args: AddEditTaskFragmentArgs by navArgs()
    private lateinit var viewModel: AddEditTaskViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val repository = (requireContext().applicationContext as TodoApplication).tasksRepository
        viewModel = AddEditTaskViewModel(repository)

        viewDataBinding = FragmentAddEditTaskBinding.inflate(inflater, container, false).apply {
            viewmodel = viewModel
            lifecycleOwner = viewLifecycleOwner
        }
        return viewDataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.start(args.taskId)
        viewModel.taskUpdatedEvent.observe(viewLifecycleOwner, EventObserver {
            val action = AddEditTaskFragmentDirections.actionAddEditTaskFragmentToTasksFragment()
            findNavController().navigate(action)
        })
    }
}
