package com.app.testlauncherapplication;

import androidx.fragment.app.Fragment;

public class AndroidLauncherActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() {
        return AndroidLauncherFragment.newInstance();
    }
}
