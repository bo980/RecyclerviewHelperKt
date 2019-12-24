package com.liang.helper.recyclerview.adapter

import android.os.Build
import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableList
import androidx.recyclerview.widget.RecyclerView


abstract class RecyclerAdapter<T, VH : RecyclerView.ViewHolder> : RecyclerView.Adapter<VH>() {

    private val items by lazy { ObservableArrayList<T>() }

    final override fun getItemCount(): Int = items.size

    fun getItem(position: Int): T = items[position]

    fun getItems(): ArrayList<T> = items

    fun add(data: T): Boolean = items.add(data)

    fun add(position: Int, data: T) {
        items.add(position, data)
    }

    fun add(vararg data: T) = items.addAll(data)

    fun addAll(position: Int, dataList: Collection<T>) = items.addAll(position, dataList)

    fun addAll(dataList: Collection<T>) = items.addAll(dataList)

    fun set(position: Int, data: T): T = items.set(position, data)

    fun clear() {
        items.clear()
    }

    fun remove(data: T) = items.remove(data)

    fun remove(position: Int): T = items.removeAt(position)

    fun removeIf(action: (t: T) -> Boolean) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            items.removeIf { t -> action(t) }
        }
    }

    fun replaceAll(action: (t: T) -> T) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            items.replaceAll { t -> action(t) }
        }
    }

    fun <R : Comparable<R>> sort(action: (t: T) -> R) {
        items.sortBy { action(it) }
    }

    fun isEmpty() = items.isEmpty()

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        items.addOnListChangedCallback(listChangedCallback)
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        items.removeOnListChangedCallback(listChangedCallback)
    }

    private val listChangedCallback =
        object : ObservableList.OnListChangedCallback<ObservableArrayList<T>>() {
            override fun onChanged(sender: ObservableArrayList<T>?) {
                notifyDataSetChanged()
            }

            override fun onItemRangeRemoved(
                sender: ObservableArrayList<T>?,
                positionStart: Int,
                itemCount: Int
            ) {
                notifyItemRangeRemoved(positionStart, itemCount)
                notifyItemRangeChanged(positionStart, itemCount)
            }

            override fun onItemRangeMoved(
                sender: ObservableArrayList<T>?,
                fromPosition: Int,
                toPosition: Int,
                itemCount: Int
            ) {
                notifyItemRangeChanged(fromPosition.coerceAtMost(toPosition), itemCount)
            }

            override fun onItemRangeInserted(
                sender: ObservableArrayList<T>?,
                positionStart: Int,
                itemCount: Int
            ) {
                notifyItemRangeInserted(positionStart, itemCount)
            }

            override fun onItemRangeChanged(
                sender: ObservableArrayList<T>?,
                positionStart: Int,
                itemCount: Int
            ) {
                notifyItemRangeChanged(positionStart, itemCount)
            }

        }
}