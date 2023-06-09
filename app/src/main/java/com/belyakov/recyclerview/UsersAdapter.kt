package com.belyakov.recyclerview

import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.belyakov.recyclerview.databinding.ItemUserBinding
import com.belyakov.recyclerview.data.model.User
import com.belyakov.recyclerview.data.model.UserListItem
import com.bumptech.glide.Glide

class UsersAdapter(
    private val actionListener: UserActionListener
) : RecyclerView.Adapter<UsersAdapter.UsersViewHolder>(), View.OnClickListener {

    var users: List<UserListItem> = emptyList()
        set(newValue) {
            val diffCallback = UsersDiffCallback(field, newValue)
            val diffResult = DiffUtil.calculateDiff(diffCallback)
            field = newValue
            diffResult.dispatchUpdatesTo(this) // передаем изменение нашему адаптеру (вместо notifyDataSetChanged())
        }

    override fun onClick(v: View) {
        val user = v.tag as User
        when (v.id) {
            R.id.moreImageViewBtn -> showPopupMenu(v)
            else -> actionListener.onUserDetails(user)
        }
    }

    // Получение количества итемов
    override fun getItemCount(): Int = users.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsersViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemUserBinding.inflate(inflater, parent, false)

        binding.moreImageViewBtn.setOnClickListener(this)

        return UsersViewHolder(binding)
    }

    // Обновление элемента списка
    override fun onBindViewHolder(holder: UsersViewHolder, position: Int) {
        val userListItem = users[position]
        val user = userListItem.user
        val context = holder.itemView.context
        with(holder.binding) {
            holder.itemView.tag = user
            moreImageViewBtn.tag = user

            if (userListItem.isInProgress) {
                moreImageViewBtn.visibility = View.INVISIBLE
                itemProgressBar.visibility = View.VISIBLE
                holder.binding.root.setOnClickListener(null)
            } else {
                moreImageViewBtn.visibility = View.VISIBLE
                itemProgressBar.visibility = View.GONE
                holder.binding.root.setOnClickListener(this@UsersAdapter)
            }

            userNameTv.text = user.name
            userCompanyTv.text = user.company.ifBlank { context.getString(R.string.unemployed) }
            if (user.photo.isNotBlank()) {
                Glide.with(photoImageView.context)
                    .load(user.photo)
                    .placeholder(R.drawable.ic_default_avatar)
                    .error(R.drawable.ic_default_avatar)
                    .into(photoImageView)
            } else {
                photoImageView.setImageResource(R.drawable.ic_default_avatar)
            }
        }
    }

    private fun showPopupMenu(view: View) {
        val popupMenu = PopupMenu(view.context, view)
        val user = view.tag as User
        val position = users.indexOfFirst { it.user.id == user.id }

        // Можно перемещать наверх только если позиция не равна 1
        popupMenu.menu.add(0, ID_MOVE_UP, Menu.NONE, "Move Up").apply {
            isEnabled = position > 0
        }
        // Можно перемещать вниз только если позиция не равна последнему значению в списке
        popupMenu.menu.add(0, ID_MOVE_DOWN, Menu.NONE, "Move Down").apply {
            isEnabled = position < users.size - 1
        }
        popupMenu.menu.add(0, ID_REMOVE, Menu.NONE, "Remove user")

        if (user.company.isNotBlank()) {
            popupMenu.menu.add(0, ID_FIRE, Menu.NONE, "Fire employee")
        }

        popupMenu.setOnMenuItemClickListener {
            when (it.itemId) {
                ID_MOVE_UP -> actionListener.onUserMove(user, -1)
                ID_MOVE_DOWN -> actionListener.onUserMove(user, 1)
                ID_REMOVE -> actionListener.onUserDelete(user)
                ID_FIRE -> actionListener.onFireUser(user)
            }
            return@setOnMenuItemClickListener true
        }
        popupMenu.show()
    }

    class UsersViewHolder(
        val binding: ItemUserBinding
    ) : RecyclerView.ViewHolder(binding.root)

    private companion object {
        const val ID_MOVE_UP = 1
        const val ID_MOVE_DOWN = 2
        const val ID_REMOVE = 3
        const val ID_FIRE = 4
    }
}