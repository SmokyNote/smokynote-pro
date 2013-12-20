#!/usr/bin/expect -f

# Script installs Android SDK components
# For a full list, run `android list sdk -a --extended`
set timeout -1;
spawn android update sdk --filter tools,platform-tools,build-tools-19.0.1,$env(ANDROID_SDKS),extra-android-support --no-ui --force
expect { 
    "Do you accept the license" { exp_send "y\r" ; exp_continue }
    eof
}
