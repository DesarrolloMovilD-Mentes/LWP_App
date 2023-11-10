package com.example.lwp_lab01

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast

class SignIn : AppCompatActivity() {
    // Datos quemados
    private val USUARIO_FIJO = "usuario"
    private val CONTRASENA_FIJA = "contraseña123"

    private lateinit var txtID: EditText
    private lateinit var txtPasswd: EditText
    private lateinit var forgotPasswordText: TextView
    private lateinit var registerText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        txtID = findViewById(R.id.txtID)
        txtPasswd = findViewById(R.id.txtPasswd)
        forgotPasswordText = findViewById(R.id.forgotPasswordText)
        registerText = findViewById(R.id.registerText)

        // Configuración del listener para "Olvidaste tu contraseña"
        forgotPasswordText.setOnClickListener {
            val forgotPasswordIntent = Intent(this@SignIn, Register::class.java)
            startActivity(forgotPasswordIntent)
        }

        // Configuración del listener para "Regístrate"
        registerText.setOnClickListener {
            val registerIntent = Intent(this@SignIn, ForgotPassword::class.java)
            startActivity(registerIntent)
        }
    }

    fun ejecutar(view: View) {
        // Recoger los datos introducidos
        val usuarioIntroducido = txtID.text.toString()
        val contrasenaIntroducida = txtPasswd.text.toString()

        // Validar los datos
        if (usuarioIntroducido == USUARIO_FIJO && contrasenaIntroducida == CONTRASENA_FIJA) {
            Toast.makeText(this, "Ingresado correctamente", Toast.LENGTH_SHORT).show()

            // Opcional: Puedes dirigir al usuario a otra actividad aquí si lo deseas
            // val intent = Intent(this, OtraActividad::class.java)
            // startActivity(intent)
        } else {
            if (usuarioIntroducido != USUARIO_FIJO) {
                Toast.makeText(this, "Usuario incorrecto", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Contraseña incorrecta", Toast.LENGTH_SHORT).show()
            }
        }
    }
}