package com.liang.helper.recyclerview.diff

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.DiffUtil.DiffResult

class DiffHelper {
    fun <T> computeDiff(
        oldList: List<T>,
        newList: List<T>,
        diffCallback: DiffUtil.ItemCallback<T>
    ): DiffResult {
        val oldSize: Int = oldList.size
        val newSize: Int = newList.size
        return DiffUtil.calculateDiff(object : DiffUtil.Callback() {
            override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {
                val oldItem: T = oldList[oldItemPosition]
                val newItem: T = newList[newItemPosition]
                return if (oldItem == null || newItem == null) {
                    null
                } else diffCallback.getChangePayload(oldItem, newItem)
            }

            override fun getOldListSize(): Int {
                return oldSize
            }

            override fun getNewListSize(): Int {
                return newSize
            }

            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                val oldItem: T = oldList[oldItemPosition]
                val newItem: T = newList[newItemPosition]
                if (oldItem === newItem) {
                    return true
                }
                return if (oldItem == null || newItem == null) {
                    false
                } else diffCallback.areItemsTheSame(oldItem, newItem)
            }

            override fun areContentsTheSame(
                oldItemPosition: Int,
                newItemPosition: Int
            ): Boolean {
                val oldItem: T = oldList[oldItemPosition]
                val newItem: T = newList[newItemPosition]
                if (oldItem === newItem) {
                    return true
                }
                return if (oldItem == null || newItem == null) {
                    false
                } else diffCallback.areContentsTheSame(oldItem, newItem)
            }
        }, true)
    }
}