package com.luciofm.presentation.droiconit.activity;

import com.luciofm.presentation.droiconit.R;
import com.luciofm.presentation.droiconit.fragment.ActivityTransitionFragment;
import com.luciofm.presentation.droiconit.fragment.AwesomeFragment;
import com.luciofm.presentation.droiconit.fragment.BaseFragment;
import com.luciofm.presentation.droiconit.fragment.HelloFragment;
import com.luciofm.presentation.droiconit.fragment.IntroFragment;
import com.luciofm.presentation.droiconit.fragment.LayoutTransitionFragment;
import com.luciofm.presentation.droiconit.fragment.MoreTransitionManagerFragment;
import com.luciofm.presentation.droiconit.fragment.MoviesFragment;
import com.luciofm.presentation.droiconit.fragment.MyselfFragment;
import com.luciofm.presentation.droiconit.fragment.QuesionsFragment;
import com.luciofm.presentation.droiconit.fragment.SceneFragment;
import com.luciofm.presentation.droiconit.fragment.SlackFragment;
import com.luciofm.presentation.droiconit.fragment.TransitionManagerFragment;
import com.luciofm.presentation.droiconit.fragment.WorkFragment;

import java.util.ArrayList;

public class FirstActivity extends BaseActivity {

    @Override
    ArrayList<Class<? extends BaseFragment>> getFragmentsList() {
        ArrayList<Class<? extends BaseFragment>> fragments = new ArrayList<>();
        fragments.add(HelloFragment.class);
        fragments.add(MyselfFragment.class);
        fragments.add(WorkFragment.class);
        fragments.add(IntroFragment.class);
        fragments.add(LayoutTransitionFragment.class);
        fragments.add(TransitionManagerFragment.class);
        fragments.add(MoreTransitionManagerFragment.class);
        fragments.add(SceneFragment.class);
        fragments.add(ActivityTransitionFragment.class);
        fragments.add(MoviesFragment.class);
        fragments.add(AwesomeFragment.class);
        fragments.add(QuesionsFragment.class);
        fragments.add(SlackFragment.class);
        return fragments;
    }

    @Override
    int getContentViewId() {
        return R.layout.root;
    }

    @Override
    int getFragmentContainerId() {
        return R.id.fragmentContainer;
    }
}
