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
import com.android.ddmlib.IDevice
import org.hamcrest.CoreMatchers.*
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.isOneOf
import org.junit.AfterClass
import org.junit.Assume.assumeTrue
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test
import java.util.concurrent.TimeUnit


class AdbUserRotationTest {
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
            val sut = AdbUserRotation(AndroidDebugBridge.getBridge().devices[0])
            sut.setUserRotationDegree(UserRotationDegree.DEGREE_0)
            val degree = sut.getUserRotationDegree()
            println("test finished set user_rotation to ${degree.name}")
            AndroidDebugBridge.terminate()
        }
    }

    lateinit var device: IDevice
    lateinit var sut: AdbUserRotation

    @Before
    fun setUp() {
        device = AndroidDebugBridge.getBridge().devices[0]
        sut = AdbUserRotation(device)
    }

    @Test
    fun getUserRotationDegree() {
        val degree = sut.getUserRotationDegree()
        assertThat(degree, isOneOf(UserRotationDegree.DEGREE_0,
                                   UserRotationDegree.DEGREE_90,
                                   UserRotationDegree.DEGREE_180,
                                   UserRotationDegree.DEGREE_270))
    }

    @Test
    fun isExistTest() {
        val actual = sut.isExist()
        assertThat(actual, `is`(true))
    }

    @Test
    fun setUserRotation180() {
        assertThat(sut.setUserRotationDegree(UserRotationDegree.DEGREE_180), `is`(true))
        val actual = sut.getUserRotationDegree()
        assertThat(actual, `is`(UserRotationDegree.DEGREE_180))
    }
}