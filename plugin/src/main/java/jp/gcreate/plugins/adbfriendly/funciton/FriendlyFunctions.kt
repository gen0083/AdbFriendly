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

import com.android.ddmlib.IDevice
import jp.gcreate.plugins.adbfriendly.util.Logger
import java.util.*


abstract class FriendlyFunctions(device: IDevice, callback: FunctionsCallback) : Runnable {
    lateinit var device: IDevice
    lateinit var callback: FunctionsCallback
    var isCancelled = false

    init {
        this.device = device
        this.callback = callback
    }

    open fun onSuccess(function: FriendlyFunctions) {
        Logger.d(this, "Function[$function] succeed")
        callback.onDone()
    }

    open fun onError(e: Exception, outputs: ArrayList<String>) {
        Logger.e(this, "Error: ${e.cause} \n" +
                       "${e.printStackTrace()}\n" +
                       "outputs:${outputs.joinToString("\n")}")
        callback.onErred()
    }

    open fun onCancel(function: FriendlyFunctions) {
        Logger.d(this, "Function[$function] cancelled.")
        callback.onCancelled()
    }
}