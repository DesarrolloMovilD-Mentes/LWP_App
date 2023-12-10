package com.example.lwp_lab01.ui.comment_List
import android.content.Context
import android.media.Image
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.bumptech.glide.Glide
import com.example.lwp_lab01.R
import com.example.lwp_lab01.entities.cls_comments
import com.google.firebase.storage.FirebaseStorage

class CommentAdapter
    (context: Context, dataModalArrayList: ArrayList<cls_comments?>?) :
    ArrayAdapter<cls_comments?>(context, 0, dataModalArrayList!!) {
    var imgs = FirebaseStorage.getInstance()
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var listitemView = convertView
        if (listitemView == null) {
            listitemView = LayoutInflater.from(context).inflate(R.layout.comment_cards, parent, false)
        }

        val dataModal: cls_comments? = getItem(position)

        val CommentText = listitemView!!.findViewById<TextView>(R.id.textViewComentario)
        val dateComment = listitemView!!.findViewById<TextView>(R.id.textViewFecha)
        val UserImage = listitemView!!.findViewById<ImageView>(R.id.imageViewAvatar)
        val userName = listitemView!!.findViewById<TextView>(R.id.textViewNombre)

        if (dataModal != null) {
            CommentText.setText(dataModal.comment.toString())
            dateComment.setText(dataModal.date.toString())
            userName.setText(dataModal.userName.toString())
            //Glide.with(context).load(dataModal.urlImage).into(UserImage)

        }

        listitemView.setOnClickListener { // on the item click on our list view.
            // we are displaying a toast message.
            if (dataModal != null) {
                // Toast.makeText(context, "Item clicked is : " + dataModal.CategoryName, Toast.LENGTH_SHORT).show()
            }
        }
        return listitemView
    }
}