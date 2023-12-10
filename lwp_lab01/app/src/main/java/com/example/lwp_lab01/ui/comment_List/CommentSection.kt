package com.example.lwp_lab01.ui.comment_List

import android.content.ContentValues
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
import androidx.core.net.toUri
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.lwp_lab01.R
import com.example.lwp_lab01.entities.cls_comments
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject
import com.google.firebase.storage.FirebaseStorage
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
    var auth = FirebaseAuth.getInstance()
    var imgs = FirebaseStorage.getInstance()
    private val commentsColectionRef = Firebase.firestore.collection("comments")
    var usernameEstablish = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_commensection)
        btnAddComment = findViewById(R.id.post_detail_add_comment_btn)
        imageUserPostComment = findViewById(R.id.post_detail_currentuser_img)
        textTitlePDF = findViewById(R.id.post_detail_title)
        commentEditText = findViewById(R.id.post_detail_comment)
        val nombrePdf = intent.getStringExtra("PDF Name")
        textTitlePDF.setText(nombrePdf)

//        obtenerImagenUsuario { imageUrl ->
//            var image = imageUrl.toUri()
//            imageUserPostComment.setImageURI(image)
//        }

        btnAddComment.setOnClickListener {
           onAddCommentButtonClick()
        }
        obtenerComentarios()
    }


    //Method to get the comment and send it to the collection
    private fun getComment(date:Date, userN: String): cls_comments{
        var dt = date.toString()
        val pdfName = textTitlePDF.text.toString()
        val comment =  commentEditText.text.toString()
        return cls_comments(pdfName, comment,dt, userN)
    }
    //Function to retrieve a pdf from firebase

    //Inserting comments into the PDF
    private fun onAddCommentButtonClick()  = CoroutineScope(Dispatchers.IO).launch {
        val dt: Date = Date()
        getName()
        try {
            val Comment = getComment(dt, usernameEstablish)
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
    }


    private fun obtenerComentarios() {
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
                            document.data["comment"].toString(), document.data["date"].toString(),
                            document.data["userName"].toString())
                        coleccion.add(datos)
                    }
                    var adapter: CommentAdapter = CommentAdapter(this, coleccion)
                    listaView.adapter = adapter
                } else {
                    Log.w(TAG, "Error getting documents.", docc.exception)
                }
            }
    }
    private fun obtenerImagenUsuario(callback: (imageUrl: String) -> Unit) {
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
    private fun getName(){
        val uid = auth.uid
        db.collection("datosUsuarios")
            .whereEqualTo("idemp", uid)
            .get()
            .addOnCompleteListener { docc ->
                if (docc.isSuccessful) {
                    for (document in docc.result!!) {
                        val username = document.data["usuario"].toString()
                        usernameEstablish= username
                    }
                } else {
                    Log.w(ContentValues.TAG, "Error getting documents.", docc.exception)
                }
            }
    }
}