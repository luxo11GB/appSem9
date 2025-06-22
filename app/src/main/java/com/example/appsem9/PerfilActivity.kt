package com.example.appsem9

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar

class PerfilActivity : AppCompatActivity() {

    private lateinit var sharedPreferencesHelper: SharedPreferencesHelper
    private lateinit var editTextNombre: EditText
    private lateinit var editTextEdad: EditText
    private lateinit var editTextEmail: EditText
    private lateinit var buttonGuardarPerfil: Button
    private lateinit var buttonCargarPerfil: Button
    private lateinit var textViewPerfil: TextView
    private lateinit var textViewtitulo: TextView
    private lateinit var switchThemePerfil: Switch
    private lateinit var scrollViewPerfil: ScrollView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_perfil)

        sharedPreferencesHelper = SharedPreferencesHelper(this)
        initViews()

        // Aplicar tema antes de listeners
        val isDarkMode = sharedPreferencesHelper.getBoolean(SharedPreferencesHelper.KEY_THEME_MODE, false)
        applyTheme(isDarkMode)

        setupListeners()

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigation)
        bottomNavigationView.selectedItemId = R.id.nav_profile

        bottomNavigationView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.nav_home -> {
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                    true
                }
                R.id.nav_profile -> {
                    // Ya estás en PerfilActivity
                    true
                }
                else -> false
            }
        }

    }

    private fun initViews() {
        scrollViewPerfil = findViewById(R.id.scrollViewPerfil)
        editTextNombre = findViewById(R.id.editTextNombre)
        editTextEdad = findViewById(R.id.editTextEdad)
        editTextEmail = findViewById(R.id.editTextEmail)
        buttonGuardarPerfil = findViewById(R.id.buttonGuardarPerfil)
        buttonCargarPerfil = findViewById(R.id.buttonCargarPerfil)
        textViewPerfil = findViewById(R.id.textViewPerfil)
        textViewtitulo = findViewById(R.id.textViewtitulo)
        switchThemePerfil = findViewById(R.id.switchThemePerfil)
    }

    private fun setupListeners() {
        buttonGuardarPerfil.setOnClickListener {
            val nombre = editTextNombre.text.toString().trim()
            val edad = editTextEdad.text.toString().trim()
            val email = editTextEmail.text.toString().trim()

            if (nombre.isEmpty() || edad.isEmpty() || email.isEmpty()) {
                val rootView = findViewById<View>(android.R.id.content)
                Snackbar.make(rootView, "Completa todos los campos", Snackbar.LENGTH_SHORT).show()

                return@setOnClickListener
            }

            sharedPreferencesHelper.saveString("perfil_nombre", nombre)
            sharedPreferencesHelper.saveInt("perfil_edad", edad.toInt())
            sharedPreferencesHelper.saveString("perfil_email", email)

            val rootView = findViewById<View>(android.R.id.content)
            Snackbar.make(rootView, "Perfil guardado", Snackbar.LENGTH_SHORT).show()

            clearInputs()

        }

        buttonCargarPerfil.setOnClickListener {
            val nombre = sharedPreferencesHelper.getString("perfil_nombre", "Sin nombre")
            val edad = sharedPreferencesHelper.getInt("perfil_edad", 0)
            val email = sharedPreferencesHelper.getString("perfil_email", "Sin email")

            val perfil = "Nombre: $nombre\nEdad: $edad\nEmail: $email"
            textViewPerfil.setTextColor(Color.BLACK)
            textViewPerfil.text = perfil
        }

        switchThemePerfil.setOnCheckedChangeListener { _, isChecked ->
            sharedPreferencesHelper.saveBoolean(SharedPreferencesHelper.KEY_THEME_MODE, isChecked)
            applyTheme(isChecked)
        }
    }

    private fun clearInputs() {
        editTextNombre.setText("")
        editTextEdad.setText("")
        editTextEmail.setText("")
    }

    private fun applyTheme(isDarkMode: Boolean) {
        if (isDarkMode) {
            scrollViewPerfil.setBackgroundColor(ContextCompat.getColor(this, android.R.color.black))
            editTextNombre.setTextColor(ContextCompat.getColor(this, android.R.color.white))
            editTextEdad.setTextColor(ContextCompat.getColor(this, android.R.color.white))
            editTextEmail.setTextColor(ContextCompat.getColor(this, android.R.color.white))

            editTextNombre.setHintTextColor(ContextCompat.getColor(this, android.R.color.white))
            editTextEdad.setHintTextColor(ContextCompat.getColor(this, android.R.color.white))
            editTextEmail.setHintTextColor(ContextCompat.getColor(this, android.R.color.white))

            editTextNombre.setHintTextColor(resources.getColor(android.R.color.white, theme))
            editTextEdad.setHintTextColor(resources.getColor(android.R.color.white, theme))
            editTextEmail.setHintTextColor(resources.getColor(android.R.color.white, theme))
            textViewPerfil.setTextColor(ContextCompat.getColor(this, android.R.color.white))
            switchThemePerfil.setTextColor(ContextCompat.getColor(this, android.R.color.white))
            textViewtitulo.setTextColor(ContextCompat.getColor(this, android.R.color.white))
            switchThemePerfil.text = "Modo Oscuro"
        } else {
            scrollViewPerfil.setBackgroundColor(ContextCompat.getColor(this, android.R.color.white))
            editTextNombre.setTextColor(ContextCompat.getColor(this, android.R.color.black))
            editTextEdad.setTextColor(ContextCompat.getColor(this, android.R.color.black))
            editTextEmail.setTextColor(ContextCompat.getColor(this, android.R.color.black))

            editTextNombre.setHintTextColor(ContextCompat.getColor(this, android.R.color.darker_gray))
            editTextEdad.setHintTextColor(ContextCompat.getColor(this, android.R.color.darker_gray))
            editTextEmail.setHintTextColor(ContextCompat.getColor(this, android.R.color.darker_gray))

            textViewPerfil.setTextColor(ContextCompat.getColor(this, android.R.color.black))
            switchThemePerfil.setTextColor(ContextCompat.getColor(this, android.R.color.black))
            textViewtitulo.setTextColor(ContextCompat.getColor(this, android.R.color.black))
            switchThemePerfil.text = "Modo Claro"
        }

        switchThemePerfil.isChecked = isDarkMode
    }
}