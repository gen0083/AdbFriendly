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
import com.intellij.openapi.application.ApplicationListener
import com.intellij.openapi.application.ApplicationManager
import jp.gcreate.plugins.adbfriendly.util.Logger
import java.util.*


object AdbConnector : IClientChangeListener, IDeviceChangeListener, IDebugBridgeChangeListener {
    private var initialized = false
    private val clientCallbacks: ArrayList<IClientChangeListener> = arrayListOf()
    private val deviceCallbacks: ArrayList<IDeviceChangeListener> = arrayListOf()
    private val bridgeCallbacks: ArrayList<IDebugBridgeChangeListener> = arrayListOf()

    init {
        connectAdb()
        ApplicationManager.getApplication().addApplicationListener(object: ApplicationListener{
            override fun applicationExiting() {
                disconnectAdb()
            }

            override fun beforeWriteActionStart(action: Any?) {
                // no-op
            }

            override fun writeActionStarted(action: Any?) {
                // no-op
            }

            override fun writeActionFinished(action: Any?) {
                // no-op
            }

            override fun canExitApplication(): Boolean {
                // no-op
                return true
            }

        })
    }

    fun connectAdb() {
        if (initialized) {
            disconnectAdb()
        }
        initialized = true
        AndroidDebugBridge.initIfNeeded(false)
        AndroidDebugBridge.createBridge()
        AndroidDebugBridge.addClientChangeListener(this)
        AndroidDebugBridge.addDebugBridgeChangeListener(this)
        AndroidDebugBridge.addDeviceChangeListener(this)
    }

    fun connectAdb(path: String) {
        if (initialized) {
            disconnectAdb()
        }
        initialized = true
        AndroidDebugBridge.initIfNeeded(false)
        AndroidDebugBridge.createBridge(path, false)
        AndroidDebugBridge.addClientChangeListener(this)
        AndroidDebugBridge.addDebugBridgeChangeListener(this)
        AndroidDebugBridge.addDeviceChangeListener(this)
    }

    fun disconnectAdb() {
        AndroidDebugBridge.removeClientChangeListener(this)
        clientCallbacks.clear()
        AndroidDebugBridge.removeDebugBridgeChangeListener(this)
        bridgeCallbacks.clear()
        AndroidDebugBridge.removeDeviceChangeListener(this)
        deviceCallbacks.clear()
        AndroidDebugBridge.terminate()
    }

    fun isAdbConnected(): Boolean {
        return getAdbBridge().isConnected
    }

    fun getAdbBridge(): AndroidDebugBridge {
        return AndroidDebugBridge.getBridge()
    }

    fun getDevices(): Array<IDevice> {
        return getAdbBridge().devices
    }

    fun addClientChangeListener(listener: IClientChangeListener) {
        if (clientCallbacks.contains(listener)) {
            throw IllegalStateException("ClientChangeListener [$listener] is already added. You may forget to remove listener.")
        }
        clientCallbacks.add(listener)
    }

    @Suppress("unused")
    fun removeClientChangeListener(listener: IClientChangeListener) {
        clientCallbacks.remove(listener)
    }

    override fun clientChanged(client: Client, changeMask: Int) {
        clientCallbacks.forEach {
            it.clientChanged(client, changeMask)
        }
        Logger.d(this, "clientChanged $client $changeMask")
    }

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
        Logger.d(this, "deviceChanged $device")
    }

    override fun deviceConnected(device: IDevice) {
        deviceCallbacks.forEach {
            it.deviceConnected(device)
        }
        Logger.d(this, "deviceConnected $device")
    }

    override fun deviceDisconnected(device: IDevice) {
        deviceCallbacks.forEach {
            it.deviceDisconnected(device)
        }
        Logger.d(this, "deviceDisconnected $device")
    }

    /**
     * If AndroidDebugBridge is changed then notify this callback.
     */
    fun addBridgeChangedListener(listener: IDebugBridgeChangeListener) {
        if (bridgeCallbacks.contains(listener)) {
            throw IllegalStateException("DebugBridgeChangedListener [$listener}] is already added. You may forget to remove listener.")
        }
        bridgeCallbacks.add(listener)
    }

    @Suppress("unused")
    fun removeBridgeChangedListener(listener: IDebugBridgeChangeListener) {
        bridgeCallbacks.remove(listener)
    }

    override fun bridgeChanged(bridge: AndroidDebugBridge) {
        bridgeCallbacks.forEach {
            it.bridgeChanged(bridge)
        }
        Logger.d(this, "bridgeChanged $bridge")
    }
}