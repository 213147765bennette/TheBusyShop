package com.ikhokha.techcheck.util

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 * Created by Bennette Molepo on 11/06/2022.
 */
object TodayDate {
    @RequiresApi(Build.VERSION_CODES.O)
    fun getTodayDateTime(): String {
        val now: LocalDateTime = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
        return now.format(formatter)
    }
}