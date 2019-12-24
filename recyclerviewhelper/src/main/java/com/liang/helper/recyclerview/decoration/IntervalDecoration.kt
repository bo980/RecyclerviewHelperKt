package com.liang.helper.recyclerview.decoration

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager

class IntervalDecoration constructor(private var mSpanCount: Int = 0, private var mInterval: Int) :
    BaseItemDecoration() {

    fun setSpanCount(spanCount: Int) {
        mSpanCount = spanCount
    }

    fun setInterval(interval: Int) {
        mInterval = interval
    }

    override fun getItemOffsets(
        outRect: Rect, view: View, parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        val layoutManager = parent.layoutManager ?: return
        val position = parent.getChildAdapterPosition(view)
        when (layoutManager) {
            is GridLayoutManager -> {
                initGridLayoutManager(outRect, position)
            }
            is LinearLayoutManager -> {
                initLinearLayoutManager(outRect, parent, position)
            }
            is StaggeredGridLayoutManager -> {
                initStaggeredGridLayoutManager(outRect, position)
            }
        }
    }

    private fun initLinearLayoutManager(
        outRect: Rect,
        parent: RecyclerView,
        position: Int
    ) {
        if (getOrientation(parent) == LinearLayoutManager.VERTICAL) {
            if (position == 0) {
                outRect.top = mInterval
            }
            outRect.bottom = mInterval
        } else {
            if (position == 0) {
                outRect.left = mInterval
            }
            outRect.right = mInterval
        }
    }

    private fun initGridLayoutManager(
        outRect: Rect,
        position: Int
    ) {
        if (mSpanCount <= 0) {
            throw ExceptionInInitializerError("spanCount must > 0")
        }
        val column = position % mSpanCount
        outRect.left = mInterval - column * mInterval / mSpanCount
        outRect.right = (column + 1) * mInterval / mSpanCount
        if (position < mSpanCount) {
            outRect.top = mInterval
        }
        outRect.bottom = mInterval
    }

    private fun initStaggeredGridLayoutManager(
        outRect: Rect,
        position: Int
    ) {
        if (mSpanCount <= 0) {
            throw ExceptionInInitializerError("spanCount must > 0")
        }
        if (position < mSpanCount) {
            outRect.top = mInterval
        }
        outRect.bottom = mInterval
        val column = position % mSpanCount
        outRect.left = mInterval - column * mInterval / mSpanCount
        outRect.right = (column + 1) * mInterval / mSpanCount
    }
}