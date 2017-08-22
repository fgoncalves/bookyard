package com.github.fgoncalves.bookyard.data.models

data class User(
    val email: String = "",
    val schemaVersion: String = "",
    val uuid: String = "",
    val books: List<String> = emptyList())
