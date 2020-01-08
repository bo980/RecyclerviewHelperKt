package com.liang.recyclerviewhelperkt

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private val adapter by lazy { TestAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
        add.setOnClickListener {
            addData()
        }

        remove.setOnClickListener {
            //            adapter.removeLevelList(2)
        }
    }

    var level = -1
    private fun addData() {
        level++
        val test = arrayListOf<String>()
        repeat(5) {
            test.add("${level}级列表:$it")
        }
        adapter.submitLevelList("${level - 1}级列表:3", level, test)
    }
}
