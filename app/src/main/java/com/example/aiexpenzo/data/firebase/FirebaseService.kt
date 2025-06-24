package com.example.aiexpenzo.data.firebase

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

object FirebaseService{
    val auth: FirebaseAuth by lazy {FirebaseAuth.getInstance()}
    val firestore: FirebaseFirestore by lazy {FirebaseFirestore.getInstance()}
}