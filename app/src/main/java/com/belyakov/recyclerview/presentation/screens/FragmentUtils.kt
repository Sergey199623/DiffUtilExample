package com.belyakov.recyclerview.presentation.screens

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.belyakov.recyclerview.App
import com.belyakov.recyclerview.Navigator
import com.belyakov.recyclerview.presentation.screens.viewModels.UserDetailsViewModel
import com.belyakov.recyclerview.presentation.screens.viewModels.UsersListViewModel

// Фабрика вью моделей
class ViewModelFactory(
    private val app: App
) : ViewModelProvider.Factory {

    /**
     * @param modelClass -> класс ViewModel
     * @return -> возвращает саму ViewModel
     */
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val viewModel: ViewModel = when (modelClass) {
            UsersListViewModel::class.java -> UsersListViewModel(app.usersServices)
            UserDetailsViewModel::class.java -> UserDetailsViewModel(app.usersServices)
            else -> throw java.lang.IllegalStateException("Unknown view model class")
        }
        return viewModel as T
    }
}

fun Fragment.factory() = ViewModelFactory(requireContext().applicationContext as App)

fun Fragment.navigator() = requireActivity() as Navigator