package com.example.todoapp.util

import com.example.todoapp.model.Importance
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class Utils {
    fun convertIdToImportance(positionId: Int): Importance {
        return when(positionId){
            0 -> Importance.BASIC
            1 -> Importance.LOW
            2 -> Importance.IMPORTANT
            else -> Importance.LOW
        }
    }

    fun convertImportanceToId(importance: Importance): Int{
        return when(importance){
            Importance.BASIC -> 0
            Importance.LOW -> 1
            Importance.IMPORTANT -> 2
            else -> 0
        }
    }
    fun convertLongDeathlineToString(TimeinMilliSeccond :Long) =  SimpleDateFormat("dd MMMM yyyy").format(Date(TimeinMilliSeccond))


}