package jp.gcreate.plugins.adbfriendly;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import jp.gcreate.plugins.adbfriendly.ui.FunctionsForm;
import jp.gcreate.plugins.adbfriendly.util.Logger;
import jp.gcreate.plugins.adbfriendly.util.ShellCommand;

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

public class ShowDialogAction extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        // TODO: remove this for release time (this is for debugging)
        ShellCommand command = new ShellCommand();
        String path = command.executeCommand("env");
        Logger.d(this, "path is " + path);
        command = new ShellCommand();
        path = command.executeCommand("who");
        Logger.d(this, "source command done " + path);
        command = new ShellCommand();
        path = command.executeCommand("env");
        Logger.d(this, "after source path is :" + path);

        FunctionsForm form = new FunctionsForm(e);
        form.show();
        form.removeListenersOnExit();
    }
}
