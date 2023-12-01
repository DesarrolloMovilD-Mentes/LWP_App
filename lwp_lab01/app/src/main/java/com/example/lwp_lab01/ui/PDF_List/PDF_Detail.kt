package com.example.lwp_lab01.ui.PDF_List

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.Target
import com.example.lwp_lab01.R


class PDF_Detail : AppCompatActivity() {
    private lateinit var btnAddComment: Button
    private lateinit var imageUserPost: ImageView
    private lateinit var imageUserPostComment: ImageView
    private lateinit var textTitlePDF: TextView
    private lateinit var textDate: TextView
    private lateinit var textInfoPdf: TextView
    private lateinit var commentEditText: EditText

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
}