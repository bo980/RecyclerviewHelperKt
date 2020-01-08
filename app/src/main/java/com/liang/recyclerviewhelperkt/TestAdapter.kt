package com.liang.recyclerviewhelperkt

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.liang.helper.recyclerview.adapter.ExpandableRecyclerAdapter

class TestAdapter : ExpandableRecyclerAdapter<ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = View.inflate(parent.context, R.layout.item_test_layout, null)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(position)?.let {
            holder.text.text = "$it"
        }
    }
}

class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val text: TextView = itemView.findViewById(R.id.textView)
}