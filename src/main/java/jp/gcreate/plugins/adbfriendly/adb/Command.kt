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

import com.android.ddmlib.IDevice
import com.android.ddmlib.IShellOutputReceiver
import com.android.ddmlib.TimeoutException
import jp.gcreate.plugins.adbfriendly.Util.Logger
import java.util.*
import java.util.concurrent.TimeUnit

class Command(val device: IDevice,
              val command: String,
              val timeout: Long = System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(2))
: IShellOutputReceiver {
    private var outputs: ArrayList<String> = arrayListOf()
    private var finished = false

    fun execute(): ArrayList<String> {
        if (finished) throw IllegalStateException("This command already used. Use new Command instance and execute.")
        device.executeShellCommand(command, this)
        while (!finished) {
            if (System.currentTimeMillis() > timeout) {
                Logger.e(this, "Command timeout ${outputs.joinToString("\n")}")
                throw TimeoutException("Command timeout.")
            }
        }
        return outputs
    }

    override fun addOutput(data: ByteArray?, offset: Int, length: Int) {
        val str = java.lang.String(data, offset, length).toString()
        outputs.add(str)
    }

    override fun flush() {
        finished = true
    }

    override fun isCancelled(): Boolean {
        return false
    }
}