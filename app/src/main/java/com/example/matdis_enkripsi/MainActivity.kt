package com.example.matdis_enkripsi

import android.graphics.Rect
import android.os.Bundle
import android.provider.Settings.Global
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import org.w3c.dom.Text

class MainActivity : AppCompatActivity() {
    private lateinit var inputPassword : EditText
    private lateinit var buttonEnkripsi : Button
    private lateinit var hasilAkhir : TextView
    private lateinit var buttonDekripsi: Button

    public fun initComponents(){
        inputPassword = findViewById(R.id.inputPassword)
        buttonEnkripsi = findViewById(R.id.encryptButton)
        hasilAkhir = findViewById(R.id.hasilAkhir)
        buttonDekripsi = findViewById(R.id.decryptButton)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.mainlayout)
        initComponents()

        buttonEnkripsi.setOnClickListener(){
            val input = inputPassword.text.toString()
            val passwordTerenkripsi = EncryptedPassword(input)
            hasilAkhir.text = passwordTerenkripsi
        }

        buttonDekripsi.setOnClickListener(){
            val input = inputPassword.text.toString()
            val passwordDekripsi = DecryptPassword(input)
            hasilAkhir.text = passwordDekripsi
        }
    }

    fun EncryptedPassword(inputPassword: String): String{
        val shiftPasswordToOriginal = inputPassword.map {
            if (it.isLetterOrDigit()){
                (it.code + 3).toChar()
            } else{
                it
            }
        }.joinToString("")
        return if (shiftPasswordToOriginal.isNotEmpty()){
            shiftPasswordToOriginal.last() + shiftPasswordToOriginal.dropLast(1)
        } else{
            ""
        }
    }

    fun DecryptPassword(inputPassword: String): String {
        if (inputPassword.isEmpty()) return ""

        val rotated = inputPassword.drop(1) + inputPassword.first()

        val shiftPassword = rotated.map {
            if (it.isLetterOrDigit()) {
                (it.code - 3).toChar()
            } else {
                it
            }
        }.joinToString("")

        return shiftPassword
    }

    fun HideKeyboard() {
        val view = currentFocus
        if (view != null) {
            val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
            view.clearFocus()
        }
    }
    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        if (ev.action == MotionEvent.ACTION_DOWN) {
            val v = currentFocus
            if (v is EditText) {
                val outRect = Rect()
                v.getGlobalVisibleRect(outRect)
                if (!outRect.contains(ev.rawX.toInt(), ev.rawY.toInt())) {
                    HideKeyboard()
                }
            }
        }
        return super.dispatchTouchEvent(ev)
    }
}