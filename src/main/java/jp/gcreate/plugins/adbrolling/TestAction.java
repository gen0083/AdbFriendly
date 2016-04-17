package jp.gcreate.plugins.adbrolling;

import com.android.ddmlib.*;
import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import jp.gcreate.plugins.adbrolling.adb.AdbConnector;
import jp.gcreate.plugins.adbrolling.adb.ShellReceiver;

import java.io.IOException;

import static sun.audio.AudioDevice.device;

/*
 * adb-rolling
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

public class TestAction extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        Project project = e.getProject();
        IDevice devices[] = AdbConnector.INSTANCE.getDevices();
        IDevice device = devices[0];
        try {
            device.executeShellCommand("dumpsys activity", new ShellReceiver());
        } catch (TimeoutException e1) {
            e1.printStackTrace();
        } catch (AdbCommandRejectedException e1) {
            e1.printStackTrace();
        } catch (ShellCommandUnresponsiveException e1) {
            e1.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        Notifications.Bus.notify(
                new Notification("test", "test notification", "this is test for my plugin " + device.getName(), NotificationType.INFORMATION)
        );
    }
}
