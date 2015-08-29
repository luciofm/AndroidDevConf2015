package com.luciofm.presentation.droiconit.activity;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;

import com.luciofm.presentation.droiconit.fragment.BaseFragment;

import java.util.ArrayList;

public abstract class BaseActivity extends Activity {

    public static final int BUTTON_NEXT = 109;
    public static final int BUTTON_PREV = 104;

    int index = 0;
    View decorView;
    ArrayList<Class<? extends BaseFragment>> fragments;

    abstract ArrayList<Class<? extends BaseFragment>> getFragmentsList();
    abstract int getContentViewId();
    abstract int getFragmentContainerId();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentViewId());
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        decorView = getWindow().getDecorView();

        fragments = getFragmentsList();
        if (savedInstanceState == null)
            nextFragment(false);

        getFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                index = getFragmentManager().getBackStackEntryCount() + 1;
            }
        });
    }

    // This snippet hides the system bars.
    public void hideSystemUI() {
        // Set the IMMERSIVE flag.
        // Set the content to appear under the system bars so that the content
        // doesn't resize when the system bars hide and show.
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                | View.SYSTEM_UI_FLAG_IMMERSIVE);
    }

    // This snippet shows the system bars. It does this by removing all the flags
    // except for the ones that make the content appear under the system bars.
    public void showSystemUI() {
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    }

    public boolean shouldSetSystemUiVisibility() {
        return true;
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus && shouldSetSystemUiVisibility()) {
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);}
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt("INDEX", index);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        index = savedInstanceState.getInt("INDEX", 0);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode != 58 && event.getScanCode() != 100)
            Log.d("IFICAN", "onKeyDown: " + keyCode + " - event: " + event.getScanCode());
        BaseFragment fragment = (BaseFragment) getFragmentManager().findFragmentByTag("current");
        int scanCode = event.getScanCode();
        switch (scanCode) {
            case BUTTON_NEXT:
            case 106:
            case 28:
            case 229:
            case 0x74:
                if (fragment != null)
                    fragment.onNextPressed();
                else
                    onBackPressed();
                return true;
            case BUTTON_PREV:
            case 105:
            case 0x79:
            case 57:
                if (fragment != null)
                    fragment.onPrevPressed();
                else
                    onBackPressed();
                return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    public void nextFragment() {
        nextFragment(null);
    }

    public void nextFragment(boolean backstack) {
        nextFragment(backstack, null);
    }

    public void nextFragment(Bundle args) {
        nextFragment(true, args);
    }

    public void nextFragment(boolean backStack, Bundle args) {
        if (fragments == null || index == fragments.size())
            return;
        Class<? extends BaseFragment> clazz = fragments.get(index++);
        try {
            BaseFragment f = clazz.newInstance();
            if (args != null)
                f.setArguments(args);
            FragmentManager fm = getFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(getFragmentContainerId(), f, "current")
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            if (backStack)
                ft.addToBackStack(null);
            ft.commit();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
