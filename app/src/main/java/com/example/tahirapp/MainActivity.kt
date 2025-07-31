package com.example.tahirapp

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.tahirapp.databinding.ActivityMainBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import android.content.pm.PackageManager
import android.net.Uri
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import io.appwrite.Client
import io.appwrite.ID
import io.appwrite.Role
import io.appwrite.Permission
import io.appwrite.models.InputFile
import io.appwrite.services.Storage
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.util.*


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var database: DatabaseReference
    private val bucketId = "688b111000356abeadfb"
    private val projectId = "6888c59c00344d7b9867"
    private val userId = "uploads"

    @SuppressLint("UnsafeIntentLaunch")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        requestPermissions()
        binding.uploadImageBtn.setOnClickListener {
            pickImageFromGallery()
        }

    }

    //Request Permissions

    private fun requestPermissions() {
        //This is an array of all permissions your app may need.
        //You list them here so they can be requested at once.
        //These permission constants come from android.Manifest.permission.
        val permissions = arrayOf(
            Manifest.permission.READ_MEDIA_IMAGES,
            Manifest.permission.READ_MEDIA_VIDEO,
            Manifest.permission.CAMERA
        )

        //ContextCompat.checkSelfPermission() is used to check if the permission is already granted.
        //PackageManager.PERMISSION_GRANTED means the user already allowed it.
        //The code filters out only those permissions which are not granted yet and stores them in the needed list.
        val needed = permissions.filter {

            // Condition That checks if the permission is not granted.
            ContextCompat.checkSelfPermission(this, it) != PackageManager.PERMISSION_GRANTED

        //gives same result as above
            //ContextCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_DENIED
        }

        //Checks if There are still permissions needed
        if (needed.isNotEmpty()) {
            //Not our function but a in-built android fuction that see the permissions list
            ActivityCompat.requestPermissions(this, needed.toTypedArray(), 101)
        }
    }

    // Image Picker Result Launcher
    private val imagePickerLauncher =

        //Allows the user to Open a Gallery and select an image
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let { //uri is a Uri type var which can be umm uri?.let only runs if the image is not null

                val file = createTempFileFromUri(it)
                file?.let { selectedFile ->//file?.let only runs if the image is not null
                    uploadImageToAppwrite(selectedFile)
                }
            }
        }

    //launch Picker
    private fun pickImageFromGallery() {
        //says to only allow images to be selected
        imagePickerLauncher.launch("image/*")
    }

    // 🔹 Convert URI to File
    private fun createTempFileFromUri(uri: Uri): File? {
        return try {

            //This lets you read the content of the image (or any file) from the user's device.
            val inputStream: InputStream? = contentResolver.openInputStream(uri)

            //This line creates a temporary file with a unique name like: f47ac10b-58cc-4372-a567-0e02b2c3d479
            val file = File(cacheDir, UUID.randomUUID().toString() + ".jpg")

            //Open an output stream that lets you write data into the temporary file.
            val outputStream = FileOutputStream(file)

            //This line copies the content from the input stream (image from the URI) into the output stream (your new file).
            //If the inputStream is not null, the image gets copied.
            inputStream?.copyTo(outputStream)

            //closes the stream
            inputStream?.close()
            outputStream.close()

            //return the file
            file

            //if any error occurs null will be passed
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    // 🔹 Upload to Appwrite & Save URL to Firebase
    private fun uploadImageToAppwrite(file: File) {
        val client = Client(this)
            .setEndpoint("https://fra.cloud.appwrite.io/v1") // Appwrite Cloud endpoint
            .setProject(projectId)

        // Initializes the Storage service from Appwrite SDK using the client.
        //This is the object you'll use to interact with Appwrite Storage API (like uploading or downloading files).
        val storage = Storage(client)

        //Converts your local File object into InputFile, which is Appwrite's expected format for uploads.
        val inputFile = InputFile.fromFile(file)

        lifecycleScope.launch {
            try {
                val result = storage.createFile(
                    bucketId = bucketId,     // Replace with your bucket ID
                    fileId = ID.unique(),
                    file = inputFile,
                    permissions = listOf(
                        Permission.read(Role.any()) // Public read access
                    )
                )

                val fileId = result.id
                val publicUrl =
                    "https://fra.cloud.appwrite.io/v1/storage/buckets/$bucketId/files/$fileId/view?project=$projectId"

                // Save to Firebase
                val imageName = binding.imageNameEditText.text.toString()

                val userId = "user123" // Replace with your user ID logic
                val dbRef = FirebaseDatabase.getInstance().getReference("users")
                    .child(userId)
                    .child("reports")
                    .child("photos")
                    .push()

                // Display Image
                //Glide.with(this@MainActivity).load(publicUrl).into(binding.imageViewArea)


                Toast.makeText(this@MainActivity, "Uploaded & URL saved to Firebase", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                Toast.makeText(this@MainActivity, "Upload failed: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }
}