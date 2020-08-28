package com.ankitamoundekar.todoapp.data

import androidx.room.TypeConverter
import com.ankitamoundekar.todoapp.data.model.Priority

class Convertor {

    @TypeConverter
    fun fromPriorityToString(priority: Priority):String{
        return priority.name
    }

    @TypeConverter
    fun fromStringToPriority(priority: String):Priority{
        return Priority.valueOf(priority)
    }
}