package com.liang.helper.recyclerview.adapter

import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
import android.util.SparseArray
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.arch.core.executor.ArchTaskExecutor
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.liang.helper.recyclerview.diff.DiffHelper
import java.lang.Exception
import kotlin.collections.ArrayList

abstract class ExpandableRecyclerAdapter<VH : RecyclerView.ViewHolder>(
    private val diffCallback: DiffUtil.ItemCallback<Any> = object : DiffUtil.ItemCallback<Any>() {
        override fun areItemsTheSame(oldItem: Any, newItem: Any) =
            oldItem === newItem

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(oldItem: Any, newItem: Any) =
            oldItem === newItem
    }
) : RecyclerView.Adapter<VH>() {

    private val diffHelper by lazy { DiffHelper() }

    private var items = arrayListOf<LevelItem>()

//    val levelMap = mutableMapOf<Any, LevelItem>()

    val levelArray = SparseArray<MutableMap<Any, Level>>()

    @RequiresApi(Build.VERSION_CODES.N)
    @JvmOverloads
    fun submitLevelList(
        lowerTag: Any,
        level: Int,
        list: List<Any>
//        isExpand: Boolean = level == 0
    ) {

        if (level < 0) throw Exception("level不能小于0!")
        var levelMap = levelArray.get(level)
        if (levelMap == null) {
            levelMap = mutableMapOf()
            levelArray.put(level, levelMap)
        }
        Log.e(javaClass.simpleName, "list: $list; lowerTag: $lowerTag")
        if (level > 0) {
            levelArray.get(level - 1)?.forEach { _, u ->
                u.list.forEach {
                    if ("${it.tag}" == "$lowerTag") {
                        Log.e(javaClass.simpleName, "it.tag: ${it.tag}; lowerTag: $lowerTag")
                        it.lowerTag = lowerTag
                    }
                }
            }
//            levelArray.get(level-1)?.get(lowerTag)?.list?.forEach {
//                Log.e(javaClass.simpleName, "list: $list; lowerTag: $lowerTag")
//                if (it.tag == lowerTag) {
//                    it.lowerTag = lowerTag
//                }
//            }
        }

        val levelList =
            list.map { LevelItem("$it", level, it) }
        levelMap[lowerTag] = Level("", level, levelList)

        if (level == 0) {
            submitList(levelList)
        }
    }

//    fun removeLevelList(tag: Any, level: Int) {
//        levelArray.get(level)?.get(tag)?.list?.let {
//            val tsets = items.clone() as ArrayList<LevelItem>
//            Log.e(javaClass.simpleName, "tsets: $tsets")
//            tsets.removeAll(it)
//            submitList(tsets)
//        }
//    }

    private fun expand(tag: Any?, level: Int, position: Int) {
        levelArray.get(level+1)?.get(tag)?.list?.let {
            val tsets = items.clone() as ArrayList<LevelItem>
            Log.e(javaClass.simpleName, "tsets: $tsets")
            tsets.addAll(position + 1, it)
            submitList(tsets)
        }
    }

    private fun fold(tag: Any?, level: Int) {
        levelArray.get(level+1)?.get(tag)?.list?.let {
            val tsets = items.clone() as ArrayList<LevelItem>
            Log.e(javaClass.simpleName, "tsets: $tsets")
            tsets.removeAll(it)
            submitList(tsets)
        }
    }

    @SuppressLint("RestrictedApi")
    private fun submitList(list: List<LevelItem>) {
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

    fun getItemViewLevel(position: Int) = getItem(position).level

    fun getItem(position: Int) = items[position]

    final override fun getItemCount() = items.size

    final override fun onBindViewHolder(holder: VH, position: Int) {
        onBindViewHolder(holder, getItem(position), position)
        holder.itemView.setOnClickListener {
            Log.e(javaClass.simpleName, "setOnClickListener: ${getItem(position).hasLowerLevel()}")
            if (getItem(position).hasLowerLevel()) {
                getItem(position).isLowerExpand = !getItem(position).isLowerExpand
                onExpandChanged(holder, getItem(position).isLowerExpand)
                if (getItem(position).isLowerExpand)
                    expand(getItem(position).lowerTag, getItem(position).level, position)
                else fold(getItem(position).lowerTag, getItem(position).level)
            }
        }
    }

    abstract fun onBindViewHolder(holder: VH, levelItem: LevelItem, position: Int)

    open fun onExpandChanged(holder: VH, isLowerExpand: Boolean) {}

}


data class Level(var tag: Any, var level: Int, var list: List<LevelItem>)

data class LevelItem(
    var tag: Any,
    var level: Int,
    var item: Any,
    var isLowerExpand: Boolean = false
) {
    var lowerTag: Any? = null
    var higherTag: Any? = null
    fun hasLowerLevel() = lowerTag != null
    fun hasHigherLevel() = higherTag != null
}
