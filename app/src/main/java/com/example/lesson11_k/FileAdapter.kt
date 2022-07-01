package com.example.lesson11_k
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

class FileAdapter(context: Context?, private val layout: Int, private val files: List<CurrentFile>) :
    ArrayAdapter<CurrentFile?>(context!!, layout, files) {

    private val inflater: LayoutInflater

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = inflater.inflate(layout, parent, false)
        val nameView = view.findViewById<TextView>(R.id.name)
        val dataView = view.findViewById<TextView>(R.id.data)
        val file = files[position]
        nameView.text = file.getName()
        dataView.text = file.getData()
        return view
    }

    init {
        inflater = LayoutInflater.from(context)
    }
}