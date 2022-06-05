package com.bignerdranch.android.trafficrulestest.data

import androidx.annotation.IdRes
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class Quiz(
    @PrimaryKey val id: UUID = UUID.randomUUID(),
    @IdRes val image: Int,
    val question: String,
    val option_one: String? = null,
    val option_two: String? = null,
    val option_three: String? = null,
    val option_four: String? = null,
    val correct_answer: Int,
    val explanation_answer: String? = null
)