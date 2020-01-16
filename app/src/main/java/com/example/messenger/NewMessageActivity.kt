package com.example.messenger

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_new_message.*

class NewMessageActivity : AppCompatActivity() {

    private val usersList = mutableListOf<User>()

    private val adapter = NewUserAdapter(mutableListOf())

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
