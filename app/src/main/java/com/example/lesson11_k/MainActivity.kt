package com.example.lesson11_k

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView.OnItemClickListener
import android.widget.Button
import android.widget.ListView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import java.io.File
import java.io.IOException
import java.util.*


class MainActivity : AppCompatActivity() {
    private var fileList: ListView? = null
    private var btnAdd: Button? = null

    companion object {
        const val KEY_FILE_NAME = "fileName"
        const val KEY_FILE_DATA = "fileData"
        const val KEY_FOLDER_NAME = "/documents"
        const val KEY_NUMBER_DOCUMENT = "numberOfEmpty"
        const val txtNULL: String = "NULL"
        const val txtEmpty: String = ""
    }

    private var defValueName: String = "NAME"
    private var defValueData: String = "DATA"

    private var states = ArrayList<State>()
    private lateinit var selectedState: State


    private fun findViewById() {
        fileList = findViewById(R.id.filesList)
        btnAdd = findViewById(R.id.btnAdd)
    }

    private fun findAllFiles() {
        val dir = File("$filesDir$KEY_FOLDER_NAME")
        val path = dir.toString()
        val created = dir.mkdir()
        if (!created) {
            val directory = File(path)
            val files: Array<File> = directory.listFiles()
            if (files.isEmpty()) {
                Toast.makeText(
                    this,
                    resources.getString(R.string.noFileInFolder),
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                for (i in files.indices) {
                    updateDataNameFile(files[i].name, Date(files[i].lastModified()).toString())
                    states.add(State(getValueName(), getValueData()))
                }
            }

        }
    }

    private fun updateDataNameFile(firstValue: String, secondValue: String) {
        val sp = getSharedPreferences(txtEmpty, Context.MODE_PRIVATE)
        val editor = sp.edit()
        editor.putString(KEY_FILE_NAME, firstValue)
        editor.putString(KEY_FILE_DATA, secondValue)

        editor.apply()
    }

    private fun getValueName(): String? {
        val sp = getSharedPreferences(txtEmpty, Context.MODE_PRIVATE)
        return sp.getString(KEY_FILE_NAME, defValueName)
    }

    private fun getValueData(): String? {
        val sp = getSharedPreferences(txtEmpty, Context.MODE_PRIVATE)
        return sp.getString(KEY_FILE_DATA, defValueData)
    }

    private fun workWithAdapter() {
        val stateAdapter = StateAdapter(this, R.layout.list_item, states)
        fileList?.adapter = stateAdapter

        val itemListener =
            OnItemClickListener { parent, _, position, _ ->
                selectedState = parent.getItemAtPosition(position) as State

                openWorkWithFileActivityForResult(selectedState.getName().toString())
            }
        fileList?.onItemClickListener = itemListener
    }

    private fun openWorkWithFileActivityForResult(txt: String) {
        val intent = Intent(this, WorkWithFileActivity::class.java)
        intent.putExtra(KEY_FILE_NAME, txt)
        setResult(RESULT_OK, intent)
        workWithFileActivityResultLauncher.launch(intent)
    }

    private var workWithFileActivityResultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val data = result.data
            if (data != null) {
                ///
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById()

        findAllFiles()

        workWithAdapter()

        btnAdd?.setOnClickListener(View.OnClickListener {
            val newFile = File("$filesDir$KEY_FOLDER_NAME/$txtNULL")
            try {
                newFile.createNewFile()
            } catch (ex: IOException) {
                println(ex)
            }

            openWorkWithFileActivityForResult(txtNULL)
        })

    }

    override fun onDestroy() {
        super.onDestroy()

        fileList = null
        btnAdd = null

    }

}