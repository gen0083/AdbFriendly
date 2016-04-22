package jp.gcreate.plugins.adbfriendly.ui;

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

import com.android.ddmlib.AndroidDebugBridge;
import com.android.ddmlib.IDevice;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.ui.DialogWrapper;
import jp.gcreate.plugins.adbfriendly.adb.AdbConnector;
import jp.gcreate.plugins.adbfriendly.funciton.FriendlyFunctions;
import jp.gcreate.plugins.adbfriendly.funciton.FunctionsCallback;

import javax.swing.*;

public class FunctionsForm extends DialogWrapper
        implements FunctionsCallback, AndroidDebugBridge.IDeviceChangeListener {
    private JTextField       rollingCount;
    private JButton          buttonRolling;
    private JButton          buttonClose;
    private JCheckBox        showProgressCheckBox;
    private JLabel           invalidCount;
    private JList            devicesList;
    private JLabel           devicesNotFound;
    private JPanel menuWindow;
    private DefaultListModel connectedDevicesModel;

    public FunctionsForm(AnActionEvent event) {
        super(event.getProject());
        setTitle("ADB Friendly");
        // constructor
        connectedDevicesModel = new DefaultListModel();
        AdbConnector.INSTANCE.addDeviceChangeListener(this);
        buttonRolling.setAction(new DialogWrapperExitAction("hoge", DialogWrapper.CLOSE_EXIT_CODE));
        devicesList.setCellRenderer(new DevicesListRenderer());
        init();
        bindDevicesToList();
    }


    @Override
    protected JComponent createCenterPanel() {
        return menuWindow;
    }

    private void bindDevicesToList() {
        IDevice devices[] = AdbConnector.INSTANCE.getDevices();
        devicesNotFound.setVisible(devices.length == 0);
        devicesNotFound.invalidate();
        connectedDevicesModel.clear();
        for (IDevice device : devices) {
            connectedDevicesModel.addElement(device);
        }
//        devicesList.setModel(new DevicesListModel(devices));
        devicesList.setModel(connectedDevicesModel);
        devicesList.invalidate();
    }

    private void checkRunnningTaskExisit() {

    }

    @Override
    public void deviceConnected(IDevice device) {
        bindDevicesToList();
    }

    @Override
    public void deviceDisconnected(IDevice device) {
        bindDevicesToList();
    }

    @Override
    public void deviceChanged(IDevice device, int changeMask) {
        bindDevicesToList();
    }

    @Override
    public void onDone() {

    }

    @Override
    public void onErrored() {

    }

    @Override
    public void onCancelled() {

    }

    public class DoFunctionsAction{
        private FriendlyFunctions functions;

        public DoFunctionsAction(FriendlyFunctions functions) {
            this.functions = functions;
        }
    }

}
