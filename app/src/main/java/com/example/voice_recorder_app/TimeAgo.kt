package com.example.voice_recorder_app

import java.util.*
import java.util.concurrent.TimeUnit

class TimeAgo {
    fun getTimeAgo(duration: Long): String {
        val now = Date()

        val seconds = TimeUnit.MILLISECONDS.toSeconds(now.time - duration)
        val minutes = TimeUnit.MILLISECONDS.toMinutes(now.time - duration)
        val hours = TimeUnit.MILLISECONDS.toHours(now.time - duration)
        val days = TimeUnit.MILLISECONDS.toDays(now.time - duration)

        return when {
            seconds < 60 -> "just now"
            minutes.equals(1) ->  "a minute ago"
            minutes > 1 && minutes < 60 ->  minutes.toString() + "minutes ago"
            hours.equals(1) ->  "an hour ago"
            hours > 1 && hours < 24 ->  hours.toString() + "hours ago"
            days.equals(1) ->  "a day ago"
            else ->  days.toString() + "days ago"
        }
    }
}