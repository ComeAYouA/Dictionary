package com.lithium.kotlin.dictionary.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*
@Entity
data class Category(
    @PrimaryKey var name: String = "",
    val ids: MutableSet<UUID> = mutableSetOf()
)