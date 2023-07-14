package com.example.todoapp.network

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.example.todoapp.model.TodoItem
import com.example.todoapp.util.NotificationUtils
import com.google.gson.Gson

class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        val notificationUtils = context?.let { NotificationUtils(it) }

        val content = intent?.getStringExtra("content")
        val importance = intent?.getStringExtra("importance")

        Log.e("contentRec",content.toString())
        Log.e("importRec",importance.toString())
        val notification = notificationUtils?.getNotificationBuilder(content!!,importance!!)?.build()
        notificationUtils?.getManager()?.notify(150, notification)
    }
}