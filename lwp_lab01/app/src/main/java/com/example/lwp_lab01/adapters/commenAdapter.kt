package com.example.lwp_lab01.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.lwp_lab01.R
import com.example.lwp_lab01.entities.cls_comments
import com.example.lwp_lab01.databinding.CommentCardsBinding

class commenAdapter(
    private val context: Context,
    private var dataModalArrayList: ArrayList<cls_comments?>
) : RecyclerView.Adapter<commenAdapter.CommentsViewHolder>() {

    // Aquí definimos el ViewHolder para nuestro adaptador
    class CommentsViewHolder(val binding: CommentCardsBinding) : RecyclerView.ViewHolder(binding.root)

    // Creamos una nueva vista (invocada por el layout manager)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentsViewHolder {
        val binding = CommentCardsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CommentsViewHolder(binding)
    }

    // Reemplazamos el contenido de una vista (invocado por el layout manager)
    override fun onBindViewHolder(holder: CommentsViewHolder, position: Int) {
        val dataModal = dataModalArrayList[position]
        holder.binding.textViewComentario.text = dataModal?.comment

        // Manejo de clics en cada elemento
        holder.itemView.setOnClickListener {
            dataModal?.comment?.let {
                Toast.makeText(context, "Item clicked is : $it", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Regresa el tamaño de tu dataset (invocado por el layout manager)
    override fun getItemCount() = dataModalArrayList.size

    // Método para actualizar la lista de datos
    fun updateData(newData: ArrayList<cls_comments?>) {
        dataModalArrayList = newData
        notifyDataSetChanged()
    }
}
