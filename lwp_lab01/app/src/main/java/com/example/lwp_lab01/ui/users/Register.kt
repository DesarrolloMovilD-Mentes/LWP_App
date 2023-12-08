package com.example.lwp_lab01.ui.users

import android.app.Activity
import android.content.Context
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
import android.widget.ImageView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import java.util.Date

private const val REQUEST_CODE_IMAGE_PICK = 0
class Register : AppCompatActivity() {
    private var nameEditText: EditText? = null
    private var gradeSchoolEditText: EditText? = null
    private var WhoIAmEditText: EditText? = null
    private var registerEmailEditText: EditText? = null
    private var registerPasswordEditText: EditText? = null
    private var confirmPasswordEditText: EditText? = null
    private lateinit var registerButton: Button
    private lateinit var addPhotoButton: Button
    private lateinit var userImage: ImageView
    var curfile: Uri? = null
    var imageRef = Firebase.storage.reference
    var auth = FirebaseAuth.getInstance()
    var db = FirebaseFirestore.getInstance()
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
        userImage = findViewById(R.id.UserImage)


        // Configura el listener del botón de registro

        addPhotoButton.setOnClickListener{
            Intent(Intent.ACTION_GET_CONTENT).also {

                it.type = "image/*"
                startActivityForResult(it, REQUEST_CODE_IMAGE_PICK)

            }
        }
        registerButton.setOnClickListener {
            registerUser()
        }
    }

    private fun registerUser() {
        val name = nameEditText!!.text.toString().trim { it <= ' ' }
        val email = registerEmailEditText!!.text.toString().trim { it <= ' ' }
        val SchoolarGrade = gradeSchoolEditText!!.text.toString().trim { it <= ' ' }
        val whoIAm = WhoIAmEditText!!.text.toString().trim { it <= ' ' }
        val password = registerPasswordEditText!!.text.toString().trim { it <= ' ' }
        val confirmPassword = confirmPasswordEditText!!.text.toString().trim { it <= ' ' }
        if (name.isEmpty() || email.isEmpty() || SchoolarGrade.isEmpty()|| whoIAm.isEmpty()|| password.isEmpty() || confirmPassword.isEmpty()) {
            Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show()
            return
        }
        if (password != confirmPassword) {
            Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show()
            return
        }else{
                saveImageIntoStorage(name)
                auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        val dt: Date = Date()
                        val user = hashMapOf(
                            "idemp" to task.result?.user?.uid,
                            "usuario" to name,
                            "email" to email,
                            "gradoAcademico" to SchoolarGrade,
                            "descripcion" to whoIAm,
                            "urlImage" to curfile.toString(),
                            "ultAcceso" to dt.toString(),
                        )
                        db.collection("datosUsuarios")
                            .add(user)
                            .addOnSuccessListener { documentReference ->

                                //Register the data into the local storage
                                val prefe = this.getSharedPreferences("appData", Context.MODE_PRIVATE)

                                //Create editor object for write app data
                                val editor = prefe.edit()

                                //Set editor fields with the new values
                                editor.putString("email", email.toString())
                                editor.putString("contra", password.toString())
                                editor.putString("gradoAcademico", SchoolarGrade.toString())
                                editor.putString("descripcion", whoIAm.toString())
                                editor.putString("ultAcceso", dt.toString())
                                editor.putString("urlImage", curfile.toString())

                                //Write app data
                                editor.commit()

                                Toast.makeText(this,"Usuario registrado correctamente",Toast.LENGTH_SHORT).show()

                                Intent().let {
                                    setResult(Activity.RESULT_OK)
                                    finish()
                                }
                            }
                            .addOnFailureListener { e ->
                                Toast.makeText(this,"Error al registrar usuario",Toast.LENGTH_SHORT).show()
                            }
                    } else {
                        Toast.makeText(this,"Error al registrar usuario",Toast.LENGTH_SHORT).show()
                    }
                }
            }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_IMAGE_PICK){
            data?.data?.let{
                curfile = it
                userImage.setImageURI(it)
            }
        }
    }

    fun saveImageIntoStorage(filename: String) {
        try {
            curfile?.let {
                imageRef.child("userImages/$filename").putFile(it)
            }
        }catch (e:Exception){

        }
    }
}