package com.liang.helper.recyclerview.adapter

import android.annotation.SuppressLint
import android.os.Build
import androidx.arch.core.executor.ArchTaskExecutor
import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableList
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.liang.helper.recyclerview.diff.DiffHelper


/**
 * diffCallback
 * 当参数diffCallback不为空的时候，更新数据必须调用submit(list: ArrayList<T>)方法才能更新列表
 * @param diffCallback The Adapter to send updates to.
 */
abstract class RecyclerAdapter<T, VH : RecyclerView.ViewHolder>
@JvmOverloads constructor(private val diffCallback: DiffUtil.ItemCallback<T>? = null) :
    RecyclerView.Adapter<VH>() {

    private var listChangedCallback: ObservableList.OnListChangedCallback<ObservableArrayList<T>>? =
        null

    private val items by lazy { ObservableArrayList<T>() }

    private val diffHelper by lazy { DiffHelper() }

    init {
        listChangedCallback = if (diffCallback == null) {
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
        } else null
    }

    final override fun getItemCount(): Int = items.size

    fun getItem(position: Int): T = items[position]

    fun getItems(): ArrayList<T> = items

    @SuppressLint("RestrictedApi")
    fun submitList(list: List<T>) {
        if (diffCallback == null) {
            throw IllegalArgumentException("Adapter的构造方法中必须传入对DiffUtil.ItemCallback的实现！")
        }
        val taskExecutor = ArchTaskExecutor.getInstance()
        taskExecutor.executeOnDiskIO {
            val oldList = getItems()
            val diffResult = diffHelper.computeDiff(oldList, list, diffCallback)
            taskExecutor.executeOnMainThread {
                diffResult.dispatchUpdatesTo(this)
                clear()
                addAll(list)
            }
        }
    }

    fun add(data: T): Boolean = items.add(data)

    fun add(position: Int, data: T) {
        items.add(position, data)
    }

    fun add(vararg data: T) = items.addAll(data)

    fun addAll(position: Int, dataList: Collection<T>) = items.addAll(position, dataList)

    /**
     * 添加数据，如果是刷新列表先clear()再addAll()
     * 当参数diffCallback不为空的时候，更新数据必须调用submit(list: ArrayList<T>)方法才能更新列表
     * @param dataList
     */
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
        if (diffCallback != null || listChangedCallback == null) {
            return
        }
        items.addOnListChangedCallback(listChangedCallback)
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        if (diffCallback != null || listChangedCallback == null) {
            return
        }
        items.removeOnListChangedCallback(listChangedCallback)
    }

}