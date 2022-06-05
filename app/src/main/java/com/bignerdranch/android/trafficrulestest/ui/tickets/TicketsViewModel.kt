package com.bignerdranch.android.trafficrulestest.ui.tickets

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bignerdranch.android.trafficrulestest.data.Ticket
import com.bignerdranch.android.trafficrulestest.db.TicketsObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TicketsViewModel : ViewModel() {

    private val ticketsRepository = TicketsRepository.get()
    val ticketsLiveData = MutableLiveData<List<Ticket>?>()

    fun addTickets() {
        viewModelScope.launch(Dispatchers.IO) {
            if (ticketsRepository.getTickets().isNullOrEmpty()) {
                viewModelScope.launch(Dispatchers.IO) {
                    ticketsRepository.addTickets(TicketsObject.createTickets())
                }
            }
        }
    }

    fun getTickets() {
        viewModelScope.launch(Dispatchers.IO) {
            ticketsLiveData.postValue(ticketsRepository.getTickets())
        }
    }

}