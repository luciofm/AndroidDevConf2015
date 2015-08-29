package com.luciofm.presentation.droiconit.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.luciofm.presentation.droiconit.activity.BaseActivity;

import butterknife.ButterKnife;

public abstract class BaseFragment extends Fragment {

    protected int currentStep = 1;

    public abstract int getLayout();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        currentStep = 1;
        return inflater.inflate(getLayout(), parent, false);
    }

    @Override
    public void onDestroyView() {
        ButterKnife.reset(this);
        super.onDestroyView();
    }

    public void onNextPressed() {
        ((BaseActivity) getActivity()).nextFragment();
    }

    public void onPrevPressed() {
        getActivity().onBackPressed();
    }
}
