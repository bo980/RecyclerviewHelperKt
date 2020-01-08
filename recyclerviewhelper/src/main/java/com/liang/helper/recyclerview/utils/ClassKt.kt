package com.liang.helper.recyclerview.utils

fun <K, V> MutableMap<K, V>.acquire(key: K): V? {
    return this.remove(key)
}

fun <K, V> MutableMap<K, V>.release(key: K, value: V): V? {
    return this.put(key, value)
}
