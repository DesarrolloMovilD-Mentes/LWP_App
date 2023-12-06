package com.example.lwp_lab01.ui.users

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.lwp_lab01.R
import com.google.firebase.Firebase
import com.google.firebase.storage.storage
import android.net.Uri

private const val REQUEST_CODE_IMAGE_PICK = 0
class Register : AppCompatActivity() {
    private var nameEditText: EditText? = null
    private var gradeSchoolEditText: EditText? = null
    private var WhoIAmEditText: EditText? = null
    private var registerEmailEditText: EditText? = null
    private var registerPasswordEditText: EditText? = null
    private var confirmPasswordEditText: EditText? = null
    private var registerButton: Button? = null
    private lateinit var addPhotoButton: Button
    var curfile: Uri? = null
    var imageRef = Firebase.storage.reference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        // Inicializa las vistas
        nameEditText = findViewById(R.id.nameEditText)
        gradeSchoolEditText = findViewById(R.id.registerSchoolGrade)
        WhoIAmEditText = findViewById(R.id.registerWhoAmI)
        registerEmailEditText = findViewById(R.id.registerEmailEditText)
        registerPasswordEditText = findViewById(R.id.registerPasswordEditText)
        confirmPasswordEditText = findViewById(R.id.confirmPasswordEditText)
        registerButton = findViewById(R.id.registerButton)
        addPhotoButton = findViewById(R.id.uploadPhotoButton)


        // Configura el listener del botón de registro

        addPhotoButton.setOnClickListener{
            Intent(Intent.ACTION_GET_CONTENT).also {

                it.type = "image/*"
                startActivityForResult(it, REQUEST_CODE_IMAGE_PICK)

            }
        }
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



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_IMAGE_PICK){
            data?.data?.let{
                curfile = it
            }
        }
    }
}