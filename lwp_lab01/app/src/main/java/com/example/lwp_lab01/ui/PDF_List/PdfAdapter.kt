package com.example.lwp_lab01.ui.PDF_List

import android.app.DownloadManager
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.AsyncTask
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.lwp_lab01.R
import java.net.HttpURLConnection
import java.net.URL

interface DownloadListener {
    fun onDownloadStarted(pdfName: String)
}

class PdfAdapter(private val pdfList: List<PdfModel>, private val listener: DownloadListener) :
    RecyclerView.Adapter<PdfAdapter.PdfViewHolder>() {

    private val context: Context by lazy { listener as Context }

    class PdfViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvPdfName: TextView = view.findViewById(R.id.tvPdfName)
        val btnDownload: Button = view.findViewById(R.id.btnDownload)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PdfViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.pdf_item, parent, false)
        return PdfViewHolder(view)
    }

    override fun onBindViewHolder(holder: PdfViewHolder, position: Int) {
        val pdf = pdfList[position]
        holder.tvPdfName.text = pdf.nombre

        // Load image from URL

        // Set click listener for the download button
        holder.btnDownload.setOnClickListener {
            // Implement your download logic here
            downloadPdf(pdf.url, pdf.nombre)
            // Notify the listener that download has started
            listener.onDownloadStarted(pdf.nombre)
        }
    }

    override fun getItemCount() = pdfList.size

    private class DownloadImageTask(private val imageView: ImageView) : AsyncTask<String, Void, Bitmap?>() {
        override fun doInBackground(vararg urls: String): Bitmap? {
            return try {
                val url = URL(urls[0])
                val connection: HttpURLConnection = url.openConnection() as HttpURLConnection
                connection.doInput = true
                connection.connect()
                val inputStream = connection.inputStream
                BitmapFactory.decodeStream(inputStream)
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }

        override fun onPostExecute(result: Bitmap?) {
            result?.let {
                imageView.setImageBitmap(result)
            }
        }
    }

    private fun downloadPdf(pdfUrl: String, pdfName: String) {
        val request = DownloadManager.Request(Uri.parse(pdfUrl))
            .setTitle(pdfName)
            .setDescription("Downloading")
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            .setAllowedOverMetered(true)
            .setAllowedOverRoaming(true)
            .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, pdfName)

        val downloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        downloadManager.enqueue(request)
    }
}
