package com.example.messenger

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.user_row.view.*


class NewUserAdapter(
    private var usersList: MutableList<User>,
    private val listener: NewUserActivityListener
) :
    RecyclerView.Adapter<NewUserViewHolder>() {

    override fun getItemCount(): Int {
        return usersList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewUserViewHolder {
        val userRowItem =
            LayoutInflater.from(parent.context).inflate(R.layout.user_row, parent, false)
        return NewUserViewHolder(userRowItem, listener)
    }

    override fun onBindViewHolder(holder: NewUserViewHolder, position: Int) {
        holder.bindView(usersList[position])
    }

    fun updateList(newList: List<User>) {
        usersList = mutableListOf()
        if (!newList.isNullOrEmpty()) {
            usersList.addAll(newList)
        }
        notifyDataSetChanged()
    }
}

class NewUserViewHolder(itemView: View, private val listener: NewUserActivityListener) : RecyclerView.ViewHolder(itemView), LayoutContainer {

    override val containerView: View? = itemView

    fun bindView(user: User) {
        itemView.userName.text = user.userName
        if (user.profileImageUrl.startsWith("https://")) {
            Picasso.get().load(user.profileImageUrl).into(itemView.profilePicture)
        } else {
            itemView.profilePictureTextView.text = user.userName.substring(0, 1)
        }

        itemView.setOnClickListener {
            listener.onUserClicked()
        }

    }
}