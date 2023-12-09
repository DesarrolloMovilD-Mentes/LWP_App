package com.example.lwp_lab01.ui.PDF_List

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class PdfManager(private val pdfList: MutableList<PdfModel>, private val adapter: com.example.lwp_lab01.ui.PDF_List.PdfAdapter) {

    fun cargarPDFs() {
        val db = Firebase.firestore
        db.collection("PDF's_modules").get().addOnSuccessListener { result ->
            for (document in result) {
                val nombre = document.getString("names") ?: ""
                val url = document.getString("url") ?: ""
                pdfList.add(PdfModel(nombre, url))
            }
            adapter.notifyDataSetChanged()
        }.addOnFailureListener { exception ->
            // Handle error
        }
    }
}
