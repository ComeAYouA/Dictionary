package com.lithium.kotlin.dictionary

import androidx.room.DatabaseView
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

data class Category(
    @PrimaryKey var name: String = "",
    val ids: MutableSet<String> = mutableSetOf()
)