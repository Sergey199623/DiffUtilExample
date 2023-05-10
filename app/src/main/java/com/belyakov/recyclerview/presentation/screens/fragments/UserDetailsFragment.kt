package com.belyakov.recyclerview.presentation.screens.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.belyakov.recyclerview.R
import com.belyakov.recyclerview.databinding.FragmentUserDetailsBinding
import com.belyakov.recyclerview.databinding.FragmentUsersListBinding
import com.belyakov.recyclerview.presentation.screens.factory
import com.belyakov.recyclerview.presentation.screens.navigator
import com.belyakov.recyclerview.presentation.screens.viewModels.UserDetailsViewModel
import com.bumptech.glide.Glide

class UserDetailsFragment : Fragment() {

    private lateinit var binding: FragmentUserDetailsBinding
    private val viewModel: UserDetailsViewModel by viewModels { factory() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.loadUser(requireArguments().getLong(ARG_USER_ID))
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUserDetailsBinding.inflate(inflater, container, false)

        viewModel.userDetails.observe(viewLifecycleOwner) {
            binding.userNameTextView.text = it.user.name
            binding.userDetailsTextView.text = it.details
            if (it.user.photo.isNotBlank()) {
                Glide.with(this)
                    .load(it.user.photo)
                    .circleCrop()
                    .into(binding.photoImageView)
            } else {
                Glide.with(this)
                    .load(R.drawable.ic_default_avatar)
                    .into(binding.photoImageView)
            }
        }

        binding.deleteButton.setOnClickListener {
            viewModel.deleteUser()
            navigator().goBack()
        }
        return binding.root
    }

    companion object {
        private const val ARG_USER_ID = "ARG_USER_ID"

        fun newInstance(userId: Long): UserDetailsFragment {
            val fragment = UserDetailsFragment()
            fragment.arguments = bundleOf(ARG_USER_ID to userId)
            return fragment
        }
    }
}