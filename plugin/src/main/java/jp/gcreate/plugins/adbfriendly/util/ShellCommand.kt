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

import com.intellij.execution.OutputListener
import com.intellij.execution.configurations.GeneralCommandLine
import com.intellij.execution.process.OSProcessHandler
import java.util.concurrent.ExecutionException
import java.util.concurrent.TimeUnit

class ShellCommand {
    @JvmOverloads
    fun executeCommand(command: String, timeoutSeconds: Long = 2): String {
        val outContent = StringBuilder()
        val errContent = StringBuilder()
        val splitedCommand: MutableList<String> = if(command.contains(" ")) command.split(" ").toMutableList() else mutableListOf(command)
        val commandLine = GeneralCommandLine(splitedCommand)
        val handler = OSProcessHandler(commandLine)

        try {
            handler.addProcessListener(OutputListener(outContent, errContent))

            handler.startNotify()
            handler.waitFor(TimeUnit.SECONDS.toMillis(timeoutSeconds))
            // check exit code to detect process finished or still running
            val exitCode = handler.process.exitValue();
        }catch (e: ExecutionException) {
            return outputErrorLog("error", e, outContent, errContent)
        }catch (e: InterruptedException) {
            return outputErrorLog("timeout", e, outContent, errContent)
        }catch (e: IllegalThreadStateException){
            return outputErrorLog("timeout", e, outContent, errContent)
        }
        return outContent.toString()
    }

    private fun outputErrorLog(cause: String, exception: Exception, output:StringBuilder, errorOutput: StringBuilder)
            :String {
        val str = "Command $cause: ${exception.message} \n" +
                  "${exception.stackTrace.joinToString("\n")}\n" +
                  " output:$output\n" +
                  " error:$errorOutput"
        Logger.e(this, str)
        return str
    }
}