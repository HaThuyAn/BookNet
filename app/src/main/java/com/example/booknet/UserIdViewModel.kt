package com.example.booknet

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import java.util.UUID

class UserIdViewModel: ViewModel() {
    fun saveUserId(context: Context, userId: UUID) {
        val sharedPreferences: SharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putString("userId", userId.toString())
        editor.apply()
    }

    fun retrieveUserId(context: Context): String? {
        val sharedPreferences: SharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        return sharedPreferences.getString("userId", null)
    }
}