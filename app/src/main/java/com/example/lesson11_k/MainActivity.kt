package com.example.lesson11_k

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView.OnItemClickListener
import android.widget.Button
import android.widget.ListView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import java.io.File
import java.io.IOException
import java.util.*


class MainActivity : AppCompatActivity() {
    companion object {
        const val KEY_FILE_NAME = "fileName"
        const val KEY_FILE_DATA = "fileData"
        const val KEY_FOLDER_NAME = "/documents"
        const val txtNULL: String = "NULL"
        const val txtEmpty: String = ""
    }

    private var fileList: ListView? = null
    private var btnAdd: Button? = null

    private var defValueName: String = "NAME"
    private var defValueData: String = "DATA"

    private var listFiles = ArrayList<CurrentFile>()
    private lateinit var selectedFile: CurrentFile

    private var editFileActivityResultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            checkingResultOfCode(result)
        }


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
                    listFiles.add(CurrentFile(getValueName(), getValueData()))
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
        val fileAdapter = FileAdapter(this, R.layout.list_item, listFiles)
        fileList?.adapter = fileAdapter

        val itemListener =
            OnItemClickListener { parent, _, position, _ ->
                selectedFile = parent.getItemAtPosition(position) as CurrentFile

                onClickFileOpenEditFileActivity(selectedFile.getName().toString())
            }
        fileList?.onItemClickListener = itemListener
    }

    private fun onClickFileOpenEditFileActivity(txt: String) {
        val intent = Intent(this, EditFileActivity::class.java)
        intent.putExtra(KEY_FILE_NAME, txt)
        setResult(RESULT_OK, intent)
        editFileActivityResultLauncher.launch(intent)
    }


    private fun checkingResultOfCode(result: ActivityResult) {
        if (result.resultCode == RESULT_OK) {
            val data = result.data
            if (data != null) {
               //
            }
        }
    }

    override fun onCreate(savedInstanceFile: Bundle?) {
        super.onCreate(savedInstanceFile)
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
            onClickFileOpenEditFileActivity(txtNULL)
        })

    }

    override fun onDestroy() {
        super.onDestroy()

        fileList = null
        btnAdd = null
    }
}
