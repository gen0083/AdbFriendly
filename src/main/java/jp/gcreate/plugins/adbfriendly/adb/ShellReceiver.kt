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

import com.android.ddmlib.MultiLineReceiver
import jp.gcreate.plugins.adbfriendly.util.Logger


class ShellReceiver() : MultiLineReceiver(){
    var cancel: Boolean = false

    override fun processNewLines(lines: Array<out String>?) {
        lines?.forEach {
            Logger.d(this, it.toString())
        }
    }

    override fun isCancelled(): Boolean {
        Logger.d(this, "isCancelled called now:$cancel")
        return cancel
    }
}