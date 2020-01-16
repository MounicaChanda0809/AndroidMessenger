package com.example.messenger

data class User(val userId: String, val userName: String, val profileImageUrl: String) {
    constructor(): this("","","")
}
