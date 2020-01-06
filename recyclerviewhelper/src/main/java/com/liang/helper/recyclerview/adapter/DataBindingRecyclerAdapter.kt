package com.liang.helper.recyclerview.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView

abstract class DataBindingRecyclerAdapter<T>(diffCallback: DiffUtil.ItemCallback<T>? = null) :
    RecyclerAdapter<T, RecyclerViewHolder>(diffCallback) {

    final override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder =
        RecyclerViewHolder(
            DataBindingUtil.inflate<ViewDataBinding>(
                LayoutInflater.from(parent.context),
                getItemLayout(viewType),
                parent,
                false
            ).root
        )

    final override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
        val viewDataBinding: ViewDataBinding? = DataBindingUtil.getBinding(holder.itemView)
        onBindViewHolder(viewDataBinding, getItem(position), position)
    }

    protected abstract fun getItemLayout(viewType: Int): Int

    protected abstract fun onBindViewHolder(
        viewDataBinding: ViewDataBinding?,
        item: T,
        position: Int
    )
}

class RecyclerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)