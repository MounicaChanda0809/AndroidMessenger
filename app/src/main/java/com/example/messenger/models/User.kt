package com.example.messenger.models

data class User(val userId: String, val userName: String, val profileImageUrl: String) {
    constructor(): this("","","")
}
