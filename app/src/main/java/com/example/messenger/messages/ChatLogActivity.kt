package com.example.messenger.messages

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.messenger.R
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_chat_log.*

class ChatLogActivity : AppCompatActivity() {


    @Suppress("DEPRECATION")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_log)
        // TODO display username in the acion bar
        supportActionBar?.title = "Chat log"
        sendButton.setBackgroundDrawable(getDrawable(R.drawable.send))


        val adapter = GroupAdapter<ViewHolder>()

        adapter.add(ReceivedChatItem())
        adapter.add(SentChatItem())
        adapter.add(ReceivedChatItem())
        adapter.add(SentChatItem())
        adapter.add(ReceivedChatItem())
        adapter.add(SentChatItem())
        adapter.add(ReceivedChatItem())
        adapter.add(SentChatItem())
        adapter.add(ReceivedChatItem())
        adapter.add(SentChatItem())
        adapter.add(ReceivedChatItem())
        adapter.add(SentChatItem())
        adapter.add(ReceivedChatItem())
        adapter.add(SentChatItem())
        adapter.add(ReceivedChatItem())
        adapter.add(SentChatItem())
        adapter.add(ReceivedChatItem())
        adapter.add(SentChatItem())
        adapter.add(ReceivedChatItem())
        adapter.add(SentChatItem())
        adapter.add(ReceivedChatItem())
        adapter.add(SentChatItem())
        adapter.add(ReceivedChatItem())
        adapter.add(SentChatItem())


        recyclerViewChatLog.layoutManager = LinearLayoutManager(this)
        recyclerViewChatLog.adapter = adapter


    }
}

class ReceivedChatItem: Item<ViewHolder>() {

    override fun getLayout(): Int {
        return R.layout.received_message_row
    }

    override fun bind(viewHolder: ViewHolder, position: Int) {
    }

}

class SentChatItem: Item<ViewHolder>() {

    override fun getLayout(): Int {
        return R.layout.sent_message_row
    }

    override fun bind(viewHolder: ViewHolder, position: Int) {
    }

}
