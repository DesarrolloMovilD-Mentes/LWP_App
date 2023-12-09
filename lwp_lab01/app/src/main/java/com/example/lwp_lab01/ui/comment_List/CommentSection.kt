package com.example.lwp_lab01.ui.comment_List

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.lwp_lab01.R
import com.example.lwp_lab01.adapters.commenAdapter
import com.example.lwp_lab01.entities.cls_PDFs
import com.example.lwp_lab01.entities.cls_comments
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext


class CommentSection : AppCompatActivity() {
    private lateinit var btnAddComment: Button
    private lateinit var imageUserPost: ImageView
    private lateinit var imageUserPostComment: ImageView
    private lateinit var textTitlePDF: TextView
    private lateinit var textDate: TextView
    private lateinit var textInfoPdf: TextView
    private lateinit var commentEditText: EditText
    private lateinit var commentList: RecyclerView
//    var db = FirebaseFirestore.getInstance()
//    var auth = FirebaseAuth.getInstance()
    private val pdfColectionRef = Firebase.firestore.collection("PDF's_modules")
    private val commentsColectionRef = Firebase.firestore.collection("comments")



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_commensection)
        btnAddComment = findViewById(R.id.post_detail_add_comment_btn)
        imageUserPostComment = findViewById(R.id.post_detail_currentuser_img)
        textTitlePDF = findViewById(R.id.post_detail_title)
        commentEditText = findViewById(R.id.post_detail_comment)
        commentList = findViewById(R.id.rv_comment)


        commentList.layoutManager = LinearLayoutManager(this) // o GridLayoutManager si prefieres

// Crear una instancia de tu adaptador con los datos iniciales
        val commentsAdapter = commenAdapter(this, arrayListOf())

// Establecer el adaptador en tu RecyclerView
        commentList.adapter = commentsAdapter
        val nombrePdf = intent.getStringExtra("PDF Name")
        textTitlePDF.setText(nombrePdf)
        subscribeToRealtimeUpdates()

        btnAddComment.setOnClickListener {
           onAddCommentButtonClick()
        }
    }


    //Method to get the comment and send it to the collection
    private fun getComment(): cls_comments{
        val pdfName = textTitlePDF.text.toString()
        val comment =  commentEditText.text.toString()
        return cls_comments(pdfName, comment)
    }
    //Function to retrieve a pdf from firebase

    //Inserting comments into the PDF
    private fun onAddCommentButtonClick()  = CoroutineScope(Dispatchers.IO).launch {
        try {
            val Comment = getComment()
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

    //RealTimeUpdates for comments
    private fun subscribeToRealtimeUpdates() {
//        setupRecyclerView()
        commentsColectionRef.addSnapshotListener { querySnapshot, firebaseFirestoreException ->
            firebaseFirestoreException?.let {
                Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
                return@addSnapshotListener
            }
            querySnapshot?.let { snapshot ->
                for (documentChange in snapshot.documentChanges) {
                    when (documentChange.type) {
                        DocumentChange.Type.ADDED -> {
                            val newComment = documentChange.document.toObject<cls_comments>()
                            //adapter.addComment(newComment)
                        }
                        // Aquí puedes manejar otros tipos de cambios si es necesario
                        else -> {}
                    }
                }
            }
        }
    }

//    private fun getComments() = CoroutineScope(Dispatchers.IO).launch {
//        try {
//            val querySnapshot = commentsColectionRef
//                .get()
//                .await()
//            val commentsList = ArrayList<cls_comments>() // Lista para almacenar los objetos cls_comments
//            for (document in querySnapshot.documents) {
//                val comment = document.toObject<cls_comments>()
//                if (comment != null) {
//                    commentsList.add(comment) // Añadir el comentario a la lista
//                }
//            }
//            withContext(Dispatchers.Main) {
//                // Aquí actualizamos el RecyclerView con los nuevos datos
//                (RecyclerView.adapter as? commenAdapter)?.updateData(commentsList)
//            }
//        } catch (e: Exception) {
//            withContext(Dispatchers.Main) {
//                Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
//            }
//        }
//    }

}