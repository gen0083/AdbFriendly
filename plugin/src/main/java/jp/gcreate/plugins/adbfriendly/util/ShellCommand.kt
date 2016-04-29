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

import java.util.concurrent.TimeUnit

class ShellCommand {
    var output: String = ""
    var succeed = false

    fun executeCommand(command: String, timeout: Long = TimeUnit.SECONDS.toMillis(2)): String {
        val timelimit = System.currentTimeMillis() + timeout
        try {
            waiting@while(System.currentTimeMillis() < timelimit) {
                val process = Runtime.getRuntime().exec(command)
                process.waitFor()
                val input = process.inputStream
                input.bufferedReader().useLines {
                    it.forEach {
                        var str = it
                        output += str
                        Logger.d(this, "readLine :$str")
                    }
                }
                succeed = true
                input.close()
                break@waiting
            }
            if (!succeed) {
                Logger.e(this, "executeComand timeout:$output")
                return "Command timeout\n" + output
            }
        }catch (e: Exception) {
            Logger.e(this, "executeCommand erred:${e.message}")
            return "Command error:" + e.message + "\n$output"
        }
        return output
    }
}