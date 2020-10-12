package com.example.messenger.registerlogin

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore.Images.Media.getBitmap
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.messenger.R
import com.example.messenger.models.User
import com.example.messenger.messages.LatestMessagesActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_register.*
import java.util.UUID

class RegisterActivity : AppCompatActivity() {

    private var selectedPhotoUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

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
        if (requestCode == 0 && resultCode == Activity.RESULT_OK && data != null) {
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
                Log.d(LOG_TAG, "Successfully created user : ${it.result?.user?.uid}")
                uploadImageToFirebase()
            }
            .addOnFailureListener {
                Log.d(LOG_TAG, "Failed to create user : ${it.message}")
                Toast.makeText(this, "Failed to create user: ${it.message}", Toast.LENGTH_LONG)
                    .show()
            }
    }

    private fun uploadImageToFirebase() {
        val fileName = UUID.randomUUID().toString()
        val ref = FirebaseStorage.getInstance().getReference("/images/$fileName")
        selectedPhotoUri?.let { uri ->
            ref.putFile(uri)
        }
        saveUserToDatabase(ref.downloadUrl.toString())
    }

    private fun saveUserToDatabase(profileImageUrl: String) {
        val uid = FirebaseAuth.getInstance().uid
        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")
        uid?.let {
            val user =
                User(
                    uid,
                    userNameEditText.text.toString(),
                    profileImageUrl
                )
            ref.setValue(user).addOnSuccessListener {
                Log.d(LOG_TAG, "Successfully added user to firebase database")
                val intent = Intent(this, LatestMessagesActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }
        }
    }

    companion object {
        const val LOG_TAG = "Register Activity"
    }
}
