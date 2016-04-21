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

import com.android.ddmlib.AndroidDebugBridge
import com.android.ddmlib.AndroidDebugBridge.*
import com.android.ddmlib.Client
import com.android.ddmlib.IDevice
import jp.gcreate.plugins.adbfriendly.util.Logger
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.Future
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock


object FunctionsManager : IDeviceChangeListener, IClientChangeListener, IDebugBridgeChangeListener, FunctionsCallback {
    private val executor: ExecutorService = Executors.newSingleThreadExecutor()
    private lateinit var currentTask: Future<*>
    private var currentFunction: FriendlyFunctions? = null
    private val lock = ReentrantLock()

    fun startFunction(function: FriendlyFunctions) {
        lock.withLock {
            if (currentFunction != null) {
                Logger.d(this, "Before function[${currentFunction}] is running. So we can not start $function}")
                return
            }
            currentFunction = function
            currentTask = executor.submit(function)
        }
    }

    fun cancel() {
        if(currentFunction == null) return
        lock.withLock {
            currentTask.cancel(true)
            currentFunction?.isCancelled = true
            currentFunction = null
        }
    }

    fun getRunningFunctionOrNull(): FriendlyFunctions? {
        lock.withLock {
            if(currentFunction == null) return null
            return currentFunction
        }
    }

    override fun onDone() {
        lock.withLock {
            currentFunction = null
        }
    }

    override fun onErrored() {
        lock.withLock {
            currentFunction = null
        }
    }

    override fun onCancelled() {
        // no-op because calling source is this manager.
    }

    override fun deviceChanged(device: IDevice, changeMask: Int) {
        Logger.d(this, "deviceChanged $device changeMask:$changeMask}")
        lock.withLock {
            currentFunction?.device = device
        }
    }

    override fun deviceConnected(device: IDevice) {
        Logger.d(this, "deviceConnected $device")
    }

    override fun deviceDisconnected(device: IDevice) {
        Logger.d(this, "deviceDisconnected $device")
    }

    override fun clientChanged(client: Client, changeMask: Int) {
        // maybe not used
        Logger.d(this, "clientChanged $client mask:$changeMask")
    }

    override fun bridgeChanged(bridge: AndroidDebugBridge) {
        // maybe not used
        Logger.d(this, "bridgeChanged $bridge")
    }

}