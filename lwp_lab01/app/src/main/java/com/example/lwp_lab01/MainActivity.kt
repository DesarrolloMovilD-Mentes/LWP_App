package com.example.lwp_lab01

import NewEntityActivity
import android.Manifest
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.transition.Transition
import android.util.Log
import android.view.Menu
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.CustomTarget
import com.example.lwp_lab01.databinding.ActivityMainBinding
import com.example.lwp_lab01.ui.users.SignIn
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.io.File

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private var auth = FirebaseAuth.getInstance()
    var db = FirebaseFirestore.getInstance()
    var imgs = FirebaseStorage.getInstance()
    private var email: String? = null
    private var contra: String? = null

    private lateinit var ivFoto: ImageView
    private lateinit var btnTomarFoto: Button
    private lateinit var btnSeleccionarImagen: Button
    private var imagenUri: Uri? = null

    companion object {
        private const val TOMAR_FOTO = 100
        private const val SELEC_IMAGEN = 200
        private const val PERMISSION_REQUEST_CODE = 1002
    }

    private val CARPETA_RAIZ = "MisFotosApp/"
    private val CARPETAS_IMAGENES = "imagenes"
    private val RUTA_IMAGEN = CARPETA_RAIZ + CARPETAS_IMAGENES
    private var path: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarMain.toolbar)

        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow,
                R.id.nav_credits, R.id.nav_entities
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        binding.appBarMain.fab.setOnClickListener {
            val intent = Intent(this, NewEntityActivity::class.java)
            startActivity(intent)
        }

        val prefe = getSharedPreferences("appData", MODE_PRIVATE)
        email = prefe.getString("email", "")
        contra = prefe.getString("contra", "")

        if (email.isNullOrBlank()) {
            val signInIntent = Intent(this, SignIn::class.java)
            startActivity(signInIntent)
        } else {
            val uid = auth.uid
            if (uid.isNullOrEmpty()) {
                auth.signInWithEmailAndPassword(email!!, contra!!).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "Authentication Successful", Toast.LENGTH_SHORT).show()
                    } else {
                        val signInIntent = Intent(this, SignIn::class.java)
                        startActivity(signInIntent)
                    }
                }
            } else {
                obtenerDatos()
            }
        }


    }

    private fun tomarFoto() {
        var nombreImagen = ""
        val fileImagen = File(Environment.getExternalStorageDirectory(), RUTA_IMAGEN)
        var isCreada = fileImagen.exists()
        if (!isCreada) {
            isCreada = fileImagen.mkdirs()
        }
        if (isCreada) {
            nombreImagen = (System.currentTimeMillis() / 1000).toString() + ".jpg"
        }

        path = Environment.getExternalStorageDirectory().toString() + File.separator + RUTA_IMAGEN + File.separator + nombreImagen
        val imagen = File(path)
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            val authorities = "${applicationContext.packageName}.provider"
            val imageUri = FileProvider.getUriForFile(this, authorities, imagen)
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
        } else {
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(imagen))
        }
        startActivityForResult(intent, TOMAR_FOTO)
    }

    private fun seleccionarImagen() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
        startActivityForResult(intent, SELEC_IMAGEN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_OK && requestCode == SELEC_IMAGEN) {
            imagenUri = data?.data
            ivFoto.setImageURI(imagenUri)
        } else if (resultCode == RESULT_OK && requestCode == TOMAR_FOTO) {
            MediaScannerConnection.scanFile(this, arrayOf(path), null) { _, _ -> }
            val bitmap = BitmapFactory.decodeFile(path)
            ivFoto.setImageBitmap(bitmap)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
//        val userLogoMenuItem = menu.findItem(R.id.action_user_logo)
//        obtenerDatosUsuario { imageUrl ->
//            Glide.with(this)
//                .asDrawable()
//                .load(imageUrl)
//                .apply(RequestOptions.circleCropTransform())
//                .into(object : CustomTarget<Drawable>() {
//                    override fun onResourceReady(resource: Drawable, transition:Transition<in Drawable>?) {
//                        userLogoMenuItem.icon = resource
//                    }
//                    override fun onLoadCleared(placeholder: Drawable?) {
//                    }
//                })
//        }
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    // Función para obtener la URL de la imagen del usuario desde Firestore y configurarla como icono
    private fun obtenerDatosUsuario(callback: (imageUrl: String) -> Unit) {
        val uid = auth.uid
        db.collection("datosUsuarios")
            .whereEqualTo("idemp", uid)
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val document = task.result?.documents?.firstOrNull()
                    document?.let {
                        val imageUrl = it.data?.get("urlImage").toString()
                        callback(imageUrl)
                    }
                } else {
                    Log.w(ContentValues.TAG, "Error getting documents.", task.exception)
                }
            }
    }
    private fun obtenerDatos() {
        val uid = auth.uid
        db.collection("datosUsuarios")
            .whereEqualTo("idemp", uid)
            .get()
            .addOnCompleteListener { docc ->
                if (docc.isSuccessful) {
                    for (document in docc.result!!) {
                        val username = document.data["usuario"].toString()
                        Toast.makeText(this@MainActivity, "Bienvenido $username", Toast.LENGTH_LONG).show()
                    }
                } else {
                    Log.w(ContentValues.TAG, "Error getting documents.", docc.exception)
                }
            }
    }

    private fun checkAndRequestPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE), PERMISSION_REQUEST_CODE)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_CODE && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            // Permissions granted
        } else {
            Toast.makeText(this, "Permissions are required for this app.", Toast.LENGTH_SHORT).show()
        }
    }
}
