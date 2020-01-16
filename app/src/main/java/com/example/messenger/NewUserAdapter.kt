package com.example.messenger

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.user_row.view.*


class NewUserAdapter(private var usersList: MutableList<User>) :
    RecyclerView.Adapter<NewUserViewHolder>() {

    override fun getItemCount(): Int {
        return usersList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewUserViewHolder {
        val userRowItem =
            LayoutInflater.from(parent.context).inflate(R.layout.user_row, parent, false)
        return NewUserViewHolder(userRowItem)
    }

    override fun onBindViewHolder(holder: NewUserViewHolder, position: Int) {
        val item = usersList[position]
        holder.userName.text = item.userName
        if (item.profileImageUrl.startsWith("https://")) {
            Picasso.get().load(item.profileImageUrl).into(holder.profilePicture)
        } else {
            holder.profilePictureTextureView.text = item.userName.substring(0, 1)
        }
    }

    fun updateList(newList: List<User>) {
        usersList = mutableListOf()
        if (!newList.isNullOrEmpty()) {
            usersList.addAll(newList)
        }
        notifyDataSetChanged()
    }
}

class NewUserViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val userName = view.UserName
    val profilePicture = view.ProfilePicture
    val profilePictureTextureView = view.profilePictureTextView
}