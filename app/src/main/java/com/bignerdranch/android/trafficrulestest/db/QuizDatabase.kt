package com.bignerdranch.android.trafficrulestest.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.bignerdranch.android.trafficrulestest.data.Quiz
import com.bignerdranch.android.trafficrulestest.data.Ticket

@Database(entities = [Ticket::class, Quiz::class], version=1, exportSchema = false)
@TypeConverters(QuizTypeConverters::class)
abstract class QuizDatabase : RoomDatabase() {

    abstract fun quizDao(): QuizDao

}