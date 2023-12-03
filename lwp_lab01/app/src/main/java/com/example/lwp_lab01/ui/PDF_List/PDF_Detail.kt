package com.example.lwp_lab01.ui.PDF_List

import android.content.ContentValues.TAG
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.Target
import com.example.lwp_lab01.R
import com.example.lwp_lab01.entities.cls_PDFs
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


class PDF_Detail : AppCompatActivity() {
    private lateinit var btnAddComment: Button
    private lateinit var imageUserPost: ImageView
    private lateinit var imageUserPostComment: ImageView
    private lateinit var textTitlePDF: TextView
    private lateinit var textDate: TextView
    private lateinit var textInfoPdf: TextView
    private lateinit var commentEditText: EditText
    var db = FirebaseFirestore.getInstance()
    var auth = FirebaseAuth.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pdf_detail)
        btnAddComment = findViewById(R.id.post_detail_add_comment_btn)
        imageUserPost = findViewById(R.id.post_detail_user_img)
        imageUserPostComment = findViewById(R.id.post_detail_currentuser_img)
        textTitlePDF = findViewById(R.id.post_detail_title)
        textDate = findViewById(R.id.post_detail_date_name)
        textInfoPdf = findViewById(R.id.post_detail_desc)
        commentEditText = findViewById(R.id.post_detail_comment)


//        val postImage = intent.extras!!.getString("postImage")
//        Glide.with(this).load(postImage).into<Target<Drawable>>(imgPost)
//
//        val postTitle = intent.extras!!.getString("title")
//        textTitlePDF.setText(postTitle)
//
//        val userpostImage = intent.extras!!.getString("userPhoto")
//        Glide.with(this).load(userpostImage).into<Target<Drawable>>(imgUserPost)
//
//        val postDescription = intent.extras!!.getString("description")
//        textInfoPdf.setText(postDescription)
//
//        // setcomment user image
//
//
//        // setcomment user image
//        Glide.with(this).load(firebaseUser.getPhotoUrl()).into<Target<Drawable>>(imgCurrentUser)
//        // get post id
//        // get post id
//        PostKey = intent.extras!!.getString("postKey")
//
//        val date: String = timestampToString(intent.extras!!.getLong("postDate"))
//        txtPostDateName.setText(date)
    }
    private fun obtenerDatos() {
        //Toast.makeText(this,"Esperando hacer algo importante", Toast.LENGTH_LONG).show()
        var coleccion: ArrayList<cls_PDFs?> = ArrayList()
        var listaView: RecyclerView = findViewById(R.id.recyclerView)
        db.collection("PDF´s_modules").orderBy("CategoryID")
            .get()
            .addOnCompleteListener { docc ->
                if (docc.isSuccessful) {
                    for (document in docc.result!!) {
                        Log.d(TAG, document.id + " => " + document.data)
                        //var datos: cls_PDFs = cls_PDFs(document.data["CategoryID"].toString().toInt(),
                          //  document.data["CategoryName"].toString(),
                           // document.data["Description"].toString(),
                            //document.data["urlImage"].toString())
                        //coleccion.add(datos)
                    }
                    //var adapter: CategoryAdapter = CategoryAdapter(this, coleccion)
                    //listaView.adapter =adapter
                } else {
                    Log.w(TAG, "Error getting documents.", docc.exception)
                }
            }
    }
}