import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import android.Manifest
import android.content.pm.PackageManager
import android.app.Activity
import android.net.Uri
import com.bumptech.glide.Glide
import com.squareup.picasso.Picasso
import com.example.lwp_lab01.databinding.ActivityNewEntityBinding

class NewEntityActivity : AppCompatActivity() {

    // Uso de ViewBinding para el layout de activity_new_entity
    private lateinit var binding: ActivityNewEntityBinding

    companion object {
        private const val GALLERY_REQUEST_CODE = 1
        private const val CAMERA_PERMISSION_CODE = 100
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewEntityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnTakeOrChoosePhoto.setOnClickListener {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), CAMERA_PERMISSION_CODE)
            } else {
                showImagePickerOptions()
            }
        }
    }


    private fun showImagePickerOptions() {
        // Aquí puedes implementar un AlertDialog para elegir entre cámara o galería
        // Por simplicidad, voy a lanzar directamente la galería
        val photoPickerIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        photoPickerIntent.type = "image/*"
        startActivityForResult(photoPickerIntent, GALLERY_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_OK && requestCode == GALLERY_REQUEST_CODE) {
            val selectedImageUri: Uri? = data?.data

            // Asegúrate de que la referencia a 'selectedImage' en 'binding' sea correcta
            if (selectedImageUri != null) {
                Glide.with(this)
                    .load(selectedImageUri)
                    .into(binding.selectedImage)
            }
        }
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            CAMERA_PERMISSION_CODE -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    showImagePickerOptions()
                } else {
                    // Explica al usuario que la función no está disponible sin permisos
                }
            }
            // Puedes manejar otros resultados de permisos aquí
        }
    }
}
