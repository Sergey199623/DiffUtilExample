package com.belyakov.recyclerview

import androidx.recyclerview.widget.DiffUtil
import com.belyakov.recyclerview.data.model.UserListItem

class UsersDiffCallback(
    private val oldList: List<UserListItem>,
    private val newList: List<UserListItem>
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    // Проверка по id, один и тот же ли это элемент
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldUser = oldList[oldItemPosition]
        val newUser = newList[newItemPosition]
        return oldUser.user.id == newUser.user.id
    }

    // Проверка содержимого элемента, поменялось ли оно (сравниваем контент в User)
    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldUser = oldList[oldItemPosition]
        val newUser = newList[newItemPosition]
        return oldUser == newUser // фактически, сравнение по equals, т.к. в дата классе Котлина - автоматом переопределены equals и hashcode
    }
}