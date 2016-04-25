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


class AdbUserRotation(var device: IDevice) {
    companion object {
        const val URI = "content://settings/system"
        const val COLUMN = "name:s:user_rotation"
        const val WHERE = "\"name='user_rotation'\""
        const val COMMAND_QUERY = "content query --uri $URI --where $WHERE"
        const val COMMAND_DELETE = "content delete --uri $URI --where $WHERE"
        fun insertCommand(degree: UserRotationDegree) = "content insert --uri $URI --bind $COLUMN --bind ${degree.bindArgs}"
        fun updateCommand(degree: UserRotationDegree) = "content update --uri $URI --bind ${degree.bindArgs} --where $WHERE"
        val regex: Regex = Regex("value=(\\d)")
    }

    fun getUserRotationDegree(): UserRotationDegree {
        val outputs = Command(device, COMMAND_QUERY).execute()
        Logger.d(this, "getUserRotationDegree ${outputs.joinToString("\n")}")
        when (outputs.size) {
            0    -> throw RuntimeException("user_rotation has not been set yet.")
            1    -> {
                val intValue = getIntValueFromOutput(outputs[0])
                Logger.d(this, "intValue is $intValue from output '${outputs[0]}'")
                return UserRotationDegree.fromIntValue(intValue)
            }
            else -> throw RuntimeException("query execution error got outputs \n${outputs.joinToString("\n")}")
        }
    }

    fun setUserRotationDegree(degree: UserRotationDegree): Boolean {
        val commandString = insertCommand(degree)
        val outputs = Command(device, commandString).execute()
        val result = outputs.size == 0
        Logger.d(this, "setUserRotationDegree command='$commandString' output='${outputs.joinToString("\n")}' result=$result")
        return result
    }

    fun isExist(): Boolean {
        val outputs = Command(device, COMMAND_QUERY).execute()
        Logger.d(this, "isExist output=${outputs.joinToString("\n")}")
        return (outputs.size == 1 && outputs[0].contains("value="))
    }

    private fun getIntValueFromOutput(output: String): Int {
        val match = regex.find(output)
        if (match == null) {
            Logger.e(this, "output dose not contain 'value=X'. output=$output")
            throw RuntimeException("output dose not contain 'value=X'. output is '$output'")
        } else {
            return match.groups[1]!!.value.toInt()
        }
    }
}