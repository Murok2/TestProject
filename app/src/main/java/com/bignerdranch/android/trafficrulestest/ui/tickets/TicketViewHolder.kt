package com.bignerdranch.android.trafficrulestest.ui.tickets

import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.bignerdranch.android.trafficrulestest.R
import com.bignerdranch.android.trafficrulestest.data.Ticket
import com.bignerdranch.android.trafficrulestest.databinding.ListItemTicketBinding
import kotlin.properties.Delegates

class TicketViewHolder(private val binding: ListItemTicketBinding) : RecyclerView.ViewHolder(binding.root) {

    private lateinit var ticket: Ticket
    private var number by Delegates.notNull<Int>()

    @RequiresApi(Build.VERSION_CODES.M)
    fun bind(ticket: Ticket, number: Int) {
        this.ticket = ticket
        this.number = number
        with(binding){
            tvTicketNumber.text = itemView.context.getString(R.string.ticket_number, number)
            pbRemain.max = ticket.quizzes.size
            pbRemain.progress = ticket.score
            pbRemain.progressDrawable.setColorFilter(itemView.context.getColor(R.color.green), android.graphics.PorterDuff.Mode.SRC_IN)
            tvQuestionNumber.text = itemView.context.getString(R.string.question_remain, ticket.score, ticket.quizzes.size)
        }
    }

    companion object {
        fun from(parent: ViewGroup, ticketListener: TicketListener): TicketViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ListItemTicketBinding.inflate(layoutInflater, parent, false)
            return TicketViewHolder(binding).apply {
                binding.clTicket.setOnClickListener {
                    ticketListener.onTicketClick(ticket.id, number)
                }
            }
        }
    }

}