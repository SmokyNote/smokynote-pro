#!/usr/bin/expect -f

# Script installs Android SDK components
# For a full list, run `android list sdk -a --extended`
set timeout -1;
spawn sdkmanager "tools" "platform-tools" "build-tools;25.0.3" "extras;android;m2repository" $env(ANDROID_PLATFORM) $env(ANDROID_IMAGE)
expect {
    "Accept?" { exp_send "y\r" ; exp_continue }
    eof
}
