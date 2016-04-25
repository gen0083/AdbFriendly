package jp.gcreate.plugins.adbfriendly.ui;

import com.android.ddmlib.AndroidDebugBridge;
import com.android.ddmlib.IDevice;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.ValidationInfo;
import jp.gcreate.plugins.adbfriendly.adb.AdbConnector;
import jp.gcreate.plugins.adbfriendly.funciton.DeviceScreenRolling;
import jp.gcreate.plugins.adbfriendly.funciton.FriendlyFunctions;
import jp.gcreate.plugins.adbfriendly.funciton.FunctionsCallback;
import jp.gcreate.plugins.adbfriendly.funciton.FunctionsManager;
import jp.gcreate.plugins.adbfriendly.util.Logger;
import jp.gcreate.plugins.adbfriendly.util.PluginConfig;

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

public class FunctionsForm extends DialogWrapper
        implements FunctionsCallback, AndroidDebugBridge.IDeviceChangeListener {
    private JTextField       rollingCount;
    private JCheckBox        showProgressCheckBox;
    private JList            devicesList;
    private JLabel           notifyDevicesNotFound;
    private JPanel           menuWindow;
    private JLabel           notifyAlreadyRunning;
    private DefaultListModel connectedDevicesModel;

    public FunctionsForm(AnActionEvent event) {
        super(event.getProject());

        setTitle("ADB Friendly");

        connectedDevicesModel = new DefaultListModel();
        devicesList.setCellRenderer(new DevicesListRenderer());

        setListenerOnLaunch();
        bindDevicesToList();
        checkRunningTaskExist();

        init();
    }

    private void setListenerOnLaunch(){
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
        notifyDevicesNotFound.setVisible(devices.length == 0);
        notifyDevicesNotFound.invalidate();
        int selected = -1;
        int i = 0;
        String previousSerial = PluginConfig.INSTANCE.getDeviceSerial();
        connectedDevicesModel.clear();
        for (IDevice device : devices) {
            if (device.getSerialNumber().equals(previousSerial)) {
                selected = i;
            }
            connectedDevicesModel.addElement(device);
            i++;
        }
        devicesList.setModel(connectedDevicesModel);
        devicesList.setSelectedIndex(selected);
        devicesList.invalidate();
    }

    private void checkRunningTaskExist() {
        FriendlyFunctions currentFunction = FunctionsManager.INSTANCE.getRunningFunctionOrNull();
        boolean isRunning = currentFunction != null;
        // If running functions now then set disable buttons which to run functions.
        setOKActionEnabled(!isRunning);
        notifyAlreadyRunning.setVisible(isRunning);
        notifyAlreadyRunning.invalidate();
    }

    @Override
    protected void doOKAction() {
        int index = devicesList.getSelectedIndex();
        IDevice device = (IDevice) connectedDevicesModel.getElementAt(index);
        int count = Integer.parseInt(rollingCount.getText());
        if (device != null && device.isOnline()) {
            FunctionsManager.INSTANCE.startFunction(
                    new DeviceScreenRolling(device,
                            FunctionsManager.INSTANCE,
                            count,
                            showProgressCheckBox.isSelected()
                            ));
        }

        dispose();
    }

    @Override
    protected ValidationInfo doValidate() {
        Logger.d(this, "validation start");
        if (devicesList.getSelectedIndex() == -1) {
            return new ValidationInfo("Select a target device.", devicesList);
        }else {
            IDevice device = (IDevice) connectedDevicesModel.getElementAt(devicesList.getSelectedIndex());
            PluginConfig.INSTANCE.setDeviceSerial(device.getSerialNumber());
        }
        try {
            int count = Integer.parseInt(rollingCount.getText());
            if(count <= 0) {
                return new ValidationInfo("Rotating count must be beggar than 0.", rollingCount);
            }
        }catch (NumberFormatException e) {
            return new ValidationInfo("Rotating count must be digit.", rollingCount);
        }
        PluginConfig.INSTANCE.save();
        return null;
    }

    /**
     * When device status changed, these callbacks are called.
     * @param device
     */
    @Override
    public void deviceConnected(IDevice device) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                bindDevicesToList();
            }
        });
    }

    @Override
    public void deviceDisconnected(IDevice device) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                bindDevicesToList();
            }
        });
    }

    @Override
    public void deviceChanged(IDevice device, int changeMask) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                bindDevicesToList();
            }
        });
    }

    /**
     * If functions are finished then these callbacks are called from FunctionsManager.
     * The callbacks notify that FunctionsManager can start a next function.
     */
    @Override
    public void onDone() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                checkRunningTaskExist();
            }
        });
    }

    @Override
    public void onErrored() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                checkRunningTaskExist();
            }
        });
    }

    @Override
    public void onCancelled() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                checkRunningTaskExist();
            }
        });
    }

    public class DoFunctionsAction{
        private FriendlyFunctions functions;

        public DoFunctionsAction(FriendlyFunctions functions) {
            this.functions = functions;
        }
    }

}
