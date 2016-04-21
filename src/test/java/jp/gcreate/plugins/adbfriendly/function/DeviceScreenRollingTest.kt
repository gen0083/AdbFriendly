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

package jp.gcreate.plugins.adbfriendly.function

import com.android.ddmlib.AndroidDebugBridge
import com.android.ddmlib.IDevice
import jp.gcreate.plugins.adbfriendly.adb.AdbAccelerometerRotation
import jp.gcreate.plugins.adbfriendly.adb.AdbUserRotation
import jp.gcreate.plugins.adbfriendly.funciton.DeviceScreenRolling
import jp.gcreate.plugins.adbfriendly.funciton.FunctionsCallback
import jp.gcreate.plugins.adbfriendly.funciton.FunctionsManager
import org.junit.AfterClass
import org.junit.Assume.assumeTrue
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import kotlin.test.assertTrue

/**
 * This test is expected to run under the one real device.
 */
class DeviceScreenRollingTest {
    companion object {
        @BeforeClass
        @JvmStatic
        fun setUpClass() {
            AndroidDebugBridge.initIfNeeded(false)
            AndroidDebugBridge.createBridge()
            wait@ for (i in 0..100) {
                TimeUnit.MILLISECONDS.sleep(10)
                if (AndroidDebugBridge.getBridge().devices.size > 0) {
                    println("device connected at $i times")
                    break@wait
                }
            }
            assumeTrue("this test must run under the connected android device and cau use adb.",
                       AndroidDebugBridge.getBridge().devices.size > 0)
        }

        @AfterClass
        @JvmStatic
        fun tearDownClass() {
            // revert settings to running test
            AndroidDebugBridge.terminate()
        }
    }

    lateinit var bridge: AndroidDebugBridge
    lateinit var device: IDevice
    lateinit var userRotation: AdbUserRotation
    lateinit var accelerometerRotation: AdbAccelerometerRotation
    lateinit var sut: DeviceScreenRolling
    var latch: CountDownLatch = CountDownLatch(1)
    val callback = object : FunctionsCallback {
        override fun onDone() {
            latch.countDown()
        }

        override fun onErrored() {
            latch.countDown()
        }

        override fun onCancelled() {
            latch.countDown()
        }
    }


    @Before
    fun setUp() {
        bridge = AndroidDebugBridge.getBridge()
        device = bridge.devices[0]
        userRotation = AdbUserRotation(device)
        accelerometerRotation = AdbAccelerometerRotation(device)
    }

    @Test(timeout = 20000L)
    fun rolling5Times() {
        latch = CountDownLatch(1)
        sut = DeviceScreenRolling(device, callback, 5)
        FunctionsManager.startFunction(sut)
        latch.await()
        assert(true)
    }

    @Test(timeout = 10000L)
    fun rollingCancel() {
        latch = CountDownLatch(1)
        sut = DeviceScreenRolling(device, callback, 10)
        FunctionsManager.startFunction(sut)
        Thread {
            TimeUnit.SECONDS.sleep(5)
            FunctionsManager.cancel()
        }.start()
        latch.await()
        assert(true)
    }

    @Test
    fun `don't Run Parallel`() {
        latch = CountDownLatch(1)
        sut = DeviceScreenRolling(device, callback, 5)
        val sut2 = DeviceScreenRolling(device, callback, 5)
        var actual: DeviceScreenRolling? = null
        FunctionsManager.startFunction(sut)
        Thread{
            TimeUnit.SECONDS.sleep(5)
            FunctionsManager.startFunction(sut2)
            actual = FunctionsManager.getRunningFunctionOrNull() as DeviceScreenRolling?
        }.start()
        latch.await()
        assertTrue(sut.equals(actual))
    }
}