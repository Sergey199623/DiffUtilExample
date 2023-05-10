package com.belyakov.recyclerview.presentation.screens.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.belyakov.recyclerview.UserActionListener
import com.belyakov.recyclerview.UsersAdapter
import com.belyakov.recyclerview.databinding.FragmentUsersListBinding
import com.belyakov.recyclerview.data.model.User
import com.belyakov.recyclerview.presentation.screens.factory
import com.belyakov.recyclerview.presentation.screens.navigator
import com.belyakov.recyclerview.presentation.screens.viewModels.UsersListViewModel

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
        adapter = UsersAdapter(object : UserActionListener {
            override fun onUserMove(user: User, moveBy: Int) {
                viewModel.moveUser(user, moveBy)
            }

            override fun onUserDelete(user: User) {
                viewModel.deleteUser(user)
            }

            override fun onUserDetails(user: User) {
                navigator().showDetails(user)
            }

            override fun onFireUser(user: User) {
                viewModel.fireUser(user)
            }

        })

        // viewLifecycleOwner -> жизненный цикл фрагмента, т.к. this - будет допускать прихода данных даже тогда, когда экрана не существует
        viewModel.users.observe(viewLifecycleOwner) {
            adapter.users = it
        }

        val layoutManager = LinearLayoutManager(requireContext())
        with(binding) {
            recyclerView.layoutManager = layoutManager
            recyclerView.adapter = adapter
        }

        return binding.root
    }
}