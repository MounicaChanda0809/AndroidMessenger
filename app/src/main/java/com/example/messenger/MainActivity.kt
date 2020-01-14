package com.example.messenger

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore.Images.Media.getBitmap
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_main.*
import java.util.UUID

class MainActivity : AppCompatActivity() {

    private var selectedPhotoUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        registerButton.setOnClickListener {
        performRegister()
        }

        profilePictureTextView.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, 0)
        }

        alreadyHavaAnAccountTextView.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    @Suppress("DEPRECATION")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 0 && resultCode.equals(Activity.RESULT_OK) && data != null) {
           // Log.d("*************", "Photo Selected")
            selectedPhotoUri = data.data
            val bitmap = getBitmap(contentResolver, selectedPhotoUri)
            selectProfilePictureImage.setImageBitmap(bitmap)
            profilePictureTextView.alpha = 0f
        }
    }

    private fun performRegister() {
        val email = emailEditText.text.toString()
        val password = passwordEditText.text.toString()
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please enter text in email and password", Toast.LENGTH_LONG)
                .show()
            return
        }

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (!it.isSuccessful) return@addOnCompleteListener
                Log.d("MainActivity", "Successfully created user : ${it.result?.user?.uid}")
                uploadImageToFirebase()
            }
            .addOnFailureListener {
                Log.d("FIREBASE", "Failed to create user : ${it.message}")
                Toast.makeText(this, "Failed to create user: ${it.message}", Toast.LENGTH_LONG)
                    .show()
            }
    }

    private fun uploadImageToFirebase() {
        val fileName= UUID.randomUUID().toString()
        val ref = FirebaseStorage.getInstance().getReference("/images/$fileName")
        selectedPhotoUri?.let { uri ->
            ref.putFile(uri)
                .addOnSuccessListener {
                    Log.d("MainActivity", "Successfully uploaded image : ${it.metadata?.path}")
                    ref.downloadUrl.addOnSuccessListener { downloadUrl ->
                        saveUserToDatabase(downloadUrl.toString())
                    }
                }
        }
    }

    private fun saveUserToDatabase(profileImageUrl: String) {
        val uid =  FirebaseAuth.getInstance().uid
        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")
        uid?.let {
            val user = User(uid, userNameEditText.text.toString(), profileImageUrl)
            ref.setValue(user).addOnSuccessListener {
                Log.d("MainActivity", "Successfully added user to firebase database")
            }
        }
    }
}

data class User(val userId: String, val userName: String, val profileImageUrl: String)

