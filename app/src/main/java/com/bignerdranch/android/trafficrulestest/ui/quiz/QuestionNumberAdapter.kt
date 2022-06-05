package com.bignerdranch.android.trafficrulestest.ui.quiz

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class QuestionNumberAdapter(var questionListener: QuestionListener): RecyclerView.Adapter<QuestionNumberViewHolder>() {

    private var questions: MutableList<Pair<Int, Boolean?>> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuestionNumberViewHolder {
        return QuestionNumberViewHolder.from(parent, questionListener)
    }

    override fun onBindViewHolder(holder: QuestionNumberViewHolder, position: Int) {
        val questionNumber = questions[position]
        holder.bind(questionNumber)
    }

    override fun getItemCount() = questions.size

    fun setQuestionsNumber(size: Int) {
        questions.clear()
        for (i in 1..size) questions.add(Pair(i, null))
        notifyDataSetChanged()
    }

    fun updateQuestionsNumber(number: Int, isCorrectAnswer: Boolean) {
        questions = questions.apply { this[number] = Pair(number + 1, isCorrectAnswer) }
        notifyDataSetChanged()
    }

}

interface QuestionListener {
    fun onQuestionNumberClick(number: Int)
}