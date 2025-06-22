package com.example.appsem9

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {

    private lateinit var sharedPreferencesHelper: SharedPreferencesHelper
    private lateinit var editTextUsername: EditText
    private lateinit var buttonSave: Button
    private lateinit var buttonLoad: Button
    private lateinit var buttonClear: Button
    private lateinit var textViewResult: TextView
    private lateinit var switchTheme: Switch
    private lateinit var rootLayout: ConstraintLayout
    private lateinit var textViewOpenCount: TextView
    private lateinit var textViewtitulo: TextView
    private lateinit var buttonResetCounter: Button




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }



        sharedPreferencesHelper = SharedPreferencesHelper(this)

        initViews()

        // Aplicar el tema después de cargar las vistas
        val isDarkMode = sharedPreferencesHelper.getBoolean(SharedPreferencesHelper.KEY_THEME_MODE, false)
        applyTheme(isDarkMode)

        setupListeners()
        incrementAppOpenCounter()

        checkFirstTime()

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigation)
        bottomNavigationView.selectedItemId = R.id.nav_home

        bottomNavigationView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.nav_home -> {
                    // Ya estás en MainActivity, no haces nada
                    true
                }
                R.id.nav_profile -> {
                    startActivity(Intent(this, PerfilActivity::class.java))
                    finish() // opcional: cierra esta actividad
                    true
                }
                else -> false
            }
        }

    }

    private fun initViews() {
        rootLayout = findViewById(R.id.main)
        editTextUsername = findViewById(R.id.editTextUsername)
        buttonSave = findViewById(R.id.buttonSave)
        buttonLoad = findViewById(R.id.buttonLoad)
        buttonClear = findViewById(R.id.buttonClear)
        textViewResult = findViewById(R.id.textViewResult)
        switchTheme = findViewById(R.id.switchTheme)
        textViewOpenCount = findViewById(R.id.textViewOpenCount)
        textViewtitulo = findViewById(R.id.textViewtitulo)
        buttonResetCounter = findViewById(R.id.buttonResetCounter)


    }

    private fun setupListeners() {
        buttonSave.setOnClickListener { saveData() }
        buttonLoad.setOnClickListener { loadData() }
        buttonClear.setOnClickListener { clearAllData() }

        switchTheme.setOnCheckedChangeListener { _, isChecked ->
            sharedPreferencesHelper.saveBoolean(SharedPreferencesHelper.KEY_THEME_MODE, isChecked)
            applyTheme(isChecked)
        }


        buttonResetCounter.setOnClickListener {
            sharedPreferencesHelper.saveInt("app_open_count", 0)
            textViewOpenCount.text = "Veces que se abrió la app: 0"
            val rootView = findViewById<View>(android.R.id.content)
            Snackbar.make(rootView, "Contador reiniciado", Snackbar.LENGTH_SHORT).show()

        }
    }

    private fun saveData() {
        val username = editTextUsername.text.toString().trim()

        if (username.isEmpty()) {
            val rootView = findViewById<View>(android.R.id.content)
            Snackbar.make(rootView, "Por favor ingresa un nombre", Snackbar.LENGTH_SHORT).show()

            return
        }

        sharedPreferencesHelper.saveString(SharedPreferencesHelper.KEY_USERNAME, username)
        sharedPreferencesHelper.saveBoolean(SharedPreferencesHelper.KEY_IS_FIRST_TIME, false)
        sharedPreferencesHelper.saveInt(SharedPreferencesHelper.KEY_USER_ID, (1000..9999).random())

        val rootView = findViewById<View>(android.R.id.content)
        Snackbar.make(rootView, "Datos guardados exitosamente", Snackbar.LENGTH_SHORT).show()

        editTextUsername.setText("")
    }

    private fun loadData() {

        textViewResult.setTextColor(Color.BLACK)
        val username = sharedPreferencesHelper.getString(SharedPreferencesHelper.KEY_USERNAME, "Sin nombre")
        val isFirstTime = sharedPreferencesHelper.getBoolean(SharedPreferencesHelper.KEY_IS_FIRST_TIME, true)
        val userId = sharedPreferencesHelper.getInt(SharedPreferencesHelper.KEY_USER_ID, 0)

        val result = "Usuario: $username\nID: $userId\nPrimera vez: ${if (isFirstTime) "Sí" else "No"}"
        textViewResult.text = result
    }

    private fun clearAllData() {
        sharedPreferencesHelper.clearAll()
        textViewResult.text = ""
        editTextUsername.setText("")
        switchTheme.isChecked = false // Reinicia a modo claro
        applyTheme(false)
        val rootView = findViewById<View>(android.R.id.content)
        Snackbar.make(rootView, "Todas las preferencias han sido eliminadas", Snackbar.LENGTH_SHORT).show()

    }

    private fun checkFirstTime() {
        val isFirstTime = sharedPreferencesHelper.getBoolean(SharedPreferencesHelper.KEY_IS_FIRST_TIME, true)
        if (isFirstTime) {
            val rootView = findViewById<View>(android.R.id.content)
            Snackbar.make(rootView, "¡Bienvenido por primera vez!", Snackbar.LENGTH_LONG).show()

        }
    }

    private fun applyTheme(isDarkMode: Boolean) {
        if (isDarkMode) {
            rootLayout.setBackgroundColor(resources.getColor(android.R.color.black, theme))
            editTextUsername.setTextColor(resources.getColor(android.R.color.white, theme))
            editTextUsername.setHintTextColor(resources.getColor(android.R.color.white, theme))
            textViewResult.setTextColor(resources.getColor(android.R.color.black, theme))
            switchTheme.setTextColor(resources.getColor(android.R.color.white, theme))
            textViewOpenCount.setTextColor(resources.getColor(android.R.color.white, theme))
            textViewtitulo.setTextColor(resources.getColor(android.R.color.white, theme))

            switchTheme.text = "Modo Oscuro"
        } else {
            rootLayout.setBackgroundColor(resources.getColor(android.R.color.white, theme))
            editTextUsername.setTextColor(resources.getColor(android.R.color.black, theme))
            textViewResult.setTextColor(resources.getColor(android.R.color.black, theme))
            switchTheme.setTextColor(resources.getColor(android.R.color.black, theme))
            textViewOpenCount.setTextColor(resources.getColor(android.R.color.black, theme))
            textViewtitulo.setTextColor(ContextCompat.getColor(this, android.R.color.black))
            switchTheme.text = "Modo Claro"
        }

        switchTheme.isChecked = isDarkMode
    }

    private fun incrementAppOpenCounter() {
        val count = sharedPreferencesHelper.getInt("app_open_count", 0) + 1
        sharedPreferencesHelper.saveInt("app_open_count", count)
        textViewOpenCount.text = "Veces que se abrió la app: $count"
    }

}