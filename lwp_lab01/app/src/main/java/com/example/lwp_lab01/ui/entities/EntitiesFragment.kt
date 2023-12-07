package com.example.lwp_lab01.ui.entities

import com.google.firebase.firestore.Query

import android.Manifest
import android.app.Activity
import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.lwp_lab01.R
import com.example.lwp_lab01.databinding.FragmentEntitiesBinding
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.storage.FirebaseStorage
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream

class EntitiesFragment : Fragment() {


    companion object {
        fun newInstance() = EntitiesFragment()
        private const val TOMAR_FOTO = 100
        private const val SELEC_IMAGEN = 200
    }

    private var _binding: FragmentEntitiesBinding? = null
    private val binding get() = _binding!!

    private lateinit var ivFoto: ImageView
    private lateinit var btnTomarFoto: Button
    private lateinit var btnSeleccionarImagen: Button
    private var imagenUri: Uri? = null

    private lateinit var etComentario: TextInputEditText
    private lateinit var btnAgregar: Button
    private var listaFotosComentarios = mutableListOf<Pair<Uri?, String>>()


    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: FotoComentarioAdapter

    // En onCreateView
    val db = FirebaseFirestore.getInstance()
    val imagenId = "id_de_la_imagen"
    val comentario = "Este es un comentario de ejemplo" // Reemplaza con el comentario real

    private var contadorComentarios = 1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEntitiesBinding.inflate(inflater, container, false)

        ivFoto = binding.ivFoto
        btnTomarFoto = binding.btnTomarFoto
        btnSeleccionarImagen = binding.btnSeleccionarImagen
        etComentario = binding.etComentario
        btnAgregar = binding.btnAgregar

        recyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(context)
        adapter = FotoComentarioAdapter(listaFotosComentarios)
        recyclerView.adapter = adapter






