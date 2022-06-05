package com.bignerdranch.android.trafficrulestest.ui.tickets

import android.content.Context
import androidx.room.Room
import com.bignerdranch.android.trafficrulestest.data.Ticket
import com.bignerdranch.android.trafficrulestest.db.QuizDatabase
import java.util.*

class TicketsRepository private constructor(context: Context) {

    private val database : QuizDatabase = Room.databaseBuilder(
        context.applicationContext,
        QuizDatabase::class.java,
        DATABASE_NAME
    ).build()

    private val quizDao = database.quizDao()

    suspend fun addTickets(tickets: List<Ticket>) = quizDao.addTickets(tickets)

    suspend fun getQuizzes(id: UUID): Ticket = quizDao.getQuizzes(id)

    suspend fun getTickets(): List<Ticket> = quizDao.getTickets()

    suspend fun updateScore(id: UUID, score: Int) { quizDao.updateScore(id, score) }

    companion object {
        private var DATABASE_NAME = "tickets-database"
        private var INSTANCE: TicketsRepository? = null

        fun initialize(context: Context) {
            if (INSTANCE == null) {
                INSTANCE = TicketsRepository(context)
            }
        }

        fun get(): TicketsRepository {
            return INSTANCE ?:
            throw IllegalStateException("TicketsRepository must be initialized")
        }
    }

}