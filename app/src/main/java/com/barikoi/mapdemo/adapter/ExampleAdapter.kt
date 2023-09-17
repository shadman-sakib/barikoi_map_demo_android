package com.barikoi.mapdemo.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.barikoi.mapdemo.R

class ExampleAdapter(private val examples: ArrayList<String>):
    RecyclerView.Adapter<ExampleAdapter.ViewHolder>() {

    private var onClickListener: ExampleAdapter.OnClickListener? =null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(android.R.layout.simple_list_item_1, parent, false)

        return ExampleAdapter.ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return examples.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val text: TextView = holder.itemView as TextView
        text.text=examples[position]
        holder.itemView.setOnClickListener {
            if (onClickListener != null) {
                onClickListener!!.onClick(position, examples[position] )
            }
        }
        holder.itemView.setOnClickListener(View.OnClickListener {
            onClickListener?.onClick(position,examples[position])
        })
    }
    // A function to bind the onclickListener.
    fun setOnClickListener(onClickListener: OnClickListener) {
        this.onClickListener = onClickListener
    }

    // onClickListener Interface
    interface OnClickListener {
        fun onClick(position: Int, data: String)
    }


    class ViewHolder(view: View):RecyclerView.ViewHolder(view)  {

    }


}