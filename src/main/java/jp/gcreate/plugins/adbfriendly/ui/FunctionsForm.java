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
import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.ui.DialogEarthquakeShaker;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.ValidationInfo;
import jp.gcreate.plugins.adbfriendly.adb.AdbConnector;
import jp.gcreate.plugins.adbfriendly.funciton.FriendlyFunctions;
import jp.gcreate.plugins.adbfriendly.funciton.FunctionsCallback;
import jp.gcreate.plugins.adbfriendly.funciton.FunctionsManager;
import jp.gcreate.plugins.adbfriendly.util.Logger;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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

        setListenerOnLaunch();

        connectedDevicesModel = new DefaultListModel();
        buttonRolling.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Logger.d(this, "button rolling clicked.");

                ValidationInfo info = doValidate();
                if (info != null) {
                    DialogEarthquakeShaker.shake((JDialog)getPeer().getWindow());
//                    startTrackingValidation();
                    initValidation();
                    return;
                }
                Notifications.Bus.notify(new Notification("test", "do action", "pass the validation", NotificationType.INFORMATION));
            }
        });
        devicesList.setCellRenderer(new DevicesListRenderer());
        init();
        bindDevicesToList();
    }

    public void setListenerOnLaunch(){
        AdbConnector.INSTANCE.addDeviceChangeListener(this);
        FunctionsManager.INSTANCE.addFunctionsCallback(this);
    }

    public void removeListenersOnExit() {
        AdbConnector.INSTANCE.removeDeviceChangedListener(this);
        FunctionsManager.INSTANCE.removeFunctionsCallbacks(this);
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
        FriendlyFunctions currentFunction = FunctionsManager.INSTANCE.getRunningFunctionOrNull();
        boolean running = currentFunction != null;
        // If running functions now then set disable buttons which to run functions.
        buttonRolling.setEnabled(running);
    }

    @Override
    protected ValidationInfo doValidate() {
        Logger.d(this, "validation start");
        if (devicesList.getSelectedIndex() == -1) {
            return new ValidationInfo("Select a target device.", devicesList);
        }
        return null;
    }

    /**
     * When device status changed, these callbacks are called.
     * @param device
     */
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

    /**
     * If functions are finished then these callbacks are called from FunctionsManager.
     * The callbacks notify that FunctionsManager can start a next function.
     */
    @Override
    public void onDone() {
        checkRunnningTaskExisit();
    }

    @Override
    public void onErrored() {
        checkRunnningTaskExisit();
    }

    @Override
    public void onCancelled() {
        checkRunnningTaskExisit();
    }

    public class DoFunctionsAction{
        private FriendlyFunctions functions;

        public DoFunctionsAction(FriendlyFunctions functions) {
            this.functions = functions;
        }
    }

}
