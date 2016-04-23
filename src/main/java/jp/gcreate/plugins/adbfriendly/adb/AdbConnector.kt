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
import jp.gcreate.plugins.adbfriendly.util.Logger
import java.util.*


object AdbConnector : IClientChangeListener, IDeviceChangeListener, IDebugBridgeChangeListener {
    private var initialized = false

    init {
        connectAdb()
    }

    fun connectAdb() {
        if (initialized) {
            AndroidDebugBridge.removeClientChangeListener(this)
            Logger.d(this, "in init already initialized removeClientChangeListener")
            AndroidDebugBridge.removeDebugBridgeChangeListener(this)
            Logger.d(this, "in init already initialized removeDebugBridgeChangeListener")
            AndroidDebugBridge.removeDeviceChangeListener(this)
            Logger.d(this, "in init already initialized removeDeviceChangeListener")
            AndroidDebugBridge.terminate()
            Logger.d(this, "in init already initialized terminate")
        }
        initialized = true
        AndroidDebugBridge.initIfNeeded(false)
        Logger.d(this, "in init initIfNeeded()")
        AndroidDebugBridge.createBridge()
        Logger.d(this, "in init createBridge()")
        AndroidDebugBridge.addClientChangeListener(this)
        Logger.d(this, "in init addClientChangeListener")
        AndroidDebugBridge.addDebugBridgeChangeListener(this)
        Logger.d(this, "in init addDebugBridgeChangeListener")
        AndroidDebugBridge.addDeviceChangeListener(this)
        Logger.d(this, "in init addDeviceChangeListener")
    }

    fun getAdbBridge(): AndroidDebugBridge {
        return AndroidDebugBridge.getBridge()
    }

    fun getDevices(): Array<IDevice> {
        return getAdbBridge().devices
    }

    private val clientCallbacks: ArrayList<IClientChangeListener> = arrayListOf()

    fun addClientChangeListener(listener: IClientChangeListener) {
        if (clientCallbacks.contains(listener)) {
            throw IllegalStateException("ClientChangeListener [$listener] is already added. You may forget to remove listener.")
        }
        clientCallbacks.add(listener)
    }

    fun removeClientChangeListener(listener: IClientChangeListener) {
        clientCallbacks.remove(listener)
    }

    override fun clientChanged(client: Client, changeMask: Int) {
        clientCallbacks.forEach {
            it.clientChanged(client, changeMask)
        }
        Notifications.Bus.notify(
                Notification("clientChanged",
                        "Adb clientChanged",
                        "client:$client mask:$changeMask",
                        NotificationType.INFORMATION)
        )
        Logger.d(this, "clientChanged $client $changeMask")
    }

    private val deviceCallbacks: ArrayList<IDeviceChangeListener> = arrayListOf()

    fun addDeviceChangeListener(listener: IDeviceChangeListener) {
        if (deviceCallbacks.contains(listener)) {
            throw IllegalStateException("DeviceChangedListener [$listener}] is already added. You may forget to remove listener.")
        }
        deviceCallbacks.add(listener)
    }

    fun removeDeviceChangedListener(listener: IDeviceChangeListener) {
        deviceCallbacks.remove(listener)
    }

    override fun deviceChanged(device: IDevice, changeMask: Int) {
        deviceCallbacks.forEach {
            it.deviceChanged(device, changeMask)
        }
        Notifications.Bus.notify(
                Notification("deviceChanged",
                        "Adb deviceChanged",
                        "device:$device mask:$changeMask",
                        NotificationType.INFORMATION)
        )
        Logger.d(this, "deviceChanged $device")
    }

    override fun deviceConnected(device: IDevice) {
        deviceCallbacks.forEach {
            it.deviceConnected(device)
        }
        Notifications.Bus.notify(
                Notification("deviceConnected",
                        "Adb deviceConnected",
                        "client:$device",
                        NotificationType.INFORMATION)
        )
        Logger.d(this, "deviceConnected $device")
    }

    override fun deviceDisconnected(device: IDevice) {
        deviceCallbacks.forEach {
            it.deviceDisconnected(device)
        }
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
    private val bridgeCallbacks: ArrayList<IDebugBridgeChangeListener> = arrayListOf()

    fun addBridgeChangedListener(listener: IDebugBridgeChangeListener) {
        if (bridgeCallbacks.contains(listener)) {
            throw IllegalStateException("DebugBridgeChangedListener [$listener}] is already added. You may forget to remove listener.")
        }
        bridgeCallbacks.add(listener)
    }

    fun removeBridgeChangedListener(listener: IDebugBridgeChangeListener) {
        bridgeCallbacks.remove(listener)
    }

    override fun bridgeChanged(bridge: AndroidDebugBridge) {
        bridgeCallbacks.forEach {
            it.bridgeChanged(bridge)
        }
        Notifications.Bus.notify(
                Notification("bridgeChanged",
                        "Adb bridgeChanged",
                        "client:$bridge",
                        NotificationType.INFORMATION)
        )
        Logger.d(this, "bridgeChanged $bridge")
    }
}