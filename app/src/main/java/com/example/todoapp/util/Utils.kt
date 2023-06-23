package com.example.todoapp.util

import com.example.todoapp.model.Importance
import java.text.SimpleDateFormat
import java.util.*

class Utils {
    fun convertIdToImportance(positionId: Int): Importance{
        return when(positionId){
            0 -> Importance.ORDINARY
            1 -> Importance.LOW
            2 -> Importance.URGENT
            else -> Importance.LOW
        }
    }

    fun convertImportanceToId(importance: Importance): Int{
        return when(importance){
            Importance.ORDINARY -> 0
            Importance.LOW -> 1
            Importance.URGENT -> 2
            else -> 0
        }
    }
    fun convertLongDeathlineToString(TimeinMilliSeccond :Long) =  SimpleDateFormat("dd MMMM yyyy").format(Date(TimeinMilliSeccond))
}