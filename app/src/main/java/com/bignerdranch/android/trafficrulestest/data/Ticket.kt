package com.bignerdranch.android.trafficrulestest.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class Ticket(
    @PrimaryKey val id: UUID = UUID.randomUUID(),
    val quizzes: MutableList<Quiz>,
    val score: Int = 0
)