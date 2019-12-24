package com.liang.helper.recyclerview.decoration

import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import com.liang.helper.recyclerview.LayoutManagerHelper

abstract class BaseItemDecoration : ItemDecoration() {
    protected fun getOrientation(parent: RecyclerView?) =
        LayoutManagerHelper.getOrientation(parent!!)

    protected fun isReverseLayout(parent: RecyclerView?) =
        LayoutManagerHelper.isReverseLayout(parent!!)
}