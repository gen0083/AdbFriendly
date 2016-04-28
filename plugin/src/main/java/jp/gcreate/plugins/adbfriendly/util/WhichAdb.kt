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

class WhichAdb {
    var output = ""

    fun getAdbPath(): String {
        try {
            val processBuilder = ProcessBuilder("/bin/sh", "-c", "which", "adb")
            val process = processBuilder.start()
            process.waitFor()
            val input = process.inputStream
            input.bufferedReader().useLines {
                it.forEach {
                    var str = it
                    output += str
                    Logger.d(this, "readLine: $str")
                }
            }
            input.close()
            process.destroy()
        }catch (e: Exception) {
            Logger.e(this, "executeCommand erred:$output")
            return "Command error\n" + output
        }
        return output
    }
}