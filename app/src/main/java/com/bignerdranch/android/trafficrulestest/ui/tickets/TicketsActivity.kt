package com.bignerdranch.android.trafficrulestest.ui.tickets

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bignerdranch.android.trafficrulestest.R
import com.bignerdranch.android.trafficrulestest.databinding.ActivityTicketsBinding
import com.bignerdranch.android.trafficrulestest.ui.quiz.QuizActivity
import com.bignerdranch.android.trafficrulestest.utils.startActivity
import com.bignerdranch.android.trafficrulestest.utils.startDialog
import java.util.*

class TicketsActivity : AppCompatActivity(), TicketListener {

    private lateinit var rvTicket: RecyclerView
    private lateinit var adapter: TicketAdapter
    private lateinit var binding: ActivityTicketsBinding

    private val viewModel: TicketsViewModel by lazy {
        ViewModelProvider(this).get(TicketsViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTicketsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        observeLiveData()
    }

    private fun observeLiveData() {
        viewModel.ticketsLiveData.observe(this, { tickets ->
            tickets?.let {
                rvTicket = binding.rvTicket
                adapter = TicketAdapter( this)
                adapter.setTickets(tickets)
                rvTicket.layoutManager = GridLayoutManager(this, 2)
                rvTicket.adapter = adapter
            }
        })
    }

    override fun onTicketClick(id: UUID, number: Int) {
        startDialog(getString(R.string.ticket_number, number), getString(R.string.test_limit_description), {
            QuizActivity.start(this, id, number)
        })
    }

    override fun onResume() {
        viewModel.getTickets()
        super.onResume()
    }

    companion object {
        fun start(context: Context?) {
            context?.startActivity<TicketsActivity>()
        }
    }

}