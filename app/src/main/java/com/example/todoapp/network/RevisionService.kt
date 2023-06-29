package com.example.todoapp.network

import android.content.Context
import android.content.SharedPreferences
import com.example.todoapp.R
import com.example.todoapp.util.REVISION

class RevisionService(context: Context) {
    private var sharedPreferences: SharedPreferences =
        context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE)

    fun setRevision(revision: Int) = sharedPreferences.edit().putInt(REVISION, revision).apply()
    fun fetchRevision(): Int = sharedPreferences.getInt(REVISION, 0)

}