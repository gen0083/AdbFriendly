ADB Friendly
===

ADB Friendly is a Android Studio plugin to provide some useful functions via ADB.

Now ver0.1, the plugin can rotate screen on connected devices or emulators only.

## Functions

### Screen rotation

Refer youtube

[![](http://img.youtube.com/vi/GfFcLmkfbTc/0.jpg)](https://www.youtube.com/watch?v=GfFcLmkfbTc)

## Installation

1. ~~Select Android Studio's menu `Preference > Plugins` then search `ADB Friendly` and install.~~(Coming soon)
1. Clone this repository then build and install from zip file. See develop section.

After installation, plugin added on toolbar at the last section.

If you don't find it, go to View menu on Android Studio and toggle toolbar.

## Usage

Click a ADB Friendly icon on Android Studio's toolbar then dialog is shown.

Select your device or emulator, and input rotating count in integer, finally click OK button.

Then target device's screen will rotate automatically.

If devices are not shown on the dialog even through you connect devices, try below please on your terminal.

```
adb devices
```

I'll fix it that the plugin reconnect to adb automatically, but now reconnect to adb by yourself sorry :(

## Development

`git clone https://github.com/gen0083/AdbFriendly.git`

My developing environment:

+ Project SDK: Intellij Platform Plugin SDK with Intellij 14.1.6 (Internal Java Platform 1.6)
+ Develop with IntelliJ 2016.1.1 with JDK 1.8
+ Check with Android Studio 2.0

### Execute

`./gradlew runIdea`

If you set alternativeIdePath (in build.gradle intellij section) then launch it.
Otherwise launch IntelliJ 14.1.6.

### Build zip file

`./gradlew buildPlugin`

Zip file located to `<project_root>/build/distributions/ADB Friendly-<version>.zip`.

## Issue

+ when first running, sometimes plugin can not connect to adb.
  At the time, control adb connection error. probably using adb location with createBridge may works fine.

  `Cannot start adb when AndroidDebugBridge is created without the location of adb.`

## License

```
ADB Friendly
Copyright 2016 gen0083

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```