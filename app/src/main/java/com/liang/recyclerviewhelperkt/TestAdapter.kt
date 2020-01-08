package com.liang.recyclerviewhelperkt

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.liang.helper.recyclerview.adapter.ExpandableRecyclerAdapter
import com.liang.helper.recyclerview.adapter.LevelItem

class TestAdapter : ExpandableRecyclerAdapter<ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = View.inflate(parent.context, R.layout.item_test_layout, null)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, levelItem: LevelItem, position: Int) {
        holder.text.text = "$levelItem"
    }
}

class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val text: TextView = itemView.findViewById(R.id.textView)
}