/*
 * ADB Friendly
 * Copyright 2016 gen0083
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package jp.gcreate.plugins.adbfriendly.util

import com.intellij.notification.NotificationDisplayType
import com.intellij.notification.NotificationGroup
import com.intellij.notification.NotificationType
import com.intellij.notification.Notifications
import java.text.SimpleDateFormat
import java.util.*

val hhmmddFormat : SimpleDateFormat = SimpleDateFormat("HH:mm:ss")
fun Date.toHHMMDD() : String = hhmmddFormat.format(this)

object Logger{
    // TODO: replace to false at release
    private val debug = true
    val notificationGroup = NotificationGroup("logger", NotificationDisplayType.NONE, true)

    @JvmStatic
    fun d(clazz: Any, message: String = ""){
        if(!debug) return
        output(clazz, message, "debug")
    }

    @JvmStatic
    fun e(clazz: Any, message: String){
        output(clazz, message, "error")
    }

    private fun output(clazz: Any, message: String, debugType: String){
        val output = "[${Date().toHHMMDD()}] (${clazz.javaClass.simpleName}/$debugType) $message"
        if (debug) {
            val notificationType = when(debugType) {
                "debug" -> NotificationType.INFORMATION
                "error" -> NotificationType.ERROR
                else -> NotificationType.INFORMATION
            }
            Notifications.Bus.notify(
                    notificationGroup.createNotification(output, notificationType)
            )
        }
        println(output)
    }
}
