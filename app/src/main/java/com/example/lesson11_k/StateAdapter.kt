package com.example.lesson11_k
import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView


class StateAdapter(context: Context?, private val layout: Int, private val states: List<State>) :
    ArrayAdapter<State?>(context!!, layout, states) {

    private val inflater: LayoutInflater

    @SuppressLint("ViewHolder")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = inflater.inflate(layout, parent, false)
        val nameView = view.findViewById<TextView>(R.id.name)
        val dataView = view.findViewById<TextView>(R.id.data)
        val state = states[position]
        nameView.text = state.getName()
        dataView.text = state.getData()
        return view
    }

    init {
        inflater = LayoutInflater.from(context)
    }
}