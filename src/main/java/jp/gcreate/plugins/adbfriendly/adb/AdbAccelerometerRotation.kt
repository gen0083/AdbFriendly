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

package jp.gcreate.plugins.adbfriendly.adb

import com.android.ddmlib.IDevice
import jp.gcreate.plugins.adbfriendly.util.Logger


class AdbAccelerometerRotation(var device: IDevice) {
    companion object {
        const val URI = "content://settings/system"
        const val COLUMN = "name:s:accelerometer_rotation"
        const val ON = "value:i:1"
        const val OFF = "value:i:0"
        const val WHERE = "\"name='accelerometer_rotation'\""
        const val COMMAND_INSERT_ON = "content insert --uri $URI --bind $COLUMN --bind $ON"
        const val COMMAND_INSERT_OFF = "content insert --uri $URI --bind $COLUMN --bind $OFF"
        const val COMMAND_UPDATE_ON = "content update --uri $URI --bind $ON --where $WHERE"
        const val COMMAND_UPDATE_OFF = "content update --uri $URI --bind $OFF --where $WHERE"
        const val COMMAND_QUERY = "content query --uri $URI --where $WHERE"
        const val COMMAND_DELETE = "content delete --uri $URI --where $WHERE"
    }

    fun enableAccelerometerRotation(): Boolean {
        val command = if (isAlreadyExist()) COMMAND_UPDATE_ON else COMMAND_INSERT_ON
        val outputs = Command(device, command).execute()
        val result = outputs.size == 0
        Logger.d(this, "enableAccelerometerRotation output=${outputs.joinToString("\n")} result=$result")
        return result
    }

    fun disableAccelerometerRotation(): Boolean {
        val command = if (isAlreadyExist()) COMMAND_UPDATE_OFF else COMMAND_INSERT_OFF
        val outputs = Command(device, command).execute()
        val result = outputs.size == 0
        Logger.d(this, "disableAccelerometerRotation output=${outputs.joinToString("\n")} result=$result")
        return result
    }

    fun deleteAccelerometerRotation(): Boolean {
        val outputs = Command(device, COMMAND_DELETE).execute()
        val result = outputs.size == 0
        Logger.d(this, "delete output=${outputs.joinToString("\n")} result=$result")
        return result
    }

    fun isAlreadyExist(): Boolean {
        val outputs = Command(device, COMMAND_QUERY).execute()
        val result = (outputs.size == 1 && outputs[0].contains("value="))
        Logger.d(this, "isAlreadyExist output=${outputs.joinToString("\n")}} result=$result")
        return result
    }

    fun isEnabled(): Boolean {
        val outputs = Command(device, COMMAND_QUERY).execute()
        val result = if (outputs.size == 1) {
            if (outputs[0].contains("value=1")) {
                true
            } else {
                false
            }
        } else {
            throw RuntimeException("Entry is not exist. Insert accelerometer_rotation first")
        }
        Logger.d(this, "isEnabled output=${outputs.joinToString("\n")} result=$result")
        return result
    }
}