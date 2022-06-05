package com.bignerdranch.android.trafficrulestest.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.bignerdranch.android.trafficrulestest.data.Ticket
import java.util.*

@Dao
interface QuizDao {

    @Query("SELECT * FROM ticket WHERE id=(:id)")
    suspend fun getQuizzes(id: UUID): Ticket

    @Query("SELECT * FROM ticket")
    suspend fun getTickets(): List<Ticket>

    @Insert
    suspend fun addTickets(tickets: List<Ticket>)

    @Query("UPDATE ticket SET score=(:score) WHERE id=(:id)")
    suspend fun updateScore(id: UUID, score: Int)
}
