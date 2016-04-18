package jp.gcreate.plugins.adbrolling

import com.android.ddmlib.AndroidDebugBridge
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.util.concurrent.TimeUnit
import kotlin.test.assertEquals

/*
 * adb-rolling
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

class AdbDebugBridgeTest {
    lateinit var bridge: AndroidDebugBridge

    @Before
    fun setUp() {
        AndroidDebugBridge.initIfNeeded(false)
        bridge = AndroidDebugBridge.createBridge()
        wait@ for(i in 0..100){
            TimeUnit.MILLISECONDS.sleep(10)
            if(bridge.devices.size > 0){
                println("device connected at $i times")
                break@wait
            }
        }
        if(bridge.devices.size == 0){
            throw RuntimeException("check your device connected?")
        }
    }

    @After
    fun tearDown() {
        AndroidDebugBridge.terminate()
    }

    @Test
    fun getDeviceName() {
        val devices = bridge.devices
        assertEquals(1, devices.size)
        println("device count:${devices.size}")
        devices.forEach {
            println("device name:${it.name}")
        }
    }

}
