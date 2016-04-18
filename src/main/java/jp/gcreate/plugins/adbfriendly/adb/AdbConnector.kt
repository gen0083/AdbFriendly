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

import com.android.ddmlib.AndroidDebugBridge
import com.android.ddmlib.AndroidDebugBridge.*
import com.android.ddmlib.Client
import com.android.ddmlib.IDevice
import com.intellij.notification.Notification
import com.intellij.notification.NotificationType
import com.intellij.notification.Notifications
import jp.gcreate.plugins.adbfriendly.Util.Logger
import javax.swing.SwingUtilities


object AdbConnector : IClientChangeListener, IDeviceChangeListener, IDebugBridgeChangeListener {
    private var initialized = false

    init {
        connectAdb()
    }

    fun connectAdb() {
        if (initialized) {
            AndroidDebugBridge.terminate()
        }
        AndroidDebugBridge.initIfNeeded(false)
        AndroidDebugBridge.createBridge()
        AndroidDebugBridge.addClientChangeListener(this)
        AndroidDebugBridge.addDebugBridgeChangeListener(this)
        AndroidDebugBridge.addDeviceChangeListener(this)
    }

    fun getAdbBridge(): AndroidDebugBridge {
        return AndroidDebugBridge.getBridge()
    }

    fun getDevices(): Array<IDevice> {
        return getAdbBridge().devices
    }

    override fun clientChanged(client: Client, changeMask: Int) {
        Notifications.Bus.notify(
                Notification("clientChanged",
                        "Adb clientChanged",
                        "client:$client mask:$changeMask",
                        NotificationType.INFORMATION)
        )
        Logger.d(this, "clientChanged $client $changeMask")
    }

    override fun deviceChanged(device: IDevice, changeMask: Int) {
        Notifications.Bus.notify(
                Notification("deviceChanged",
                        "Adb deviceChanged",
                        "device:$device mask:$changeMask",
                        NotificationType.INFORMATION)
        )
        Logger.d(this, "deviceChanged $device")
    }

    override fun deviceConnected(device: IDevice) {
        Notifications.Bus.notify(
                Notification("deviceConnected",
                        "Adb deviceConnected",
                        "client:$device",
                        NotificationType.INFORMATION)
        )
        Logger.d(this, "deviceConnected $device")
    }

    override fun deviceDisconnected(device: IDevice) {
        Notifications.Bus.notify(
                Notification("deviceDisconnected",
                        "Adb deviceDisconnected",
                        "client:$device",
                        NotificationType.INFORMATION)
        )
        Logger.d(this, "deviceDisconnected $device")
    }

    /**
     * If AndroidDebugBridge is changed then notify this callback.
     */
    override fun bridgeChanged(bridge: AndroidDebugBridge) {
        Notifications.Bus.notify(
                Notification("bridgeChanged",
                        "Adb bridgeChanged",
                        "client:$bridge",
                        NotificationType.INFORMATION)
        )
        Logger.d(this, "bridgeChanged $bridge")
    }
}