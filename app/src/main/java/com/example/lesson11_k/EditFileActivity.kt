package com.example.lesson11_k

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.lesson11_k.MainActivity.Companion.KEY_FILE_NAME
import com.example.lesson11_k.MainActivity.Companion.KEY_FOLDER_NAME
import com.example.lesson11_k.MainActivity.Companion.txtEmpty
import java.io.*
import java.lang.Exception
import java.util.*


class EditFileActivity : AppCompatActivity() {
    companion object {
        const val NAME_FILE_FOR_EMPTY_LINE = "Document"
        const val SLASH_N = "\n"
    }

    private var editTextFile: EditText? = null

    private var nameFile: String = "movies.txt"

    private var mainActivityResultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        checkingResultOfCode(result)
    }

    private fun findViewById() {
        editTextFile = findViewById(R.id.editTextFile)
    }

    private fun changeAppBar() {
        val actionbar = supportActionBar
        actionbar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onSupportNavigateUp(): Boolean {
        onClickBtnBack()
        return true
    }


    private fun checkingNameForUniqueness(currentFileName: String): String {
        val directory = File(File("$filesDir$KEY_FOLDER_NAME").toString())
        val files: Array<File> = directory.listFiles()

        var secondPathFirstLine: String
        var arrForPattern: Array<String>
        val listRepeatingNumbers: MutableList<Int> = mutableListOf()

        var newNameFile = currentFileName

        for (i in files.indices) {
            if (files[i].name.contains(currentFileName)) {
                secondPathFirstLine = files[i].name.substring(currentFileName.length, files[i].name.length)

                if (secondPathFirstLine.contains(Regex("\\(\\d+\\)$"))) {
                    arrForPattern = secondPathFirstLine.split(Regex("[()]")).toTypedArray()
                    arrForPattern[1].toIntOrNull()?.let { listRepeatingNumbers.add(it) }
                }

                if (secondPathFirstLine.trim().isEmpty() && listRepeatingNumbers.isEmpty()) {
                    newNameFile = "$currentFileName (1)"
                }
            }
        }

        if (listRepeatingNumbers.isNotEmpty()) {
            newNameFile = currentFileName + " (${Collections.max(listRepeatingNumbers) + 1})"
        }

        return newNameFile
    }

    private fun saveText() {
        val currentFile = File("$filesDir$KEY_FOLDER_NAME/$nameFile")
        var renameFile = currentFile
        var newNameFile = ""

        var allFileText = editTextFile?.text.toString()
        var firstLine = allFileText.split(SLASH_N)[0]

        if (firstLine == txtEmpty) {
            firstLine = NAME_FILE_FOR_EMPTY_LINE
            editTextFile?.setText(firstLine + editTextFile?.text)
        }

        if (nameFile != firstLine) {
            newNameFile = checkingNameForUniqueness(firstLine)
            renameFile = File("$filesDir$KEY_FOLDER_NAME/$newNameFile")
            try {
                allFileText = newNameFile +SLASH_N+ editTextFile?.text.toString().split(SLASH_N)[1]
            } catch (e: Exception) {
                allFileText = newNameFile
            }
        } else {
            allFileText = editTextFile?.text.toString()
        }

        try {
            FileOutputStream(currentFile).use { oStream ->
                oStream.write(allFileText.toByteArray())

                currentFile.renameTo(renameFile)
            }
        } catch (fnfe: FileNotFoundException) {
            fnfe.printStackTrace()
        }
    }

    private fun openText() {
        val file = File("$filesDir$KEY_FOLDER_NAME/$nameFile")
        try {
            FileInputStream(file).use { iStream ->
                val byte = ByteArray(iStream.available())
                iStream.read(byte)
                val t = String(byte)
                editTextFile?.setText(t, TextView.BufferType.EDITABLE)
            }
        } catch (fnfe: FileNotFoundException) {
            fnfe.printStackTrace()
        }
    }

    private fun onClickBtnBack() {
        val intent = Intent(this, MainActivity::class.java)
        mainActivityResultLauncher.launch(intent)
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
        setContentView(R.layout.activity_work_with_file)

        findViewById()

        changeAppBar()

        nameFile = intent.getStringExtra(KEY_FILE_NAME).toString()

        openText()
    }

    override fun onPause() {
        saveText()
        super.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()

        editTextFile = null
    }
}