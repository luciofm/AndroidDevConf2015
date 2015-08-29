package com.luciofm.presentation.droiconit.activity;

import android.app.Activity;
import android.app.KeyguardManager;
import android.content.Intent;
import android.os.Bundle;
import android.os.PowerManager;
import android.view.KeyEvent;

import com.luciofm.presentation.droiconit.R;
import com.luciofm.presentation.droiconit.fragment.BaseFragment;

import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.os.PowerManager.ACQUIRE_CAUSES_WAKEUP;
import static android.os.PowerManager.FULL_WAKE_LOCK;
import static android.os.PowerManager.ON_AFTER_RELEASE;
import static android.view.WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED;

public class MainActivity extends Activity {
    public static final int BUTTON_NEXT = 109;
    public static final int BUTTON_PREV = 104;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        ButterKnife.inject(this);
    }

    @Override
    protected void onStart() {
        // Unlock device on Debug/QA builds...
        unlockDevice(this);
        super.onStart();
    }

    public void unlockDevice(Activity activity) {
        KeyguardManager keyguardManager = (KeyguardManager) activity.getSystemService(KEYGUARD_SERVICE);
        final KeyguardManager.KeyguardLock keyguardLock = keyguardManager.newKeyguardLock("Unlock!");
        keyguardLock.disableKeyguard();

        activity.getWindow().addFlags(FLAG_SHOW_WHEN_LOCKED);

        PowerManager powerManager = (PowerManager) activity.getSystemService(POWER_SERVICE);
        PowerManager.WakeLock wakeLock = powerManager.newWakeLock(FULL_WAKE_LOCK | ACQUIRE_CAUSES_WAKEUP | ON_AFTER_RELEASE, "Wakeup!");
        wakeLock.acquire();
        wakeLock.release();
    }

    @OnClick(R.id.button)
    public void onButtonClick() {
        startActivity(new Intent(this, FirstActivity.class));
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        BaseFragment fragment = (BaseFragment) getFragmentManager().findFragmentByTag("current");
        int scanCode = event.getScanCode();
        switch (scanCode) {
            case BUTTON_NEXT:
            case 28:
            case 229:
            case 0x74:
                onButtonClick();
                return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}