        btnTomarFoto.setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.CAMERA
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                tomarFoto()
            } else {
                requestPermissions(arrayOf(Manifest.permission.CAMERA), TOMAR_FOTO)
            }
        }

        btnSeleccionarImagen.setOnClickListener {
            seleccionarImagen()
        }

        btnAgregar.setOnClickListener {
            val comentario = etComentario.text.toString()

            if (imagenUri != null && comentario.isNotBlank()) {
                // Sube la imagen a Firebase Storage
                subirImagenAFirebaseStorage(imagenUri!!) { imageUrl ->
                    if (imageUrl != null) {
                        // Una vez que la imagen se haya subido con éxito, guarda el comentario y la URL de la imagen en Firebase Firestore
                        guardarComentarioEnFirestore(comentario, imageUrl)

                        // Incrementa el contador de comentarios
                        contadorComentarios++

                        // Limpia la vista de imagen y el campo de texto
                        ivFoto.setImageResource(0)
                        etComentario.text?.clear()
                        imagenUri = null
                        ocultarElementosParaAgregar()
                    } else {
                        Toast.makeText(context, "Error al subir la imagen", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(context, "Debes seleccionar una imagen y escribir un comentario", Toast.LENGTH_SHORT).show()
            }
        }



        binding.fabAgregar.setOnClickListener {
            mostrarElementosParaAgregar()
        }
        cargarImagenesDeFirestore()
        return binding.root
    }

    private fun tomarFoto() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, TOMAR_FOTO)
    }

    private fun seleccionarImagen() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
        startActivityForResult(intent, SELEC_IMAGEN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                TOMAR_FOTO -> {
                    val imageBitmap = data?.extras?.get("data") as? Bitmap
                    binding.ivFoto.setImageBitmap(imageBitmap)
                    imagenUri = guardarImagenEnAlmacenamiento(imageBitmap!!)
                }

                SELEC_IMAGEN -> {
                    imagenUri = data?.data
                    binding.ivFoto.setImageURI(imagenUri)
                }
            }
        }
    }
    private fun guardarImagenEnAlmacenamiento(bitmap: Bitmap): Uri? {
        val filename = "IMG_${System.currentTimeMillis()}.jpg"
        var fos: OutputStream? = null
        var imageUri: Uri? = null

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            context?.contentResolver?.also { resolver ->
                val contentValues = ContentValues().apply {
                    put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
                    put(MediaStore.MediaColumns.MIME_TYPE, "image/jpg")
                    put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
                }

                imageUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
                fos = imageUri?.let { resolver.openOutputStream(it) }
            }
        } else {
            val imagesDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
            val image = File(imagesDir, filename)
            imageUri = Uri.fromFile(image)
            fos = FileOutputStream(image)
        }

        fos?.use {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, it)
        }

        return imageUri
    }

    private fun mostrarElementosParaAgregar() {
        binding.ivFoto.visibility = View.VISIBLE
        binding.btnTomarFoto.visibility = View.VISIBLE
        binding.btnSeleccionarImagen.visibility = View.VISIBLE
        binding.textInputLayout.visibility = View.VISIBLE
        binding.btnAgregar.visibility = View.VISIBLE
    }

    private fun ocultarElementosParaAgregar() {
        binding.ivFoto.visibility = View.GONE
        binding.btnTomarFoto.visibility = View.GONE
        binding.btnSeleccionarImagen.visibility = View.GONE
        binding.textInputLayout.visibility = View.GONE
        binding.btnAgregar.visibility = View.GONE
    }

    private fun     agregarFotoYComentario(foto: Uri?, comentario: String) {
        if (foto != null && comentario.isNotBlank()) {
            listaFotosComentarios.add(Pair(foto, comentario))
            adapter.notifyDataSetChanged()

            // Limpiar la vista de imagen y el campo de texto
            binding.ivFoto.setImageResource(0) // o setImageBitmap(null)
            binding.etComentario.text?.clear()
            imagenUri = null
        } else {
            Toast.makeText(
                context,
                "Debes tomar una foto y escribir un comentario",
                Toast.LENGTH_SHORT
            ).show()
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    // Agrega esta variable en la parte superior de tu Fragment
    private val storageRef = FirebaseStorage.getInstance().reference

    // Luego, en tu función subirImagenAFirebaseStorage, puedes usar esta referencia
    private fun subirImagenAFirebaseStorage(imageUri: Uri, callback: (String?) -> Unit) {
        val referenciaImagen = storageRef.child("imagenes/${System.currentTimeMillis()}.jpg")

        referenciaImagen.putFile(imageUri)
            .addOnSuccessListener { taskSnapshot ->
                referenciaImagen.downloadUrl.addOnSuccessListener { uri ->
                    callback(uri.toString())
                }.addOnFailureListener {
                    callback(null)
                }
            }
            .addOnFailureListener {
                callback(null)
            }
    }


    private fun cargarImagenesDeFirestore() {
        val db = FirebaseFirestore.getInstance()
        val comentariosRef = db.collection("datosImg")

        comentariosRef.orderBy("posic", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { querySnapshot ->
                listaFotosComentarios.clear() // Limpia la lista actual antes de añadir nuevos elementos
                for (document in querySnapshot) {
                    val urlImagen = document.getString("url") ?: ""
                    val comentario = document.getString("descrip") ?: ""
                    if (urlImagen.isNotBlank()) {
                        listaFotosComentarios.add(Pair(Uri.parse(urlImagen), comentario))
                    }
                }
                adapter.notifyDataSetChanged() // Notifica al adaptador que los datos han cambiado
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error al cargar las imágenes: ", e)
                Toast.makeText(context, "Error al cargar las imágenes", Toast.LENGTH_SHORT).show()
            }
    }



    private fun guardarComentarioEnFirestore(comentario: String, imageUrl: String) {
        val db = FirebaseFirestore.getInstance()
        val comentariosRef = db.collection("datosImg")
        val posicCounterRef = db.collection("contadores").document("posicCounter")

        db.runTransaction { transaction ->
            val posicCounterSnapshot = transaction.get(posicCounterRef)
            val ultimoPosic = posicCounterSnapshot.getLong("posic")
            Log.d("Firestore", "El último posic recuperado es: $ultimoPosic")

            // Si el documento no existe o no tiene el campo 'posic', inicializa a 1
            val nuevoPosic = if (ultimoPosic != null) {
                ultimoPosic + 1
            } else {
                1L // Comienza en 1 si no existe el campo 'posic'
            }

            Log.d("Firestore", "El nuevo posic será: $nuevoPosic")

            val nuevoComentario = hashMapOf(
                "descrip" to comentario,
                "posic" to nuevoPosic,
                "url" to imageUrl
            )

            val newComentarioRef = comentariosRef.document()
            transaction.set(newComentarioRef, nuevoComentario)
        }.addOnSuccessListener {
            Toast.makeText(context, "Entidad guardada con éxito mediante transacción", Toast.LENGTH_SHORT).show()
        }.addOnFailureListener { exception ->
            Toast.makeText(context, "Error al guardar la entidad mediante transacción: ${exception.message}", Toast.LENGTH_SHORT).show()
        }
    }












    class FotoComentarioAdapter(private val lista: List<Pair<Uri?, String>>) :
        RecyclerView.Adapter<FotoComentarioAdapter.ViewHolder>() {

        class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val ivFoto: ImageView = view.findViewById(R.id.ivFotoItem)
            val tvComentario: TextView = view.findViewById(R.id.tvComentario)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_foto_comentario, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val (uri, comentario) = lista[position]
            holder.tvComentario.text = comentario
            if (uri != null) {
                Glide.with(holder.ivFoto.context).load(uri).into(holder.ivFoto)
            }
        }

        override fun getItemCount() = lista.size
    }



}