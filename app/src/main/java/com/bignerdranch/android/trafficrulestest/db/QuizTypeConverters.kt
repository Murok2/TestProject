package com.bignerdranch.android.trafficrulestest.db

import androidx.room.TypeConverter
import com.bignerdranch.android.trafficrulestest.data.Quiz
import com.google.gson.Gson
import java.util.*

class QuizTypeConverters {

    @TypeConverter
    fun toUUID(uuid: String?): UUID? {
        return UUID.fromString(uuid)
    }

    @TypeConverter
    fun fromUUID(uuid: UUID?): String? {
        return uuid?.toString()
    }

    @TypeConverter
    fun toQuizzes(value: String) = Gson().fromJson(value, Array<Quiz>::class.java).toList()

    @TypeConverter
    fun fromQuizzes(value: List<Quiz>?): String = Gson().toJson(value)

}