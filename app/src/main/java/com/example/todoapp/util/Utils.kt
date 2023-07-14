package com.example.todoapp.util

import android.util.Log
import com.example.todoapp.model.Importance
import java.text.DateFormat
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

fun convertLongDeathlineToString(TimeinMilliSeccond :Long) =  SimpleDateFormat("dd MMMM yyyy").format(Date(TimeinMilliSeccond))

fun convertMinutesToLong( TimeinMilliSeccond:Long,hour:Int,min:Int):Long{
    var year = SimpleDateFormat("yyyy").format(Date(TimeinMilliSeccond))
    var day = SimpleDateFormat("dd").format(Date(TimeinMilliSeccond))
    var mount = SimpleDateFormat("MM").format(Date(TimeinMilliSeccond))

    val date = SimpleDateFormat("yyyy MM dd hh mm").parse("$year $mount $day $hour $min")
    return date.time
}
fun convertImportanceToString(importance: Importance): String{
    return when(importance){
        Importance.BASIC -> "Нет"
        Importance.LOW -> "Низкий"
        Importance.IMPORTANT -> "Высокий"
        else -> "Нет"
    }
}
fun convertIntTimeToString(hour: Int,min: Int): String {
    var ans = "00:00"
    if(hour>10){
        if(min>10) ans = "$hour:$min"
        else ans = "$hour:0$min"
    }
    else{
        if(min>10)ans = "0$hour:$min"
        else "0$hour:0$min"
    }
    return ans
}