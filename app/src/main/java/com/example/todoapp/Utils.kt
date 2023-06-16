package com.example.todoapp

import com.example.todoapp.model.Importance
import java.text.SimpleDateFormat
import java.util.*

class Utils {
    fun convertIdToImportance(positionId: Int): Importance{
        return when(positionId){
            0 -> Importance.LOW
            1 -> Importance.ORDINARY
            2 -> Importance.URGENT
            else -> Importance.LOW
        }
    }

    fun convertLongDeathlineToString(TimeinMilliSeccond :Long):String{
        val dateString: String = SimpleDateFormat("dd MMMM yyyy").format(Date(TimeinMilliSeccond))
        println(dateString)
        return dateString
    }
}