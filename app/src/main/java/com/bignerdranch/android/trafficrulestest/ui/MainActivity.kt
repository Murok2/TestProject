package com.bignerdranch.android.trafficrulestest.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.bignerdranch.android.trafficrulestest.databinding.ActivityMainBinding
import com.bignerdranch.android.trafficrulestest.ui.tickets.TicketsActivity
import com.bignerdranch.android.trafficrulestest.ui.tickets.TicketsViewModel

class MainActivity : AppCompatActivity() {

    private val viewModel: TicketsViewModel by lazy {
        ViewModelProvider(this).get(TicketsViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel.addTickets()
        binding.btnStart.setOnClickListener {
            TicketsActivity.start(this)
        }
    }

}