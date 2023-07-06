package com.example.todoapp.network

import android.content.Context
import android.content.SharedPreferences
import com.example.todoapp.R
import com.example.todoapp.util.REVISION
import com.example.todoapp.util.REVISION_DATABASE
import javax.inject.Inject

class RevisionService @Inject constructor(context: Context) {
    private var sharedPreferences: SharedPreferences =
        context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE)

    fun setRevisionNetwork(revision: Int) = sharedPreferences.edit().putInt(REVISION, revision).apply()
    fun getRevisionNetwork(): Int = sharedPreferences.getInt(REVISION, 0)


     fun setRevisionDatabase(revision: Int)  = sharedPreferences.edit().putInt(REVISION_DATABASE, revision).apply()
     fun getRevisionDatabase(): Int = sharedPreferences.getInt(REVISION_DATABASE, 0)

}