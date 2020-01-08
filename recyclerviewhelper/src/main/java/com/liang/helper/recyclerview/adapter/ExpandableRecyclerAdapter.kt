package com.liang.helper.recyclerview.adapter

import android.annotation.SuppressLint
import android.text.method.TextKeyListener.clear
import android.util.Log
import android.util.SparseArray
import androidx.arch.core.executor.ArchTaskExecutor
import androidx.core.util.Pools
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.liang.helper.recyclerview.diff.DiffHelper
import kotlin.collections.ArrayList

abstract class ExpandableRecyclerAdapter<VH : RecyclerView.ViewHolder>(
    private val diffCallback: DiffUtil.ItemCallback<Any> = object : DiffUtil.ItemCallback<Any>() {
        override fun areItemsTheSame(oldItem: Any, newItem: Any) =
            oldItem === newItem

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(oldItem: Any, newItem: Any) =
            oldItem == newItem
    }
) : RecyclerView.Adapter<VH>() {

    private val diffHelper by lazy { DiffHelper() }

    private var items = arrayListOf<Any>()

    val levelMap = mutableMapOf<Int, LevelItem>()

    val levelArray = SparseArray<LevelItem>()


    fun submitLevelList(level: Int, list: List<Any>) {
        levelMap[level] = LevelItem(level, list)
        val tsets = items.clone() as ArrayList<Any>
        tsets.addAll(list)
        submitList(tsets)
    }

    @SuppressLint("RestrictedApi")
    private fun submitList(list: List<Any>) {
        val taskExecutor = ArchTaskExecutor.getInstance()
        taskExecutor.executeOnDiskIO {
            val oldList = items
            val diffResult = diffHelper.computeDiff(oldList, list, diffCallback)
            taskExecutor.executeOnMainThread {
                diffResult.dispatchUpdatesTo(this)
                items.clear()
                items.addAll(list)
            }
        }
    }

    fun removeLevelList(level: Int) {
        val tsets = items.clone() as ArrayList<Any>
        Log.e(javaClass.simpleName, "tsets: $tsets")
        levelMap[level]?.list?.let {
            tsets.removeAll(it)
        }
        submitList(tsets)
    }

}

data class LevelItem(var level: Int, var list: List<Any>)

