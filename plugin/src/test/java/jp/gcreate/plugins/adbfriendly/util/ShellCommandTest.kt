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

import org.hamcrest.CoreMatchers.*
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test

class ShellCommandTest {
    @Test
    fun execWhichAdb() {
        val command = ShellCommand()
        val result = command.executeCommand("which adb")
        println(result)
        assertThat(result, notNullValue())
        assertThat(result, instanceOf(String::class.java))
    }

    @Test
    fun execPing() {
        val command = ShellCommand()
        val result = command.executeCommand("ping -c 4 www.yahoo.co.jp")
        println(result)
        assertThat(result, notNullValue())
        assertThat(result, instanceOf(String::class.java))
    }

    @Test
    fun checkEnv() {
        val command = ShellCommand()
        val result = command.executeCommand("env")
        println(result)
        assertThat(result, notNullValue())
        assertThat(result, instanceOf(String::class.java))
        val com = ShellCommand()
        val ac = com.executeCommand("who")
        println("who is :$ac")
    }
}