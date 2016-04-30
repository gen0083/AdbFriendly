package jp.gcreate.plugins.adbfriendly.ui;

import com.android.ddmlib.IDevice;
import jp.gcreate.plugins.adbfriendly.util.Logger;

import javax.swing.*;
import java.awt.*;

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

class DevicesListRenderer extends JLabel implements ListCellRenderer {
    DevicesListRenderer() {
        super();
        setOpaque(true);
    }

    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected,
                                                  boolean cellHasFocus) {
        if (value instanceof IDevice) {
            IDevice device = (IDevice) value;
            String  name   = (device.isEmulator()) ? device.getAvdName() : device.getName();
            setText(name);
        } else {
            Logger.d(this, "This renderer implements for IDevice but called other models.");
            setText(" something else ");
        }

        // set item color
        if (isSelected) {
            setBackground(list.getSelectionBackground());
            setForeground(list.getSelectionForeground());
        } else {
            setBackground(list.getBackground());
            setForeground(list.getForeground());
        }

        return this;
    }
}
