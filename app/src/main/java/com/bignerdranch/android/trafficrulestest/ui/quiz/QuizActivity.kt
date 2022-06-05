package com.bignerdranch.android.trafficrulestest.ui.quiz

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bignerdranch.android.trafficrulestest.R
import com.bignerdranch.android.trafficrulestest.databinding.ActivityQuizBinding
import com.bignerdranch.android.trafficrulestest.utils.startActivity
import com.bignerdranch.android.trafficrulestest.utils.startConfirmationDialog
import com.bignerdranch.android.trafficrulestest.utils.startDialog
import com.bignerdranch.android.trafficrulestest.utils.startExplainingDialog
import java.util.*

class QuizActivity : AppCompatActivity(), QuestionListener {

    private lateinit var rvQuestionsNumber: RecyclerView
    private lateinit var adapter: QuestionNumberAdapter
    private lateinit var binding: ActivityQuizBinding
    private var timeLeft: Long = 0
    private var countDownTimer: CountDownTimer? = null

    private val viewModel: QuizViewModel by lazy {
        ViewModelProvider(this).get(QuizViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQuizBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel.getQuizzes(UUID.fromString(intent.getStringExtra(TICKET_ID)))
        observeLiveData(savedInstanceState)
        with(binding) {
            toolbar.title = getString(R.string.ticket_number, intent.getIntExtra(TICKET_NUMBER, 0))
            toolbar.setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.help -> {
                        when(viewModel.isAnswered) {
                            true -> startExplainingDialog(message = viewModel.currentQuestion.explanation_answer ?: getString(R.string.no_help))
                            false -> Toast.makeText(this@QuizActivity, getString(R.string.help_availability), Toast.LENGTH_LONG).show()
                        }
                        true
                    }
                    else -> false
                }
            }
            btnNext.setOnClickListener {
                if (!viewModel.isAnswered) {
                    if (optionOne.isChecked || optionTwo.isChecked || optionThree.isChecked || optionFour.isChecked) {
                        checkAnswer()
                    } else {
                        Toast.makeText(this@QuizActivity, getString(R.string.please_choose_answer), Toast.LENGTH_LONG).show()
                    }
                } else {
                    showQuestion()
                }
            }
        }
        timeLeft = COUNTDOWN_TIMER
        startCountDown()
    }

    private fun observeLiveData(savedInstanceState: Bundle?) {
        viewModel.quizzesLiveData.observe(this, { ticket ->
            ticket?.let {
                with(binding) {
                    if (savedInstanceState == null) {
                        viewModel.setQuestionsTotalNum(ticket.quizzes.size)
                        this@QuizActivity.rvQuestionsNumber = rvQuestionsNumber
                        adapter = QuestionNumberAdapter(this@QuizActivity)
                        adapter.setQuestionsNumber(viewModel.questionsTotalNumber)
                        rvQuestionsNumber.layoutManager = LinearLayoutManager(this@QuizActivity, LinearLayoutManager.HORIZONTAL, false)
                        rvQuestionsNumber.adapter = adapter
                        ticket.quizzes.shuffle()
                        viewModel.setQuizTicket(ticket.quizzes)
                        showQuestion()
                    } else viewModel.setCurrentQuestion(ticket.quizzes)
                }
            }
        })
    }

    @SuppressLint("ResourceType")
    private fun showQuestion() {
        with(binding) {
            optionOne.setTextColor(Color.parseColor(BLACK_COLOR))
            optionTwo.setTextColor(Color.parseColor(BLACK_COLOR))
            optionThree.setTextColor(Color.parseColor(BLACK_COLOR))
            optionFour.setTextColor(Color.parseColor(BLACK_COLOR))
            rgOptions.clearCheck()

            when {
                viewModel.isQuestionNotAnswered() -> {
                    viewModel.currentQuestion = viewModel.ticket[viewModel.questionCounter]
                    ivQuestion.setImageResource(viewModel.currentQuestion.image)
                    tvQuestion.text = viewModel.currentQuestion.question
                    optionOne.text = viewModel.currentQuestion.option_one
                    optionTwo.text = viewModel.currentQuestion.option_two
                    optionThree.apply {
                        if (viewModel.currentQuestion.option_three.isNullOrEmpty()) visibility = View.GONE
                        else {
                            visibility = View.VISIBLE
                            text = viewModel.currentQuestion.option_three
                        }
                    }
                    optionFour.apply {
                        if (viewModel.currentQuestion.option_four.isNullOrEmpty()) visibility = View.GONE
                        else {
                            visibility = View.VISIBLE
                            text = viewModel.currentQuestion.option_four
                        }
                    }
                    viewModel.isAnswered = false
                    btnNext.text = getString(R.string.answer)
                }
                viewModel.isQuestionAnswered() -> {
                    viewModel.questionCounter++
                    showQuestion()
                }
                viewModel.isQuestionExistAndQuizNotFinished() -> {
                    viewModel.questionCounter = 0
                    showQuestion()
                }
                else -> {
                    startConfirmationDialog(getString(R.string.test_is_over), getString(R.string.final_score, viewModel.score, viewModel.questionsTotalNumber), {
                        viewModel.updateScore(UUID.fromString(intent.getStringExtra(TICKET_ID)))
                        finish()
                    },{
                        viewModel.updateScore(UUID.fromString(intent.getStringExtra(TICKET_ID)))
                        finish()
                        start(this@QuizActivity, UUID.fromString(intent.getStringExtra(TICKET_ID)), intent.getIntExtra(TICKET_NUMBER, 0))
                    })
                }
            }
        }
    }

    override fun onQuestionNumberClick(number: Int) {
        viewModel.questionCounter = number - 1
        showQuestion()
    }

    private fun checkAnswer(){
        with(binding) {
            viewModel.isAnswered = true
            viewModel.answeredQuestions.add(Pair(viewModel.questionCounter, true))
            val selectedAnswer: RadioButton = findViewById(rgOptions.checkedRadioButtonId)
            val answerNumber: Int = rgOptions.indexOfChild(selectedAnswer) + 1
            if(answerNumber == viewModel.currentQuestion.correct_answer){
                viewModel.score++
                adapter.updateQuestionsNumber(viewModel.questionCounter, true)
            } else {
                showIncorrectAnswer(answerNumber)
                adapter.updateQuestionsNumber(viewModel.questionCounter, false)
            }
            rvQuestionsNumber.scrollToPosition(viewModel.questionCounter)
            showCorrectAnswer()
        }
    }

    private fun showCorrectAnswer() {
        with(binding) {
            when(viewModel.currentQuestion.correct_answer){
                1 -> optionOne.setTextColor(resources.getColor(R.color.correct_answer_color))
                2 -> optionTwo.setTextColor(resources.getColor(R.color.correct_answer_color))
                3 -> optionThree.setTextColor(resources.getColor(R.color.correct_answer_color))
                4 -> optionFour.setTextColor(resources.getColor(R.color.correct_answer_color))
            }
            if (!viewModel.isQuestionsOver()) btnNext.text = getString(R.string.next)
            else btnNext.text = getString(R.string.finish_test)
        }
    }

    private fun showIncorrectAnswer(answerNumber: Int) {
        with(binding) {
            when(answerNumber){
                1 -> optionOne.setTextColor(resources.getColor(R.color.incorrect_answer_color))
                2 -> optionTwo.setTextColor(resources.getColor(R.color.incorrect_answer_color))
                3 -> optionThree.setTextColor(resources.getColor(R.color.incorrect_answer_color))
                4 -> optionFour.setTextColor(resources.getColor(R.color.incorrect_answer_color))
            }
        }
    }

    private fun startCountDown() {
        countDownTimer = object : CountDownTimer(timeLeft, COUNTDOWN_INTERVAL) {
            override fun onTick(millisUntilFinished: Long) {
                timeLeft = millisUntilFinished
                updateCountDown()
            }
            override fun onFinish() {
                timeLeft = 0
                updateCountDown()
                startConfirmationDialog(getString(R.string.time_is_over), getString(R.string.final_score, viewModel.score, viewModel.questionsTotalNumber), {
                    finish()
                },{
                    viewModel.updateScore(UUID.fromString(intent.getStringExtra(TICKET_ID)))
                    start(this@QuizActivity, UUID.fromString(intent.getStringExtra(TICKET_ID)), intent.getIntExtra(TICKET_NUMBER, 0))
                })
            }
        }.start()
    }

    private fun updateCountDown() {
        with(binding) {
            val min = (timeLeft / 1000).toInt() / 60
            val sec = (timeLeft / 1000).toInt() % 60

            val timeFormat = String.format(Locale.getDefault(), "%02d:%02d", min, sec)
            toolbar.subtitle = getString(R.string.minutes_remain, timeFormat)

            if (timeLeft < 10000) toolbar.setSubtitleTextColor(Color.RED)
        }
    }

    override fun onBackPressed() {
        startDialog(getString(R.string.leave_test), getString(R.string.leave_test_confirm), {
            super.onBackPressed()
        }, getString(R.string.leave))
    }

    companion object {
        private const val BLACK_COLOR = "#000000"
        private const val TICKET_ID = "TICKET_ID"
        private const val TICKET_NUMBER = "TICKET_NUMBER"
        private const val COUNTDOWN_INTERVAL: Long = 1000
        private const val COUNTDOWN_TIMER: Long = 1500000
        fun start(context: Context?, id: UUID, number: Int) {
            context?.startActivity<QuizActivity> {
                putExtra(TICKET_ID, id.toString())
                putExtra(TICKET_NUMBER, number)
            }
        }
    }

}