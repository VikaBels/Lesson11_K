package com.example.lesson11_k

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.lesson11_k.MainActivity.Companion.KEY_FILE_NAME
import com.example.lesson11_k.MainActivity.Companion.KEY_FOLDER_NAME
import com.example.lesson11_k.MainActivity.Companion.KEY_NUMBER_DOCUMENT
import com.example.lesson11_k.MainActivity.Companion.txtEmpty
import com.example.lesson11_k.MainActivity.Companion.txtNULL
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Paths


class WorkWithFileActivity : AppCompatActivity() {
    private var editTextFile: EditText? = null

    private var nameFile: String = "movies.txt"
    private var nameFileForEmptyLine: String = "Document"


    private fun findViewById() {
        editTextFile = findViewById(R.id.editTextFile)
    }

    private fun changeAppBar() {
        val actionbar = supportActionBar
        actionbar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onSupportNavigateUp(): Boolean {
        openMainActivityForResult()
        return true
    }


    private fun updateNumberEmptyDocument(firstValue: Int) {
        val sp = getSharedPreferences(txtEmpty, Context.MODE_PRIVATE)
        val editor = sp.edit()
        editor.putInt(KEY_NUMBER_DOCUMENT, firstValue)

        editor.apply()
    }

    private fun getNumberEmptyDocument(): Int {
        val sp = getSharedPreferences(txtEmpty, Context.MODE_PRIVATE)
        return sp.getInt(KEY_NUMBER_DOCUMENT, 0)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SdCardPath")
    private fun saveText() {
        var oStream: FileOutputStream? = null
        try {
            val text = editTextFile?.text.toString()
            val file = File("$filesDir$KEY_FOLDER_NAME/$nameFile")
            oStream = FileOutputStream(file)
            oStream.write(text.toByteArray())

            if (nameFile == txtNULL) {
                var firstLine = file.useLines { it.firstOrNull() }
                if (firstLine == txtEmpty) {

                    val filePath =
                        "$filesDir$KEY_FOLDER_NAME/$nameFileForEmptyLine${getNumberEmptyDocument()}"
                    val path = Paths.get(filePath)

                    if (Files.exists(path)) {
                        var numberEmptyDoc = getNumberEmptyDocument()
                        updateNumberEmptyDocument(++numberEmptyDoc)
                        firstLine = "$nameFileForEmptyLine${getNumberEmptyDocument()}"
                    } else {
                        firstLine = "$nameFileForEmptyLine${getNumberEmptyDocument()}"
                    }
                } else {
                    if (Files.exists(Paths.get("$filesDir$KEY_FOLDER_NAME/$firstLine"))) {
                        Toast.makeText(
                            this,
                            resources.getString(R.string.nameTakenOverwritten),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                val newFile = File("$filesDir$KEY_FOLDER_NAME/$firstLine")
                file.renameTo(newFile)
            }

        } catch (ex: IOException) {
            Toast.makeText(this, ex.message, Toast.LENGTH_SHORT).show()
        } finally {
            try {
                oStream?.close()
            } catch (ex: IOException) {
                Toast.makeText(this, ex.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    @SuppressLint("RestrictedApi", "SdCardPath")
    private fun openText() {
        var iStream: FileInputStream? = null
        try {
            val file = File("$filesDir$KEY_FOLDER_NAME/$nameFile")
            iStream = FileInputStream(file)
            val byte = ByteArray(iStream.available())
            iStream.read(byte)
            val t = String(byte)
            editTextFile?.setText(t, TextView.BufferType.EDITABLE)

        } catch (ex: IOException) {
            Toast.makeText(this, ex.message, Toast.LENGTH_SHORT).show()
        } finally {
            try {
                iStream?.close()
            } catch (ex: IOException) {
                Toast.makeText(this, ex.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun openMainActivityForResult() {
        val intent = Intent(this, MainActivity::class.java)
        mainActivityResultLauncher.launch(intent)
    }

    private var mainActivityResultLauncher = registerForActivityResult(
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
        setContentView(R.layout.activity_work_with_file)

        findViewById()

        changeAppBar()

        nameFile = intent.getStringExtra(KEY_FILE_NAME).toString()

        openText()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onPause() {
        saveText()
        super.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()

        editTextFile = null
    }
}