package com.bignerdranch.android.trafficrulestest.ui.quiz

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bignerdranch.android.trafficrulestest.data.Quiz
import com.bignerdranch.android.trafficrulestest.data.Ticket
import com.bignerdranch.android.trafficrulestest.ui.tickets.TicketsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

class QuizViewModel : ViewModel() {

    var ticket: MutableList<Quiz> = mutableListOf()
    var questionCounter: Int = 0
    var questionsTotalNumber: Int = 0
    lateinit var currentQuestion: Quiz
    var isAnswered: Boolean = false
    var score: Int = 0
    var answeredQuestions = mutableListOf<Pair<Int, Boolean>>()

    private val ticketsRepository = TicketsRepository.get()
    val quizzesLiveData = MutableLiveData<Ticket?>()

    fun getQuizzes(id: UUID) {
        viewModelScope.launch(Dispatchers.IO) {
            quizzesLiveData.postValue(ticketsRepository.getQuizzes(id))
        }
    }

    fun setQuizTicket(quizzes: MutableList<Quiz>) {
        ticket.clear()
        ticket.addAll(quizzes)
    }

    fun setCurrentQuestion(quizzes: MutableList<Quiz>) {
        currentQuestion = quizzes[questionCounter - 1]
    }

    fun setQuestionsTotalNum(questionsCount: Int) {
        questionsTotalNumber = questionsCount
    }

    fun isQuestionNotAnswered() = questionCounter < questionsTotalNumber && !answeredQuestions.contains(Pair(questionCounter, true))

    fun isQuestionAnswered() = questionCounter < questionsTotalNumber && answeredQuestions.contains(Pair(questionCounter, true))

    fun isQuestionExistAndQuizNotFinished() = questionCounter == questionsTotalNumber && !answeredQuestions.contains(Pair(questionCounter, true)) && answeredQuestions.size != questionsTotalNumber

    fun isQuestionsOver() = answeredQuestions.size == questionsTotalNumber

    fun updateScore(id: UUID) {
        viewModelScope.launch(Dispatchers.IO) {
            ticketsRepository.updateScore(id, score)
        }
    }

}