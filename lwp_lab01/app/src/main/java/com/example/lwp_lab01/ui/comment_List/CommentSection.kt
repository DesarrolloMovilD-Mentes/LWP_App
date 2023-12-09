package com.example.lwp_lab01.ui.comment_List

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.lwp_lab01.R
import com.example.lwp_lab01.entities.cls_comments
import com.google.firebase.Firebase
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.util.Date


class CommentSection : AppCompatActivity() {
    private lateinit var btnAddComment: Button
    private lateinit var imageUserPost: ImageView
    private lateinit var imageUserPostComment: ImageView
    private lateinit var textTitlePDF: TextView
    private lateinit var textDate: TextView
    private lateinit var textInfoPdf: TextView
    private lateinit var commentEditText: EditText
    private lateinit var commentList: RecyclerView
    var db = FirebaseFirestore.getInstance()
//  var auth = FirebaseAuth.getInstance()
    private val commentsColectionRef = Firebase.firestore.collection("comments")



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_commensection)
        btnAddComment = findViewById(R.id.post_detail_add_comment_btn)
        imageUserPostComment = findViewById(R.id.post_detail_currentuser_img)
        textTitlePDF = findViewById(R.id.post_detail_title)
        commentEditText = findViewById(R.id.post_detail_comment)

        val nombrePdf = intent.getStringExtra("PDF Name")
        textTitlePDF.setText(nombrePdf)

        btnAddComment.setOnClickListener {
           onAddCommentButtonClick()
        }
        obtenerDatos()
    }


    //Method to get the comment and send it to the collection
    private fun getComment(date:Date): cls_comments{
        var dt = date.toString()
        val pdfName = textTitlePDF.text.toString()
        val comment =  commentEditText.text.toString()
        return cls_comments(pdfName, comment,dt)
    }
    //Function to retrieve a pdf from firebase

    //Inserting comments into the PDF
    private fun onAddCommentButtonClick()  = CoroutineScope(Dispatchers.IO).launch {
        var idComm = 1
        val dt: Date = Date()
        try {
            val Comment = getComment(dt)
            commentsColectionRef.add(Comment).await()
            withContext(Dispatchers.Main) {
                Toast.makeText(this@CommentSection, "Comment Added", Toast.LENGTH_LONG).show()
                commentEditText.setText("")
            }

        } catch (e: Exception){
            withContext(Dispatchers.Main) {
                Toast.makeText(this@CommentSection, e.message, Toast.LENGTH_LONG).show()
            }
        }
        idComm += 1
    }


    private fun obtenerDatos() {
        //Toast.makeText(this,"Esperando hacer algo importante", Toast.LENGTH_LONG).show()
        var coleccion: ArrayList<cls_comments?> = ArrayList()
        var listaView: ListView = findViewById(R.id.rv_comment)
        db.collection("comments")
            .whereEqualTo("pdfName",textTitlePDF.text)
            .orderBy("date")
            .get()
            .addOnCompleteListener { docc ->
                if (docc.isSuccessful) {
                    for (document in docc.result!!) {
                        Log.d(TAG, document.id + " => " + document.data)
                        var datos: cls_comments = cls_comments(document.data["pdfName"].toString(),
                            document.data["comment"].toString(), document.data["date"].toString())
                        coleccion.add(datos)
                    }
                    var adapter: CommentAdapter = CommentAdapter(this, coleccion)
                    listaView.adapter = adapter
                } else {
                    Log.w(TAG, "Error getting documents.", docc.exception)
                }
            }
    }
}