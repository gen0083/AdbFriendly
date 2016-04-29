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

import com.intellij.ide.util.PropertiesComponent

object PluginConfig {
    const val DEVICE_SERIAL = "jp.gcreate.plugins.adbfriendly.device_serial"
    const val ROTATE_COUNT = "jp.gcreate.plugins.adbfriendly.rotate_count"
    const val SHOW_PROGRESS = "jp.gcreate.plugins.adbfriendly.show_progress"
    const val ADB_PATH = "jp.gcreate.plugins.adbfriendly.adb_path"

    var deviceSerial: String = ""
    var rotateCount: Int = 0
    var showProgress: Boolean = false
    var adbPath: String = ""

    init {
        val component = PropertiesComponent.getInstance()
        deviceSerial = component.getValue(DEVICE_SERIAL, "")
        rotateCount = component.getOrInitInt(ROTATE_COUNT, 0)
        showProgress = component.getBoolean(SHOW_PROGRESS, false)
        adbPath = component.getValue(ADB_PATH, "")
    }

    fun save() {
        val component = PropertiesComponent.getInstance()
        component.apply {
            setValue(DEVICE_SERIAL, deviceSerial)
            setValue(ROTATE_COUNT, rotateCount.toString())
            setValue(SHOW_PROGRESS, showProgress.toString())
            setValue(ADB_PATH, adbPath)
        }
    }
}
