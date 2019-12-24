package com.liang.helper.recyclerview

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

object LayoutManagerHelper {
    fun getOrientation(recyclerView: RecyclerView): Int {
        val layoutManager = recyclerView.layoutManager
        throwIfNotLinearLayoutManager(layoutManager)
        return (layoutManager as LinearLayoutManager?)!!.orientation
    }

    fun isReverseLayout(recyclerView: RecyclerView): Boolean {
        val layoutManager = recyclerView.layoutManager
        throwIfNotLinearLayoutManager(layoutManager)
        return (layoutManager as LinearLayoutManager?)!!.reverseLayout
    }

    private fun throwIfNotLinearLayoutManager(layoutManager: RecyclerView.LayoutManager?) {
        check(layoutManager is LinearLayoutManager) {
            "LayoutManager can only be used with a " +
                    "LinearLayoutManager."
        }
    }
}