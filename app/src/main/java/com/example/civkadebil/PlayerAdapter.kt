package com.example.civkadebil

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlin.Unit

class PlayerAdapter(private val players: MutableList<addPlayer>, private val onDeleteClickListener: (Int) -> Unit, private val onCheckedChangeListener: (Int, Boolean) -> Unit) :
    RecyclerView.Adapter<PlayerAdapter.PlayerViewHolder>() {

    class PlayerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val playerNameTextView: TextView = itemView.findViewById(R.id.playerNameTextView)
        val aiCheckBox: CheckBox = itemView.findViewById(R.id.aiCheckBox)
        val delete: Button = itemView.findViewById(R.id.deleta)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayerViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.player, parent, false)
        return PlayerViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: PlayerViewHolder, position: Int) {
        val player = players[position]
        holder.playerNameTextView.text = player.name
        holder.aiCheckBox.isChecked = player.isAI

        // Obsługa zmiany stanu CheckBoxa
        holder.aiCheckBox.setOnCheckedChangeListener { _, isChecked ->
            onCheckedChangeListener.invoke(position, isChecked)
        }


        holder.delete.setOnClickListener {
            onDeleteClickListener.invoke(position)
        }

    }

    override fun getItemCount(): Int {
        return players.size
    }
}
data class addPlayer(val name: String, var isAI: Boolean)
