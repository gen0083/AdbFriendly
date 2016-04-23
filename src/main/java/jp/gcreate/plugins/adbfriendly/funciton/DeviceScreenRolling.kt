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

package jp.gcreate.plugins.adbfriendly.funciton

import com.android.ddmlib.IDevice
import jp.gcreate.plugins.adbfriendly.adb.AdbAccelerometerRotation
import jp.gcreate.plugins.adbfriendly.adb.AdbUserRotation
import jp.gcreate.plugins.adbfriendly.adb.UserRotationDegree
import java.util.*


class DeviceScreenRolling(device: IDevice, callback: FunctionsCallback = FunctionsManager,
                          val times: Int, val showProgress: Boolean = false)
: FriendlyFunctions(device, callback) {

    override fun run() {
        // run rolling function
        val accelerometerRotation = AdbAccelerometerRotation(device)
        val userRotation = AdbUserRotation(device)
        var errorOutputs: ArrayList<String> = arrayListOf()
        try {
            accelerometerRotation.disableAccelerometerRotation()
            for (i in 0..times - 1) {
                if (isCancelled || Thread.interrupted()) {
                    onCancel(this)
                    throw InterruptedException("Task cancelled")
                }
                userRotation.setUserRotationDegree(UserRotationDegree.DEGREE_90)
                userRotation.setUserRotationDegree(UserRotationDegree.DEGREE_0)
            }
            accelerometerRotation.enableAccelerometerRotation()
            onSuccess(this)
        } catch(interrepted: InterruptedException) {
            onError(interrepted, errorOutputs)
        } finally {
            accelerometerRotation.enableAccelerometerRotation()
        }
    }
}