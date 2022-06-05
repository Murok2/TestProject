package com.bignerdranch.android.trafficrulestest.ui.quiz

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bignerdranch.android.trafficrulestest.R
import com.bignerdranch.android.trafficrulestest.databinding.ListItemQuestionNumberBinding
import kotlin.properties.Delegates

class QuestionNumberViewHolder(private val binding: ListItemQuestionNumberBinding): RecyclerView.ViewHolder(binding.root) {

    var questionNumber by Delegates.notNull<Int>()

    @SuppressLint("ResourceAsColor", "UseCompatLoadingForDrawables")
    fun bind(questionNumber: Pair<Int, Boolean?>) {
        this.questionNumber = questionNumber.first
        binding.btnQuestionNumber.text = questionNumber.first.toString()
        when(questionNumber.second) {
            true -> binding.btnQuestionNumber.setBackgroundColor(itemView.resources.getColor(R.color.green))
            false -> binding.btnQuestionNumber.setBackgroundColor(itemView.resources.getColor(R.color.incorrect_answer_color))
            else -> binding.btnQuestionNumber.setBackgroundColor(itemView.resources.getColor(R.color.dark_grey))
        }
    }

    companion object {
        fun from(parent: ViewGroup, questionListener: QuestionListener): QuestionNumberViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ListItemQuestionNumberBinding.inflate(layoutInflater, parent, false)
            return QuestionNumberViewHolder(binding).apply {
                binding.btnQuestionNumber.setOnClickListener {
                    questionListener.onQuestionNumberClick(questionNumber)
                }
            }
        }
    }

}