package com.example.lwp_lab01.ui.PDF_List

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.lwp_lab01.R

class PDF_section : AppCompatActivity(), com.example.lwp_lab01.ui.PDF_List.DownloadListener {
    private lateinit var recyclerView: RecyclerView
    private lateinit var pdfAdapter: com.example.lwp_lab01.ui.PDF_List.PdfAdapter
    private val pdfList = mutableListOf<com.example.lwp_lab01.ui.PDF_List.PdfModel>()
    private lateinit var pdfManager: com.example.lwp_lab01.ui.PDF_List.PdfManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pdf_section)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        pdfAdapter = com.example.lwp_lab01.ui.PDF_List.PdfAdapter(pdfList, this)
        recyclerView.adapter = pdfAdapter

        pdfManager = com.example.lwp_lab01.ui.PDF_List.PdfManager(pdfList, pdfAdapter)
        pdfManager.cargarPDFs()
    }

    override fun onDownloadStarted(pdfName: String) {
        mostrarMensaje("Descarga iniciada para $pdfName")
    }

    private fun mostrarMensaje(mensaje: String) {
        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show()
    }
}