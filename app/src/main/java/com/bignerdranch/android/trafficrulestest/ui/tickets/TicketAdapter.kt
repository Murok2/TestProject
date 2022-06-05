package com.bignerdranch.android.trafficrulestest.ui.tickets

import android.os.Build
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.bignerdranch.android.trafficrulestest.data.Ticket
import java.util.*

class TicketAdapter(var ticketListener: TicketListener): RecyclerView.Adapter<TicketViewHolder>() {

    private var tickets: MutableList<Ticket> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TicketViewHolder {
        return TicketViewHolder.from(parent, ticketListener)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onBindViewHolder(holder: TicketViewHolder, position: Int) {
        val ticket = tickets[position]
        val number = position + 1
        holder.bind(ticket, number)
    }

    override fun getItemCount() = tickets.size

    fun setTickets(items: List<Ticket>) {
        tickets.clear()
        tickets.addAll(items)
        notifyDataSetChanged()
    }

}

interface TicketListener {
    fun onTicketClick(id: UUID, number: Int)
}