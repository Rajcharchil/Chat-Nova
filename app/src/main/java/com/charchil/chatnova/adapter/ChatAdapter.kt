package com.charchil.chatnova.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.charchil.chatnova.R
import com.charchil.chatnova.models.ChatMessage

class ChatAdapter(private val messages: ArrayList<ChatMessage>) :
    RecyclerView.Adapter<ChatAdapter.ChatViewHolder>() {

    class ChatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tVMessage: TextView = itemView.findViewById(R.id.tVMessage)
        val tVUserMessage: TextView = itemView.findViewById(R.id.tVUserMessage)
        val progressBar: ProgressBar = itemView.findViewById(R.id.progressBar)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_chat, parent, false)
        return ChatViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        val message = messages[position]

        if (message.isUser) {
            // User Message (Right side)
            holder.tVUserMessage.text = message.text
            holder.tVUserMessage.visibility = View.VISIBLE
            holder.tVMessage.visibility = View.GONE
            holder.tVUserMessage.setBackgroundResource(R.drawable.user_bubble)
            holder.progressBar.visibility = View.GONE
        } else {
            // AI Message (Left side)
            if(message.isLoading){
                holder.progressBar.visibility = View.VISIBLE
                holder.tVMessage.text = message.text
                holder.tVMessage.visibility = View.GONE
                holder.tVUserMessage.visibility = View.GONE
                return
            }else if (!message.isLoading){
                holder.progressBar.visibility = View.GONE
                holder.tVMessage.text = message.text
                holder.tVMessage.visibility = View.VISIBLE
                holder.tVUserMessage.visibility = View.GONE
                holder.tVMessage.setBackgroundResource(R.drawable.ai_bubble)
                return
            }
        }
    }

    override fun getItemCount(): Int {
        return messages.size
    }

    fun addMessage(message: ChatMessage) {
        messages.add(message)
        notifyItemInserted(messages.size - 1)
    }
}
