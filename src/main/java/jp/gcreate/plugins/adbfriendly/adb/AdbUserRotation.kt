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
import jp.gcreate.plugins.adbfriendly.Util.Logger


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
                return UserRotationDegree.DEGREE_0
            }
            else -> throw RuntimeException("query execution error got outputs \n${outputs.joinToString("\n")}")
        }
    }
}