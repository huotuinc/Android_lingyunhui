package com.huotu.lingyunhui.fragment;


import android.os.Bundle;
import android.view.View;

import com.huotu.lingyunhui.R;

public class AgentFragment extends BaseFragment {

    boolean init;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (!init){


            init=true;
        }
    }

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_agent;
    }

    @Override
    public void loadPage() {

    }
}
