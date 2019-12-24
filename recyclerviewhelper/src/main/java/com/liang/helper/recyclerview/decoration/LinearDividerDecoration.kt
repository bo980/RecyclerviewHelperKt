package com.liang.helper.recyclerview.decoration

import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class LinearDividerDecoration : BaseItemDecoration {
    private var mDivider: Drawable

    constructor(drawable: Drawable) {
        mDivider = drawable
    }

    constructor(dividerWidth: Int, color: Int) {
        val gradientDrawable = GradientDrawable()
        gradientDrawable.setColor(color)
        gradientDrawable.shape = GradientDrawable.RECTANGLE
        gradientDrawable.setSize(dividerWidth, dividerWidth)
        mDivider = gradientDrawable
    }

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        if (getOrientation(parent) == LinearLayoutManager.VERTICAL) {
            outRect[0, 0, 0] = mDivider.intrinsicHeight
        } else {
            outRect[0, 0, mDivider.intrinsicHeight] = 0
        }
    }

    override fun onDraw(
        c: Canvas,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.onDraw(c, parent, state)
        if (getOrientation(parent) == LinearLayoutManager.VERTICAL) {
            drawVertical(c, parent)
        } else {
            drawHorizontal(c, parent)
        }
    }

    private fun drawVertical(c: Canvas?, parent: RecyclerView) {
        val left = parent.paddingLeft
        val right = parent.width - parent.paddingRight
        val recyclerViewTop = parent.paddingTop
        val recyclerViewBottom = parent.height - parent.paddingBottom
        val childCount = parent.childCount
        for (i in 0 until childCount) {
            val child = parent.getChildAt(i)
            val params = child
                .layoutParams as RecyclerView.LayoutParams
            val top =
                recyclerViewTop.coerceAtLeast(child.bottom + params.bottomMargin)
            val bottom =
                recyclerViewBottom.coerceAtMost(top + mDivider.intrinsicHeight)
            mDivider.setBounds(left, top, right, bottom)
            mDivider.draw(c!!)
        }
    }

    private fun drawHorizontal(c: Canvas?, parent: RecyclerView) {
        val top = parent.paddingTop
        val bottom = parent.height - parent.paddingBottom
        val recyclerViewLeft = parent.paddingLeft
        val recyclerViewRight = parent.width - parent.paddingRight
        val childCount = parent.childCount
        for (i in 0 until childCount) {
            val child = parent.getChildAt(i)
            val params = child
                .layoutParams as RecyclerView.LayoutParams
            val left =
                recyclerViewLeft.coerceAtLeast(child.right + params.rightMargin)
            val right =
                recyclerViewRight.coerceAtMost(left + mDivider.intrinsicHeight)
            mDivider.setBounds(left, top, right, bottom)
            mDivider.draw(c!!)
        }
    }
}