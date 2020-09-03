package com.example.helloworld2

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
import kotlinx.android.synthetic.main.activity_camera.*
import java.io.File

const val REQUEST_IMAGE_CAPTURE = 99

class CameraActivity : AppCompatActivity() {
    private var fileName = "temp_photo"
    private val takePicture = registerForActivityResult(ActivityResultContracts.TakePicturePreview()){
        imageView.setImageBitmap(it)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera)

        cameraButton.setOnClickListener {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)  {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 1)
            } else {
                getImage()
            }
        }

        highResButton.setOnClickListener{
            takePicture.launch(null)
        }
    }

    private fun getImage() {
        val imgPath = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        var imageFile: File? = null
        imageFile = File.createTempFile(fileName, ".jpg", imgPath)
        fileName = imageFile!!.absolutePath
        val photoURI: Uri = FileProvider.getUriForFile(
            this,
            "com.example.helloworld2.fileprovider",
            imageFile)
        val imgIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (imgIntent.resolveActivity(packageManager) != null) {
            imgIntent.flags = Intent.FLAG_GRANT_WRITE_URI_PERMISSION
            imgIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
            startActivityForResult(imgIntent, REQUEST_IMAGE_CAPTURE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            fullImageView.setImageBitmap(BitmapFactory.decodeFile(fileName))
        }
    }
}