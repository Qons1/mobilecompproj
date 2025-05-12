package com.example.trackforce.viewmodels

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import com.example.trackforce.data.models.User
import java.util.Date

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) : ViewModel() {

    fun login(email: String, password: String, callback: (Boolean, String) -> Unit) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    callback(true, "Login successful")
                } else {
                    callback(false, task.exception?.message ?: "Login failed")
                }
            }
    }

    fun signup(email: String, password: String, role: String, callback: (Boolean, String) -> Unit) {
        if (role != User.ROLE_MANAGER && role != User.ROLE_EMPLOYEE) {
            callback(false, "Invalid role. Must be either MANAGER or EMPLOYEE")
            return
        }

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val firebaseUser = task.result?.user
                    if (firebaseUser != null) {
                        val user = User(
                            uid = firebaseUser.uid,
                            email = email,
                            role = role,
                            createdAt = Date()
                        )
                        
                        firestore.collection("users")
                            .document(user.uid)
                            .set(user)
                            .addOnSuccessListener {
                                callback(true, "Signup successful")
                            }
                            .addOnFailureListener { e ->
                                // Cleanup: delete the auth user if Firestore fails
                                firebaseUser.delete()
                                callback(false, e.message ?: "Failed to create user profile")
                            }
                    } else {
                        callback(false, "Failed to create user")
                    }
                } else {
                    callback(false, task.exception?.message ?: "Signup failed")
                }
            }
    }

    fun getCurrentUser() = auth.currentUser

    fun signOut() {
        auth.signOut()
    }
}

