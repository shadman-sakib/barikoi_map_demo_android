package com.barikoi.mapdemo.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.barikoi.mapdemo.R
import com.barikoi.mapdemo.model.Instructions

class InstructionAdapter(private val instructions: ArrayList<Instructions>): RecyclerView.Adapter<InstructionAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.instructions_view, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, i: Int) {
        holder.direction.text=instructions[i].text
        instructions[i].time?.let {
            holder.time.text= "${(it/60000)}min"
        }
        instructions[i].distance?.let {
            holder.distance.text = "${it}m"
        }
        instructions[i].sign?.let {
            when(it){
                -8 -> holder.sign.setImageResource(R.drawable.u_turn)
                -7 -> holder.sign.setImageResource(R.drawable.circle)
                -6 -> holder.sign.setImageResource(R.drawable.u_turn)
                -5 -> holder.sign.setImageResource(R.drawable.u_turn)
                -4 -> holder.sign.setImageResource(R.drawable.u_turn)
                -3 -> holder.sign.setImageResource(R.drawable.turn_left)
                -2 -> holder.sign.setImageResource(R.drawable.corner_up_left)
                -1 -> holder.sign.setImageResource(R.drawable.left_turn)
                -0 -> holder.sign.setImageResource(R.drawable.circle)
                1 -> holder.sign.setImageResource(R.drawable.right_turn)
                2 -> holder.sign.setImageResource(R.drawable.corner_up_right)
                3 -> holder.sign.setImageResource(R.drawable.up_right)
                4 -> holder.sign.setImageResource(R.drawable.circle)
                5 -> holder.sign.setImageResource(R.drawable.circle)
                6 -> holder.sign.setImageResource(R.drawable.circle)
                7 -> holder.sign.setImageResource(R.drawable.circle)
                8 -> holder.sign.setImageResource(R.drawable.u_turn)


            }
        }
    }

    override fun getItemCount() = instructions.size




    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val sign: ImageView = itemView.findViewById(R.id.imageViewSign)
        val direction: TextView = itemView.findViewById(R.id.textViewDirections)
        val distance:  TextView = itemView.findViewById(R.id.textViewDistance)
        val time: TextView =itemView.findViewById((R.id.textViewTime))
    }
}