package com.example.lwp_lab01

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class Register : AppCompatActivity() {
    private var nameEditText: EditText? = null
    private var registerEmailEditText: EditText? = null
    private var registerPasswordEditText: EditText? = null
    private var confirmPasswordEditText: EditText? = null
    private var registerButton: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        // Inicializa las vistas
        nameEditText = findViewById(R.id.nameEditText)
        registerEmailEditText = findViewById(R.id.registerEmailEditText)
        registerPasswordEditText = findViewById(R.id.registerPasswordEditText)
        confirmPasswordEditText = findViewById(R.id.confirmPasswordEditText)
        registerButton = findViewById(R.id.registerButton)

        // Configura el listener del botón de registro
        //registerButton.setOnClickListener(View.OnClickListener { registerUser() })
    }

    private fun registerUser() {
        val name = nameEditText!!.text.toString().trim { it <= ' ' }
        val email = registerEmailEditText!!.text.toString().trim { it <= ' ' }
        val password = registerPasswordEditText!!.text.toString().trim { it <= ' ' }
        val confirmPassword = confirmPasswordEditText!!.text.toString().trim { it <= ' ' }
        if (name.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show()
            return
        }
        if (password != confirmPassword) {
            Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show()
            return
        }

        // Aquí puedes agregar la lógica de registro, como guardar el usuario en una base de datos
        // o usar algún servicio de autenticación.
        Toast.makeText(this, "Usuario registrado con éxito", Toast.LENGTH_SHORT).show()
    }
}