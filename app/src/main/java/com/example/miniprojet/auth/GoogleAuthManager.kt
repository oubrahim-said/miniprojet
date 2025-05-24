package com.example.miniprojet.auth

import android.content.Context
import android.content.Intent
import com.example.miniprojet.data.model.User
import com.example.miniprojet.data.repository.UserRepository
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GoogleAuthManager(private val context: Context) {
    
    private val userRepository = UserRepository()
    
    // Configure Google Sign-In
    private val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestEmail()
        .requestId()
        .requestProfile()
        .build()
    
    val googleSignInClient: GoogleSignInClient = GoogleSignIn.getClient(context, gso)
    
    // Check if user is already signed in
    fun getLastSignedInAccount(): GoogleSignInAccount? {
        return GoogleSignIn.getLastSignedInAccount(context)
    }
    
    // Handle sign-in result
    suspend fun handleSignInResult(completedTask: Task<GoogleSignInAccount>): Result<User> {
        return withContext(Dispatchers.IO) {
            try {
                val account = completedTask.getResult(ApiException::class.java)
                
                // Create user from Google account
                val user = User(
                    id = account.id ?: "",
                    name = account.displayName ?: "",
                    email = account.email ?: "",
                    photoUrl = account.photoUrl?.toString(),
                    studentId = null,
                    department = null
                )
                
                // Save user to database
                userRepository.insertUser(user)
                
                Result.success(user)
            } catch (e: ApiException) {
                Result.failure(e)
            }
        }
    }
    
    // Sign out
    fun signOut() {
        googleSignInClient.signOut()
    }
    
    // Get sign-in intent
    fun getSignInIntent(): Intent {
        return googleSignInClient.signInIntent
    }
}
