package com.example.lwp_lab01.ui.entities

import android.Manifest
import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
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
import com.example.lwp_lab01.R
import com.example.lwp_lab01.databinding.FragmentEntitiesBinding
import com.google.android.material.textfield.TextInputEditText
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
            val foto = imagenUri
            agregarFotoYComentario(foto, comentario)
            ocultarElementosParaAgregar()
        }


        binding.fabAgregar.setOnClickListener {
            mostrarElementosParaAgregar()
        }
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
            val item = lista[position]
            holder.tvComentario.text = item.second
            holder.ivFoto.setImageURI(item.first)
        }

        override fun getItemCount() = lista.size
    }

}



