package com.lithium.kotlin.dictionary.database

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.*

class WordTypeConverters {

    private val gson = Gson()
    @TypeConverter
    fun toUUID(uuid: String?): UUID? {
        return UUID.fromString(uuid)
    }
    @TypeConverter
    fun fromUUID(uuid: UUID?): String? {
        return uuid?.toString()
    }
    @TypeConverter
    fun fromMutableSetToJson(list: MutableSet<String>?): String?{
        return if(list == null){
            gson.toJson(emptySet<String>())
        }else{
            gson.toJson(list)
        }
    }
    @TypeConverter
    fun jsonToMutableSet(json: String?): MutableSet<String>{
        return if (json.isNullOrEmpty()) {
            mutableSetOf()
        } else {
            val type = object : TypeToken<MutableSet<String>>() {}.type
            gson.fromJson(json, type)
        }
    }
}