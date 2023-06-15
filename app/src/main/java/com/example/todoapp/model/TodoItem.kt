package com.example.todoapp.model

import java.util.Date

data class TodoItem(
    var id:String,
    var content:String?,
    var importance: Importance?,
    var flag:Boolean?,
    var dateCreated: Long?,
    var dateChanged: Long?=0L,
    var deadline: Long? = 0L,
    ){
}
