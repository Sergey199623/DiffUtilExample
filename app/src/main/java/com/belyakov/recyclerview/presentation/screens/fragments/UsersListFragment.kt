package com.belyakov.recyclerview.presentation.screens.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.belyakov.recyclerview.UsersAdapter
import com.belyakov.recyclerview.databinding.FragmentUsersListBinding
import com.belyakov.recyclerview.presentation.screens.factory
import com.belyakov.recyclerview.presentation.screens.navigator
import com.belyakov.recyclerview.presentation.screens.viewModels.UsersListViewModel
import com.belyakov.recyclerview.tasks.*

class UsersListFragment : Fragment() {

    private lateinit var binding: FragmentUsersListBinding
    private lateinit var adapter: UsersAdapter

    private val viewModel: UsersListViewModel by viewModels { factory() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUsersListBinding.inflate(inflater, container, false)
        adapter = UsersAdapter(viewModel)

        // viewLifecycleOwner -> жизненный цикл фрагмента, т.к. this - будет допускать прихода данных даже тогда, когда экрана не существует
        viewModel.users.observe(viewLifecycleOwner) {
            hideAll()
            when(it) {
                is SuccessResult -> {
                    binding.recyclerView.visibility = View.VISIBLE
                    adapter.users = it.data
                }
                is ErrorResult -> {
                    binding.tryAgainContainer.visibility = View.VISIBLE
                }
                is PendingResult -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
                is EmptyResult -> {
                    binding.noUsersTextView.visibility = View.VISIBLE
                }
            }
        }

        viewModel.actionShowDetails.observe(viewLifecycleOwner, Observer {
            it.getValue()?.let { user -> navigator().showDetails(user) }
        })
        viewModel.actionShowToast.observe(viewLifecycleOwner, Observer {
            it.getValue()?.let { messageRes -> navigator().showToast(messageRes) }
        })

        val layoutManager = LinearLayoutManager(requireContext())
        with(binding) {
            recyclerView.layoutManager = layoutManager
            recyclerView.adapter = adapter
        }

        return binding.root
    }

    private fun hideAll() {
        binding.recyclerView.visibility = View.GONE
        binding.progressBar.visibility = View.GONE
        binding.tryAgainContainer.visibility = View.GONE
        binding.noUsersTextView.visibility = View.GONE
    }
}