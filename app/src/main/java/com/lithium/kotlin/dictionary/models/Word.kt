package com.lithium.kotlin.dictionary.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class Word(
    @PrimaryKey val id : UUID = UUID.randomUUID(),
    var sequence: String = "",
    val translation: MutableSet<String> = mutableSetOf(),
    val categories: MutableSet<String> = mutableSetOf(),
    val photoFilePath:String = "",
    val progression:Int = 0
)