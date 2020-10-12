package com.example.messenger

interface LatestMessagesActivityListener {

    fun onMessageClicked()

}

interface NewUserActivityListener {

    fun onUserClicked(userName: String)

}