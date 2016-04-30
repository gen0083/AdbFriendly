package jp.gcreate.plugins.adbfriendly.ui;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.ValidationInfo;
import jp.gcreate.plugins.adbfriendly.util.PluginConfig;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

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

public class SetAdbPathForm extends DialogWrapper {
    private JPanel     contentPane;
    private JTextField inputedAdbPath;
    private String adbPath;

    public SetAdbPathForm(Project project) {
        super(project);

        setTitle("Set Your ADB Path");
        adbPath = PluginConfig.INSTANCE.getAdbPath();

        init();
    }

    @Nullable
    @Override
    protected JComponent createCenterPanel() {
        return contentPane;
    }

    public String getAdbPath() {
        return adbPath;
    }

    @Override
    protected void doOKAction() {
        super.doOKAction();
    }

    @Nullable
    @Override
    protected ValidationInfo doValidate() {
        String path = inputedAdbPath.getText();
        if (path != null) {
            adbPath = path.trim();
            if (adbPath.length() == 0) {
                return new ValidationInfo("Input your adb path.", inputedAdbPath);
            }
        }
        return null;
    }
}
