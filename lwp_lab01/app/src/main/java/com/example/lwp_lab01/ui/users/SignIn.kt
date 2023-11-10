package com.example.lwp_lab01.ui.users

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import com.example.lwp_lab01.R
import android.app.Activity
import android.content.Context
import androidx.appcompat.app.AlertDialog
import android.widget.Button
import com.google.firebase.auth.FirebaseAuth

class SignIn : AppCompatActivity() {
    var auth = FirebaseAuth.getInstance()

    private lateinit var btnAutenticar: Button
    private lateinit var txtEmail: EditText
    private lateinit var txtContra: EditText
    private lateinit var forgotPasswordText: TextView
    private lateinit var registerText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        btnAutenticar = findViewById(R.id.btnOK)
        txtEmail = findViewById(R.id.txtCorreo)
        txtContra = findViewById(R.id.txtPasswd)
        forgotPasswordText = findViewById(R.id.forgotPasswordText)
        registerText = findViewById(R.id.registerText)

        // Configuración del listener para "Olvidaste tu contraseña"
        forgotPasswordText.setOnClickListener {
            val forgotPasswordIntent = Intent(this@SignIn, ForgotPassword::class.java)
            startActivity(forgotPasswordIntent)
        }

        // Configuración del listener para "Regístrate"
        registerText.setOnClickListener {
            val registerIntent = Intent(this@SignIn, Register::class.java)
            startActivity(registerIntent)
        }
    }
    fun ejecutar(view: View) {
        if(txtEmail.text.isNotEmpty() && txtContra.text.isNotEmpty()){
            auth.signInWithEmailAndPassword(txtEmail.text.toString(), txtContra.text.toString()).addOnCompleteListener{
                if (it.isSuccessful){
                    //Register the data into the local storage
                    val prefe = this.getSharedPreferences("appData", Context.MODE_PRIVATE)

                    //Create editor object for write app data
                    val editor = prefe.edit()

                    //Set editor fields with the new values
                    editor.putString("email", txtEmail.text.toString())
                    editor.putString("contra", txtContra.text.toString())

                    //Write app data
                    editor.commit()

                    // call back to main activity
                    Intent().let {
                        setResult(Activity.RESULT_OK)
                        finish()
                    }
                }else{
                    showAlert("Error","Al autenticar el usuario")
                }
            }
        }else{
            showAlert("Error","El correo electrónico y contraseña no pueden estar vacíos")
        }
    }

    private fun showAlert(titu:String, mssg: String){
        val diagMessage = AlertDialog.Builder(this)
        diagMessage.setTitle(titu)
        diagMessage.setMessage(mssg)
        diagMessage.setPositiveButton("Aceptar", null)

        val diagVentana: AlertDialog = diagMessage.create()
        diagVentana.show()
    }
}