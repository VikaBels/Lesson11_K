package com.example.lesson11_k

import android.annotation.SuppressLint


@SuppressLint("NotConstructor")
class State(private var name: String?, private var data: String?) {
    fun getName(): String? {
        return name
    }

    fun setName(name: String?) {
        this.name = name
    }

    fun getData(): String? {
        return data
    }

    fun setData(data: String?) {
        this.data = data
    }
}
