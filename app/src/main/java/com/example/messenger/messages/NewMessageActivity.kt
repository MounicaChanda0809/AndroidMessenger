package com.example.messenger.messages

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.messenger.*
import com.example.messenger.models.User
import com.example.messenger.recyclerViewAdapters.NewUserAdapter
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_new_message.*

class NewMessageActivity : AppCompatActivity(), NewUserActivityListener {

    private val usersList = mutableListOf<User>()

    private val adapter =
        NewUserAdapter(mutableListOf(), this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_message)
        supportActionBar?.title = getString(R.string.select_user)
    }

    override fun onStart() {
        super.onStart()
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
        fetchUsersFromFireBase()
    }

    companion object {
        val USER_KEY= "USER_KEY"
    }

    override fun onUserClicked(userName: String) {
        val intent = Intent(this, ChatLogActivity::class.java)
        startActivity(intent)
        intent.putExtra(USER_KEY, userName)
        finish()
    }

    private fun fetchUsersFromFireBase() {
        usersList.clear()
        val fireBaseDatabase = FirebaseDatabase.getInstance().getReference("/users")
        fireBaseDatabase.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                // not needed now
            }

            override fun onDataChange(p0: DataSnapshot) {
                p0.children.forEach {
                    it.getValue(User::class.java)?.let { user ->
                        usersList.add(user)
                    }
                    adapter.updateList(usersList)
                    Log.d("*******************", it.toString())
                }
            }
        })
    }
}
