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

package jp.gcreate.plugins.adbfriendly

import org.hamcrest.CoreMatchers.*
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test

class ContainsTest {
    val target = "column=name value=1 test=test"

    @Test
    fun getIntFromValue(){
        assertThat(target.contains("value"), `is`(true))
    }

    @Test
    fun regex(){
        val regex = Regex("value=(\\d)")
        val result = regex.find(target)
        assertThat(result, notNullValue())
        assertThat(result?.groups?.size, `is`(2))
        assertThat(result?.groups?.get(1)?.value, `is`("1"))
    }
}