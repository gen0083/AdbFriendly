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


enum class UserRotationDegree(val intValue: Int, val bind: String) {
    DEGREE_0  (0, "value:i:0"),
    DEGREE_90 (1, "value:i:1"),
    DEGREE_180(2, "value:i:2"),
    DEGREE_270(3, "value:i:3");

    companion object {
        fun fromIntValue(int: Int): UserRotationDegree {
            UserRotationDegree.values().forEach {
                if (it.intValue == int) return it
            }
            throw IllegalArgumentException("int value must to be 0 to 3")
        }
    }
}

