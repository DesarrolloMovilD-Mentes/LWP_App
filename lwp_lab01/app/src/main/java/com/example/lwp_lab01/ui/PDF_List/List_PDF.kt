package com.example.lwp_lab01.ui.PDF_List

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.lwp_lab01.R
import com.google.firebase.Firebase
import com.google.firebase.database.Query
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.storage


class List_PDF : AppCompatActivity() {
    private lateinit var pdfList: RecyclerView
    private lateinit var progressVar: ProgressBar
    var query: Query? = null
    private val PDFs_Collections = Firebase.firestore.collection("PDF's_modules")

    @Override
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_pdf)
       // displayPDFS();
    }

//    fun displayPDFS(){
//
//        var pdfReference = pdfRef.child("documentosPDF");
//        pdfList = findViewById(R.id.recyclerView)
//        pdfList.setHasFixedSize(true)
//        pdfList.setLayoutManager(LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false))
//        progressVar = findViewById(R.id.progress_bar)
//        progressVar.setVisibility(View.VISIBLE)
//        query = pdfReference.
//
//
//    }


}