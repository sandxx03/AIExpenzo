package com.example.aiexpenzo.data.firebase

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

//singleton object - only one instance exists to ensure centralized and consistent Firebase access
object FirebaseService{
    // by lazy - initialized only when first accessed

    //FirebaseAuth.getInstance() - creates/retrieves singleton instance to manage user login/logout/session
    val auth: FirebaseAuth by lazy {FirebaseAuth.getInstance()}

    //FirebaseAuth.getInstance() - provides access to Firebase Firestore.
    val firestore: FirebaseFirestore by lazy {FirebaseFirestore.getInstance()}
}